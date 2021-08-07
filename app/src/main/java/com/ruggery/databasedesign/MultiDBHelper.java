package com.ruggery.databasedesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MultiDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbwithusername.db";

    public MultiDBHelper(Context context) {
        super(context, "dbwithusername.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase multiDB) {
        multiDB.execSQL("create Table blogs(blog_id INTEGER primary key, subject TEXT, description TEXT, pdate TEXT, created_by TEXT)");

        multiDB.execSQL("create Table blogstags(blog_id INTEGER primary key, tag TEXT)");

        multiDB.execSQL("create Table comments(comment_id INTEGER primary key, sentiment TEXT, description TEXT, cdate TEXT, blog_id INTEGER, posted_by TEXT)");

        multiDB.execSQL("create Table hobbies(username TEXT primary key, hobby TEXT)");

        multiDB.execSQL("create Table follows(leadername TEXT primary key, followername TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase multiDB, int i, int i1) {
        multiDB.execSQL("DROP TABLE IF EXISTS blogs");
        multiDB.execSQL("DROP TABLE IF EXISTS blogstags");
        multiDB.execSQL("DROP TABLE IF EXISTS comments");
        multiDB.execSQL("DROP TABLE IF EXISTS hobbies");
        multiDB.execSQL("DROP TABLE IF EXISTS follows");
    }

    public boolean initialize(String initial){
        SQLiteDatabase multiDB = this.getWritableDatabase();
        Cursor cursor = multiDB.rawQuery("Select * from blogs where subject = ?", new String[]{initial});
        if(cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
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

    public void addTags(String tags) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tag", tags);

        //inserting new row
        sqLiteDatabase.insert("blogstags", null , values);
        //close database connection
        sqLiteDatabase.close();
    }

    public ArrayList<NoteModel> getNotes() {                  //needs to update the tags
        ArrayList<NoteModel> arrayList = new ArrayList<>();

        // select all query
        String select_query= "SELECT B.blog_id, B.subject, B.description, T.tag FROM blogs AS B, blogstags AS T WHERE B.blog_id = T.blog_id"; //"SELECT *FROM " + "blogs"

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
        sqLiteDatabase.delete("blogstags", "blog_id=" + ID, null); //made a change here
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
        sqLiteDatabase.update("blogstags", values, "blog_id=" + ID, null);
        sqLiteDatabase.close();
    }

}
