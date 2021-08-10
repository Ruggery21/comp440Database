package com.ruggery.databasedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText usernameSignUp, passwordSignUp, confirmPassword;
    private EditText firstName, lastName, emailSignUp;
    private Button signUpButton, existUserButton;

    DBHelper DB;
    MultiDBHelper multiDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameSignUp = findViewById(R.id.usernameSignUp);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        confirmPassword = findViewById(R.id.confirmPassword);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        emailSignUp = findViewById(R.id.emailSignUp);

        signUpButton = findViewById(R.id.signUpButton);
        existUserButton = findViewById(R.id.existUserButton);

        DB = new DBHelper(this);
        multiDBHelper = new MultiDBHelper(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usernameSignUp.getText().toString();
                String pass = passwordSignUp.getText().toString();
                String repass = confirmPassword.getText().toString();
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String email = emailSignUp.getText().toString();

                if(user.equals("")||pass.equals("")||repass.equals("")||fName.equals("")||lName.equals("")||email.equals("")){
                    Toast.makeText(MainActivity.this, "Please enter all the fields!", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        Boolean checkemail = DB.checkuseremail(email);
                        if(checkuser == false && checkemail == false){
                            Boolean insert = DB.insertData(user, pass, fName, lName, email);
                            multiDBHelper.addUser(user, pass, email);
                            if(insert == true){
                                Toast.makeText(MainActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.putExtra("user_key", user);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(checkemail==true){
                            Toast.makeText(MainActivity.this, "Email already exists!!! Try Again", Toast.LENGTH_SHORT).show();
                        }
                        else if(checkuser==true){
                            Toast.makeText(MainActivity.this, "User already exists!!! Try Again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Passwords not matching. Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        existUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}