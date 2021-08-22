package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    MultiDBHelper multiDBHelper;

    private ListView blogList;
    ArrayList<NoteModel> array;
    CommentAdapter commentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        String user = getIntent().getStringExtra("user_key");

        multiDBHelper = new MultiDBHelper(this);
        
        array = new ArrayList<>();
        blogList = findViewById(R.id.blogList);
        loadData();
        blogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int j = i + 1;
                String pos = String.valueOf(j);
                Intent intent = new Intent(CommentActivity.this, DisplayCommentActivity.class);
                intent.putExtra("pos_key", pos);
                intent.putExtra("user_key", user);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        array = multiDBHelper.getNotes();
        commentAdapter = new CommentAdapter(this, array);
        blogList.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
    }
}