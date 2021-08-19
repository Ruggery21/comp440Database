package com.ruggery.databasedesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MultiDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbwithusername.db";

    public MultiDBHelper(Context context) {
        super(context, "dbwithusername.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase multiDB) {
        multiDB.execSQL("create Table blogs(blog_id INTEGER primary key, " +
                "subject TEXT, " +
                "description TEXT, " +
                "pdate TEXT, " +
                "created_by TEXT, " +
                "FOREIGN KEY (created_by) REFERENCES users (username))");

        multiDB.execSQL("create Table displaytags(blog_id INTEGER primary key, tag TEXT)");

        multiDB.execSQL("create Table blogstags(blog_id INTEGER, " +
                "tag TEXT, " +
                "PRIMARY KEY(blog_id, tag), " +
                "FOREIGN KEY (blog_id) REFERENCES blogs (blog_id))");

        multiDB.execSQL("create Table comments(comment_id INTEGER primary key AUTOINCREMENT, " +
                "sentiment TEXT, description TEXT, cdate TEXT, " +
                "blog_id INTEGER, posted_by TEXT, " +
                "FOREIGN KEY (blog_id) REFERENCES blogs (blog_id), " +
                "FOREIGN KEY (posted_by) REFERENCES users (username), " +
                "CONSTRAINT sentiment_types CHECK((sentiment in ('negative','positive'))))");

        multiDB.execSQL("create Table follows(leadername TEXT, " +
                "followername TEXT, " +
                "PRIMARY KEY (leadername, followername), " +
                "FOREIGN KEY (leadername) REFERENCES users (username), " +
                "FOREIGN KEY (followername) REFERENCES users (username))");

        multiDB.execSQL("create Table hobbies(username TEXT, " +
                "hobby TEXT, " +
                "PRIMARY KEY(username, hobby), " +
                "FOREIGN KEY(username) REFERENCES users (username), " +
                "CONSTRAINT hobby_types CHECK((hobby in('hiking','swimming','calligraphy','bowling','movie','cooking','dancing'))))");

        multiDB.execSQL("create Table users(username TEXT, " +
                "password TEXT, " +
                "email TEXT, " +
                "PRIMARY KEY(username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase multiDB, int i, int i1) {
        multiDB.execSQL("DROP TABLE IF EXISTS blogs");
        multiDB.execSQL("DROP TABLE IF EXISTS displaytags");
        multiDB.execSQL("DROP TABLE IF EXISTS comments");
        multiDB.execSQL("DROP TABLE IF EXISTS hobbies");
        multiDB.execSQL("DROP TABLE IF EXISTS follows");
    }

    public void addUser(String username, String password, String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        MyDB.insert("users", null, contentValues);

    }

    public void addNotes(String title, String des, String date, String creator) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("subject", title);
        values.put("description", des);
        values.put("pdate", date);
        values.put("created_by", creator);

        //inserting new row
        sqLiteDatabase.insert("blogs", null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    public void addBTags(String id, String tags) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("blog_id", id);
        values.put("tag", tags);

        //inserting new row
        sqLiteDatabase.insert("blogstags", null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    public void addTags(String tags) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tag", tags);

        //inserting new row
        sqLiteDatabase.insert("displaytags", null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    public void addComment(String senti, String des, String date, String id, String creator) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sentiment", senti);
        values.put("description", des);
        values.put("cdate", date);
        values.put("blog_id", id);
        values.put("posted_by", creator);

        //inserting new row
        sqLiteDatabase.insert("comments", null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    public void addHobby(String user, String hobbies){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hobby", hobbies);
        values.put("username", user);

        sqLiteDatabase.insert("hobbies", null, values);
        sqLiteDatabase.close();
    }

    public void addFollower(String leader, String follower){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("leadername", leader);
        values.put("followername", follower);

        sqLiteDatabase.insert("follows", null, values);
        sqLiteDatabase.close();
    }

    public ArrayList<NoteModel> getNotes() {                  //needs to update the tags
        ArrayList<NoteModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT B.blog_id, B.subject, B.description, T.tag FROM blogs AS B, displaytags AS T WHERE B.blog_id = T.blog_id"; //"SELECT *FROM " + "blogs"

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getString(0));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setDes(cursor.getString(2));
                noteModel.setTags(cursor.getString(3));
                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    public ArrayList<NoteModel> getComments(String position) {
        ArrayList<NoteModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT comment_id, sentiment, description, posted_by FROM comments WHERE blog_id =" + position;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteModel noteModel = new NoteModel();
                noteModel.setID(cursor.getString(0));
                noteModel.setTitle(cursor.getString(1));
                noteModel.setDes(cursor.getString(2));
                noteModel.setTags(cursor.getString(3));
                arrayList.add(noteModel);
            }while (cursor.moveToNext());
        }
        return arrayList;
    }

    public void delete(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete("blogs", "blog_id=" + ID, null); //made a change here
        sqLiteDatabase.close();
    }

    public void deleteTags(String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //deleting row
        sqLiteDatabase.delete("displaytags", "blog_id=" + ID, null); //made a change here
        sqLiteDatabase.close();
    }

    public void updateNote(String title, String des, String date, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("subject", title);
        values.put("description", des);
        values.put("pdate", date);

        //updating row
        sqLiteDatabase.update("blogs", values, "blog_id=" + ID, null);
        sqLiteDatabase.close();
    }

    public void updateTags(String tags, String ID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("tag", tags);

        //updating row
        sqLiteDatabase.update("displaytags", values, "blog_id=" + ID, null);
        sqLiteDatabase.close();
    }

    public String getUsername(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT created_by FROM blogs WHERE blog_id = " + id, null);
        String data = "";
        if(cursor.moveToFirst()){
            do{
                data = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return data;
    }

    public String posBlog(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT subject FROM blogs AS B, comments AS C WHERE B.created_by = '" + user + "' " +
                "AND B.blog_id = (SELECT C.blog_id FROM comments WHERE C.sentiment = 'positive')", null);
        StringBuilder result = new StringBuilder("");
        if(cursor.moveToFirst()){
            do {
                result.append(cursor.getString(0) + "\n");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return String.valueOf(result);
    }

    public String mostBlogs(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT created_by, count(*) as blog_count FROM blogs " +
                "WHERE pdate = '" + date + "' group by created_by " +
                "HAVING blog_count = (SELECT max(c) FROM (SELECT created_by, count(*) AS C FROM blogs " +
                "WHERE pdate = '" + date + "' group by created_by) AS T)", null);
        StringBuilder result = new StringBuilder("");
        if(cursor.moveToFirst()){
            do {
                result.append(cursor.getString(0) + " count: ");
                result.append(cursor.getString(1) + "\n");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return String.valueOf(result);
    }

    public String followBy(String x, String y){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT F1.leadername FROM follows AS F1, follows AS F2 WHERE F1.leadername = F2.leadername " +
                "AND F1.followername = '" + x +"' " +
                "AND F2.followername = '" + y + "'", null);
        StringBuilder result = new StringBuilder("");
        if(cursor.moveToFirst()){
            do {
                result.append(cursor.getString(0) + "\n");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return String.valueOf(result);
    }

    public String tagBlogs(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT subject FROM blogstags AS B, blogs AS C " +
                "WHERE tag = '" + tag +"' " +
                "AND B.blog_id = (SELECT C.blog_id FROM blogs)", null);
        StringBuilder result = new StringBuilder("");
        if(cursor.moveToFirst()){
            do {
                result.append(cursor.getString(0) + "\n");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return String.valueOf(result);
    }

    public String neverPosted(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users EXCEPT SELECT posted_by FROM comments", null);
        StringBuilder result = new StringBuilder("");
        if(cursor.moveToFirst()){
            do {
                result.append(cursor.getString(0) + "\n");
            }while(cursor.moveToNext());
        }
        cursor.close();
        return String.valueOf(result);
    }
}
