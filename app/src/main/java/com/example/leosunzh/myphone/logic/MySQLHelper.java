package com.example.leosunzh.myphone.logic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库Helper
 * Created by leosunzh on 2015/12/12.
 */
public class MySQLHelper extends SQLiteOpenHelper{

    public MySQLHelper(Context context) {
        super(context,"myphone.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE persons(_id INTEGER PRIMARY KEY AUTOINCREMENT,id LONG,name VARCHAR(20),number LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
