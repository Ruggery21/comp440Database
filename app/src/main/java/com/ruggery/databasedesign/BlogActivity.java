package com.ruggery.databasedesign;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BlogActivity extends AppCompatActivity {

    ArrayList<NoteModel> arrayList;
    RecyclerView recyclerView;
    FloatingActionButton actionButton;
    MultiDBHelper multiDBHelper;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String currentDate = sdf.format(new Date());
    private String username;
    DateUtil Today = new DateUtil();
    private int clickCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        String user = getIntent().getStringExtra("user_key");
        username = user;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        actionButton = (FloatingActionButton) findViewById(R.id.add);
        multiDBHelper = new MultiDBHelper(this);
        displayNotes();

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    //display notes list
    public void displayNotes() {
        arrayList = new ArrayList<>(multiDBHelper.getNotes());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NotesAdapter adapter = new NotesAdapter(getApplicationContext(), this, arrayList, username);
        recyclerView.setAdapter(adapter);
    }

    //display dialog
    public void showDialog() {
        final EditText title, des, tags;
        Button submit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.dialog);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        title = (EditText) dialog.findViewById(R.id.title);
        des = (EditText) dialog.findViewById(R.id.description);
        tags = (EditText) dialog.findViewById(R.id.tags);
        submit = (Button) dialog.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("Please Enter Title");
                }else if(des.getText().toString().isEmpty()) {
                    des.setError("Please Enter Description");
                }else if(tags.getText().toString().isEmpty()) {
                        tags.setError("Please Enter a tag(s)");
                }else {
                    if((Today.isToday(Calendar.getInstance())) && clickCount <=2) {
                        multiDBHelper.addNotes(title.getText().toString(), des.getText().toString(), currentDate, username);
                        multiDBHelper.addTags(tags.getText().toString());
                        String blog_id = multiDBHelper.retrieveBlogID(username, title.getText().toString());
                        Blogtags(blog_id,tags.getText().toString());
                        dialog.cancel();
                        displayNotes();
                        clickCount++;
                    }
                    else{
                        Toast.makeText(BlogActivity.this, "Reached limit of 2 posts/day", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void Blogtags(String id,String tag){
        String[] tagSplit = tag.split("\\s*,\\s*");
        int num_Tags = tagSplit.length;
        for(int i = 0; i < num_Tags; i++){
            multiDBHelper.addBTags(id, tagSplit[i]);
        }
    }

}