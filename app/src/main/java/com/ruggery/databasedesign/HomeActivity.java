package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity{
    private TextView userView;
    private Button initializeButton;
    private Button blogBtn;
    private Spinner hobbySpinner;
    private Button submitHobby;

    MultiDBHelper multiDB;

    private TextView testView;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Gets username from login page
        String user = getIntent().getStringExtra("user_key");
        //Displays what user is logged in
        userView = findViewById(R.id.userView);
        userView.setText(user);

        initializeButton = findViewById(R.id.initializeButton);
        blogBtn = findViewById(R.id.navToBlog);

        hobbySpinner = findViewById(R.id.hobbySpinner);
        submitHobby = findViewById(R.id.submitHobby);

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

        submitHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hobby = hobbySpinner.getSelectedItem().toString();
                multiDB.addHobby(user, hobby);
                Toast.makeText(HomeActivity.this, hobby, Toast.LENGTH_SHORT).show();
            }
        });

     testView = findViewById(R.id.testView);
     testButton = findViewById(R.id.testButton);

     testButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             //String name = multiDB.getUsername();
             //testView.setText(name);
         }
     });



    }
}