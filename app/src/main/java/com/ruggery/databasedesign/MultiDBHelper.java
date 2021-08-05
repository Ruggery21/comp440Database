package com.ruggery.databasedesign;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.Nullable;

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
}
