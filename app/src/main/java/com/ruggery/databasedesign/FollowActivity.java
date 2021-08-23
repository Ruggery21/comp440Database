package com.ruggery.databasedesign;

import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FollowActivity extends AppCompatActivity {

    private TextView mTextView;
    MultiDBHelper multiDBHelper;
    private EditText searchUser;
    private Button followBtn;
    private TextView userFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        mTextView = (TextView) findViewById(R.id.text);

        multiDBHelper = new MultiDBHelper(this);

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = searchUser.getText().toString();
                String result = multiDBHelper.posBlog(user);
                userFound.setText(result);
            }
        });

    }
}