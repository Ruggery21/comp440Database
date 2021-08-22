package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayCommentActivity extends AppCompatActivity {
    MultiDBHelper multiDBHelper;

    private ListView commentList;
    ArrayList<NoteModel> array;
    DisplayCommentAdapter adapter;

    private String currentUser;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String currentDate = sdf.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comment);

        String pos = getIntent().getStringExtra("pos_key");
        currentUser = getIntent().getStringExtra("user_key");

        multiDBHelper = new MultiDBHelper(this);

        array = new ArrayList<>();
        commentList = findViewById(R.id.commentList);
        loadData(pos);
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String commentID = multiDBHelper.retrieveComment(pos, currentUser);
                if(commentID != "") {
                    Toast.makeText(DisplayCommentActivity.this, "This is position: " + pos + "\n Current User: " + currentUser + "\n Comment ID: " + commentID, Toast.LENGTH_SHORT).show();
                    showCommentDialog(commentID, pos);
                }
                else{
                    Toast.makeText(DisplayCommentActivity.this, "Current User can not edit this Comment.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData(String position) {
        array = multiDBHelper.getComments(position);
        adapter = new DisplayCommentAdapter(this, array);
        commentList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void showCommentDialog(String commentID, String pos) {
        final EditText commentDescription;
        final Spinner sentiment;
        Button commentSubmit;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        dialog.setContentView(R.layout.updatecomment_layout);
        params.copyFrom(dialog.getWindow().getAttributes());
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        commentDescription = (EditText) dialog.findViewById(R.id.commentdDescription);
        commentSubmit = (Button) dialog.findViewById(R.id.commentSubmit);
        sentiment = (Spinner) dialog.findViewById(R.id.sentiment);

        commentSubmit.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View v) {
                String senti = sentiment.getSelectedItem().toString();
                String name = multiDBHelper.getUsername(pos);
                if(!currentUser.equals(name)) {
                    if (senti.equals("delete")) {
                        multiDBHelper.deleteComment(commentID);
                        dialog.cancel();
                        adapter.notifyDataSetChanged();
                    }
                    else if(commentDescription.getText().toString().isEmpty()){
                        commentDescription.setError("Please Enter Description");
                    }
                    else {
                        //updating note
                        multiDBHelper.updateComment(senti, commentDescription.getText().toString(), currentDate, commentID);
                        dialog.cancel();
                        //notify list
                        adapter.notifyDataSetChanged();
                    }
                }
                else{
                    Toast.makeText(DisplayCommentActivity.this, "Current User cannot post self comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}