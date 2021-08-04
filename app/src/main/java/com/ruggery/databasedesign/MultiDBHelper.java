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

    //columns for blog table
    /*public static final String TABLE_BLOGS = "blogs";
    public static final String COLUMN_BLOG_ID = "blog_id";
    public static final String COLUMN_BLOG_SUBJECT = "subject";
    public static final String COLUMN_BLOG_DESCRIPTION = "description";
    public static final String COLUMN_BLOG_PDATE = "pdate";
    public static final String COLUMN_BLOG_CREATED_BY = "created_by";

    //columns for blog tags table
    public static final String TABLE_BLOGSTAGS = "blogstags";
    public static final String COLUMN_BLOGSTAG_ID = "blog_id";
    public static final String COLUMN_BLOGSTAG_TAG = "tag";

    //columns for comment table
    public static final String TABLE_COMMENTS = "comments";
    public static final String COLUMN_COMMENT_ID = "comment_id";
    public static final String COLUMN_COMMENT_SENTIMENT = "sentiment";
    public static final String COLUMN_COMMENT_DESCRIPTION = "description";
    public static final String COLUMN_COMMENT_CDATE = "cdate";
    public static final String COLUMN_COMMENT_BLOGID = "blog_id";
    public static final String COLUMN_COMMENT_POSTED_BY = "posted_by";

    //columns for hobbies table
    public static final String TABLE_HOBBIES = "hobbies";
    public static final String COLUMN_HOBBY_USERNAME = "username";
    public static final String COLUMN_HOBBY_HOBBY = "hobby";

    //columns for followers table
    public static final String TABLE_FOLLOWS = "follows";
    public static final String COLUMN_FOLLOW_LEADERNAME = "leadername";
    public static final String COLUMN_FOLLOW_FOLLOWERNAME = "followername";

    private static final String SQL_CREATE_TABLE_BLOGS = "CREATE TABLE " + TABLE_BLOGS + "("
            + COLUMN_BLOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BLOG_SUBJECT + " TEXT, "
            + COLUMN_BLOG_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_BLOG_PDATE + " TEXT NOT NULL, " //MIGHT NEED TO BE INTEGER
            + COLUMN_BLOG_CREATED_BY + " TEXT NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_BLOGSTAGS = "CREATE TABLE " + TABLE_BLOGSTAGS + "("
            + COLUMN_BLOGSTAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BLOGSTAG_TAG + " TEXT NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_COMMENTS = "CREATE TABLE " + TABLE_COMMENTS + "("
            + COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_COMMENT_SENTIMENT + " TEXT NOT NULL, "
            + COLUMN_COMMENT_DESCRIPTION + " TEXT NOT NULL, "
            + COLUMN_COMMENT_CDATE + " TEXT NOT NULL, " //MIGHT NEED TO BE INTEGER " INTEGER NOT NULL, "
            + COLUMN_COMMENT_BLOGID + " TEXT NOT NULL, "  //MIGHT NEED TO BE INTEGER
            +COLUMN_COMMENT_POSTED_BY + " TEXT NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_HOBBIES = "CREATE TABLE " + TABLE_HOBBIES + "("
            + COLUMN_HOBBY_USERNAME + " TEXT NOT NULL, "
            + COLUMN_HOBBY_HOBBY + " TEXT NOT NULL"
            + ");";

    private static final String SQL_CREATE_TABLE_FOLLOWS = "CREATE TABLE " + TABLE_FOLLOWS + "("
            + COLUMN_FOLLOW_LEADERNAME + " TEXT NOT NULL, "
            + COLUMN_FOLLOW_FOLLOWERNAME + " TEXT NOT NULL"
            + ");";


                                                */
    @Override
    public void onCreate(SQLiteDatabase multiDB) {
        //multiDB.execSQL(SQL_CREATE_TABLE_BLOGS);
        multiDB.execSQL("create Table blogs(blog_id INTEGER primary key, subject TEXT, description TEXT, pdate TEXT, created_by TEXT)");
        //multiDB.execSQL(SQL_CREATE_TABLE_BLOGSTAGS);
        multiDB.execSQL("create Table blogstags(blog_id INTEGER primary key, tag TEXT)");
        //multiDB.execSQL(SQL_CREATE_TABLE_COMMENTS);
        multiDB.execSQL("create Table comments(comment_id INTEGER primary key, sentiment TEXT, description TEXT, cdate TEXT, blog_id INTEGER, posted_by TEXT)");
        //multiDB.execSQL(SQL_CREATE_TABLE_HOBBIES);
        multiDB.execSQL("create Table hobbies(username TEXT primary key, hobby TEXT)");
        //multiDB.execSQL(SQL_CREATE_TABLE_FOLLOWS);
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
