package com.ruggery.databasedesign;

import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FollowActivity extends AppCompatActivity {

    DBHelper DB;
    MultiDBHelper multiDBHelper;
    private EditText searchUser;
    private Button followBtn;
    private TextView userFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        searchUser = (EditText) findViewById(R.id.searchUser);
        followBtn = (Button) findViewById(R.id.followBtn);
        userFound = (TextView) findViewById(R.id.userFound);

        DB = new DBHelper(this);
        multiDBHelper = new MultiDBHelper(this);
        initiate();

        String user = getIntent().getStringExtra("user_key");

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String leader = searchUser.getText().toString();
                String result = multiDBHelper.getUsername(leader);

                String user = getIntent().getStringExtra("user_key");
                userFound.setText(result);

                Boolean checkuser = DB.checkusername(result);

                if(checkuser){
                    multiDBHelper.addFollower(leader, user);
                    Toast.makeText(FollowActivity.this, "You have now followed " + leader, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(FollowActivity.this, "User does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initiate() {
        userFound = findViewById(R.id.userFound);
    }

}