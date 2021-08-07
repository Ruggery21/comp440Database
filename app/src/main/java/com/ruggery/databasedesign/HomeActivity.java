package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private Button initializeButton;
    private Button blogBtn;
    private TextView userView;

    MultiDBHelper multiDB;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Gets username from login page
        String user = getIntent().getStringExtra("user_key");

        initializeButton = findViewById(R.id.initializeButton);
        blogBtn = findViewById(R.id.navToBlog);

        //Displays what user is logged in
        userView = findViewById(R.id.userView);
        userView.setText(user);

        //Database this here
        multiDB = new MultiDBHelper(this);

        initializeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean initial = multiDB.initialize("null");
                if(initial == false){
                    Toast.makeText(HomeActivity.this, "Initializing Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Opening blogs", Toast.LENGTH_SHORT).show();

                Intent blogIntent = new Intent(getApplicationContext(), BlogActivity.class);
                blogIntent.putExtra("user_key", user);
                startActivity(blogIntent);
            }
        });
    }
}