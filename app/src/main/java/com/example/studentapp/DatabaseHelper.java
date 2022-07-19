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

    public static final String TABLE_NAME_2 = "record_table";
    public static final String TABLE_COL_1_2 = "StudentName";
    public static final String TABLE_COL_2_2 = "Timestamp";
    public static final String TABLE_COL_3_2 = "AddStudent";
    public static final String TABLE_COL_4_2 = "DeleteStudent";
    public static final String TABLE_COL_5_2 = "TrainingGroup";
    public static final String TABLE_COL_6_2 = "ModifiedTG";
    public static final String TABLE_COL_7_2 = "Priority";
    public static final String TABLE_COL_8_2 = "ModifiedPR";
    public static final String TABLE_COL_9_2 = "Progress";
    public static final String TABLE_COL_10_2 = "ModifiedPG";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,TG TEXT,PG TEXT,PT TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME_2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,StudentName TEXT,Timestamp TEXT,AddStudent TEXT,DeleteStudent TEXT,TrainingGroup TEXT,ModifiedTG TEXT,Priority TEXT,ModifiedPR TEXT,Progress TEXT,ModifiedPG TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_2);
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
    public boolean updateData(int id,String name, String tg, String pg, String pt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // Accessing content for overwrite
        contentValues.put(TABLE_COL_1, name);
        contentValues.put(TABLE_COL_2, tg);
        contentValues.put(TABLE_COL_3, pg);
        contentValues.put(TABLE_COL_4, pt);
        db.update(TABLE_NAME,contentValues, "ID = ?", new String[]{Integer.toString(id)});
        return true;
    }

    // Delete Data from Database table
    public Integer deleteData(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{Integer.toString(id)});
    }

    public boolean insertRecord(String StudentName,String Timestamp,String AddStudent,String DeleteStudent,String TrainingGroup,String ModifiedTG,String Priority,String ModifiedPR,String Progress,String ModifiedPG){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COL_1_2, StudentName);
        contentValues.put(TABLE_COL_2_2, Timestamp);
        contentValues.put(TABLE_COL_3_2, AddStudent);
        contentValues.put(TABLE_COL_4_2, DeleteStudent);
        contentValues.put(TABLE_COL_5_2, TrainingGroup);
        contentValues.put(TABLE_COL_6_2, ModifiedTG);
        contentValues.put(TABLE_COL_7_2, Priority);
        contentValues.put(TABLE_COL_8_2, ModifiedPR);
        contentValues.put(TABLE_COL_9_2, Progress);
        contentValues.put(TABLE_COL_10_2, ModifiedPG);

        // Insert contents into database
        long success = db.insert(TABLE_NAME_2, null, contentValues);

        if(success == -1){ // when query not inserted into database
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllRecord(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " +TABLE_NAME_2,null);
        return cur;
    }

}
