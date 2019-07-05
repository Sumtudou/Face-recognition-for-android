package com.example.a11630.face_new;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context, "facedata.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String stu_table = "create table name_id (id text primary key ,name text)"; //name----id
        String stu_table1 = "create table time_id(id text ,time text)"; //time----id
//执行SQL语句
        db.execSQL(stu_table);
        db.execSQL(stu_table1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

  public  void Insert(SQLiteDatabase db, String table, String name, String id) {   ///db,table,name,id
        ContentValues cValue = new ContentValues();
        cValue.put("id", id);
        cValue.put("name", name);
        db.insert(table, null, cValue);
        System.out.println("插入成功："+id+"   "+name);
    }
    public  void Insert_two(SQLiteDatabase db, String table, String time, String id) {   ///db,table,time,id
        ContentValues cValue = new ContentValues();
        cValue.put("id", id);
        cValue.put("time", time);
        db.insert(table, null, cValue);
        System.out.println("插入time成功："+id+"   "+time);
    }
    public void Delete(SQLiteDatabase db, String table, String message) {
        String sql = "delete from " + table + " where  " + message;
        System.out.println("sqi语句："+sql);
        db.execSQL(sql);
    }
}
