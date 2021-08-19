package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class QueryActivity extends AppCompatActivity {

    private EditText posUser, mostDate, userX, userY, tagBox;
    private Button posSearch, mostSearch, followSearch, tagSearch, neverSearch;
    private TextView posResults, mostResults, followResults, tagResults, neverResults;

    MultiDBHelper multiDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        multiDBHelper = new MultiDBHelper(this);
        initiate();

        posSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = posUser.getText().toString();
                String result = multiDBHelper.posBlog(user);
                posResults.setText(result);
            }
        });

        mostSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = mostDate.getText().toString();
                String result = multiDBHelper.mostBlogs(date);
                mostResults.setText(result);
            }
        });

        followSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userA = userX.getText().toString();
                String userB = userY.getText().toString();
                String result = multiDBHelper.followBy(userA, userB);
                followResults.setText(result);
            }
        });

        tagSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = tagBox.getText().toString();
                String result = multiDBHelper.tagBlogs(tag);
                tagResults.setText(result);
            }
        });

        neverSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = multiDBHelper.neverPosted();
                neverResults.setText(result);
            }
        });
    }

    private void initiate() {
        posUser = findViewById(R.id.posUser);
        mostDate = findViewById(R.id.mostDate);
        userX = findViewById(R.id.userX);
        userY = findViewById(R.id.userY);
        tagBox = findViewById(R.id.tagBox);

        posSearch = findViewById(R.id.posSearch);
        mostSearch = findViewById(R.id.mostSearch);
        followSearch = findViewById(R.id.followSearch);
        tagSearch = findViewById(R.id.tagSearch);
        neverSearch = findViewById(R.id.neverSearch);

        posResults = findViewById(R.id.posResults);
        mostResults = findViewById(R.id.mostResults);
        followResults = findViewById(R.id.followResults);
        tagResults = findViewById(R.id.tagResults);
        neverResults = findViewById(R.id.neverResults);
    }
}