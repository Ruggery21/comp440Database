package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayCommentActivity extends AppCompatActivity {
    MultiDBHelper multiDBHelper;

    private ListView commentList;
    ArrayList<NoteModel> array;
    DisplayCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comment);

        String pos = getIntent().getStringExtra("pos_key");

        multiDBHelper = new MultiDBHelper(this);

        array = new ArrayList<>();
        commentList = findViewById(R.id.commentList);
        loadData(pos);
    }

    private void loadData(String position) {
        array = multiDBHelper.getComments(position);
        adapter = new DisplayCommentAdapter(this, array);
        commentList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}