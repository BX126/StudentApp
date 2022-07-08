package com.example.studentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "student.db";
    public static final String TABLE_NAME = "student_table";
    public static final String TABLE_COL_1 = "name";
    public static final String TABLE_COL_2 = "tg";
    public static final String TABLE_COL_3 = "PG";
    public static final String TABLE_COL_4 = "pt";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TG TEXT,PG TEXT,PT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Performing Insert Operation on Database
    public boolean insertData(String name, String tg, String pg, String pt){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COL_1, name);
        contentValues.put(TABLE_COL_2, tg);
        contentValues.put(TABLE_COL_3, pg);
        contentValues.put(TABLE_COL_4, pt);

        // Insert contents into database
        long success = db.insert(TABLE_NAME, null, contentValues);

        if(success == -1){ // when query not inserted into database
            return false;
        }else{
            return true;
        }
    }


    // Read all Data from Database using CURSOR to pick one by one row
    public Cursor getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " +TABLE_NAME,null);
        return cur;
    }

    // Update Data of Database table
    public boolean updateData(String name, String tg, String pg, String pt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // Accessing content for overwrite
        contentValues.put(TABLE_COL_1, name);
        contentValues.put(TABLE_COL_2, tg);
        contentValues.put(TABLE_COL_3, pg);
        contentValues.put(TABLE_COL_4, pt);
        db.update(TABLE_NAME,contentValues, "NAME = ?", new String[]{name});
        return true;
    }

    // Delete Data from Database table
    public Integer deleteData(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ?", new String[]{name});
    }

}
