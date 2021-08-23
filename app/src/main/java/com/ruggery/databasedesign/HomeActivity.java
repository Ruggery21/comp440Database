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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity{
    private TextView userView;
    private Button initializeButton;
    private Button blogBtn;
    private Spinner hobbySpinner;
    private Button submitHobby;
    private Button followUsers;

    private Button navToComment;
    private Button query;

    MultiDBHelper multiDB;

    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> pass = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    ArrayList<String> name2 = new ArrayList<>();
    ArrayList<String> hobb = new ArrayList<>();
    ArrayList<String> leader = new ArrayList<>();
    ArrayList<String> follower = new ArrayList<>();
    ArrayList<String> sub = new ArrayList<>();
    ArrayList<String> des = new ArrayList<>();
    ArrayList<String> pdate = new ArrayList<>();
    ArrayList<String> creator = new ArrayList<>();
    ArrayList<String> tag = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();
    ArrayList<String> tag_2 = new ArrayList<>();
    ArrayList<String> s = new ArrayList<>();
    ArrayList<String> d = new ArrayList<>();
    ArrayList<String> c = new ArrayList<>();
    ArrayList<String> b = new ArrayList<>();
    ArrayList<String> p = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String user = getIntent().getStringExtra("user_key");
        userView = findViewById(R.id.userView);
        userView.setText(user);

        initializeButton = findViewById(R.id.initializeButton);
        blogBtn = findViewById(R.id.navToBlog);

        hobbySpinner = findViewById(R.id.hobbySpinner);
        submitHobby = findViewById(R.id.submitHobby);

        navToComment = findViewById(R.id.navToComment);
        query = findViewById(R.id.query);

        followUsers = findViewById(R.id.followUsers);

        multiDB = new MultiDBHelper(this);

        initializeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonDataFromAsset());
                    JSONArray jsonUser = jsonObject.getJSONArray("users");
                    initiateUsers(jsonUser);
                    JSONArray jsonHobby = jsonObject.getJSONArray("hobbies");
                    initiateHobbies(jsonHobby);
                    JSONArray jsonFollow = jsonObject.getJSONArray("follows");
                    initiateFollows(jsonFollow);
                    JSONArray jsonBlog = jsonObject.getJSONArray("blogs");
                    initiateBlogs(jsonBlog);
                    JSONArray jsonBT = jsonObject.getJSONArray("blogstags");
                    initiateBlogTags(jsonBT);
                    JSONArray jsonDT = jsonObject.getJSONArray("displaytags");
                    initiateDisplayTags(jsonDT);
                    JSONArray jsonCom = jsonObject.getJSONArray("comments");
                    initiateComments(jsonCom);
                } catch (JSONException e) {
                    e.printStackTrace();
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

        navToComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CommentActivity.class);
                intent.putExtra("user_key", user);
                startActivity(intent);
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QueryActivity.class);
                startActivity(intent);
            }
        });

        followUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FollowActivity.class);
                startActivity(intent);
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
    }

    private String jsonDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("databaseValues.json");
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void initiateUsers(JSONArray jsonUser){
        try {
            String user[] = new String[3];
            for(int i = 0; i < jsonUser.length(); i++){
                JSONObject userData = jsonUser.getJSONObject(i);
                name.add(userData.getString("username"));
                pass.add(userData.getString("password"));
                email.add(userData.getString("email"));
                user[0] = name.get(i);
                user[1] = pass.get(i);
                user[2] = email.get(i);
                multiDB.addUser(user[0], user[1], user[2]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiateHobbies(JSONArray jsonHobby){
        try {
            String hob[] = new String[2];
            for(int i = 0; i < jsonHobby.length(); i++){
                JSONObject userData = jsonHobby.getJSONObject(i);
                name2.add(userData.getString("username"));
                hobb.add(userData.getString("hobby"));
                hob[0] = name2.get(i);
                hob[1] = hobb.get(i);
                multiDB.addHobby(hob[0], hob[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiateFollows(JSONArray jsonFollow){
        try {
            String fol[] = new String[2];
            for(int i = 0; i < jsonFollow.length(); i++){
                JSONObject userData = jsonFollow.getJSONObject(i);
                leader.add(userData.getString("leadername"));
                follower.add(userData.getString("followername"));
                fol[0] = leader.get(i);
                fol[1] = follower.get(i);
                multiDB.addFollower(fol[0], fol[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiateBlogs(JSONArray jsonBlog){
        try {
            String blog[] = new String[4];
            for(int i = 0; i < jsonBlog.length(); i++){
                JSONObject userData = jsonBlog.getJSONObject(i);
                sub.add(userData.getString("subject"));
                des.add(userData.getString("description"));
                pdate.add(userData.getString("pdate"));
                creator.add(userData.getString("created_by"));
                blog[0] = sub.get(i);
                blog[1] = des.get(i);
                blog[2] = pdate.get(i);
                blog[3] = creator.get(i);
                multiDB.addNotes(blog[0], blog[1], blog[2], blog[3]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiateBlogTags(JSONArray jsonBT){
        try {
            String bt[] = new String[2];
            for(int i = 0; i < jsonBT.length(); i++){
                JSONObject userData = jsonBT.getJSONObject(i);
                id.add(userData.getString("blog_id"));
                tag.add(userData.getString("tag"));
                bt[0] = id.get(i);
                bt[1] = tag.get(i);
                multiDB.addBTags(bt[0], bt[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiateDisplayTags(JSONArray jsonDT){
        try {
            String dt[] = new String[2];
            for(int i = 0; i < jsonDT.length(); i++){
                JSONObject userData = jsonDT.getJSONObject(i);
                //temp.add(userData.getString("blog_id"));
                tag_2.add(userData.getString("tag"));
                dt[0] = tag_2.get(i);
                //dt[1] = temp2.get(i);
                multiDB.addTags(dt[0]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initiateComments(JSONArray jsonCom){
        try {
            String com[] = new String[5];
            for(int i = 0; i < jsonCom.length(); i++){
                JSONObject userData = jsonCom.getJSONObject(i);
                s.add(userData.getString("sentiment"));
                d.add(userData.getString("description"));
                c.add(userData.getString("cdate"));
                b.add(userData.getString("blog_id"));
                p.add(userData.getString("posted_by"));
                com[0] = s.get(i);
                com[1] = d.get(i);
                com[2] = c.get(i);
                com[3] = b.get(i);
                com[4] = p.get(i);
                multiDB.addComment(com[0], com[1], com[2], com[3], com[4]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}