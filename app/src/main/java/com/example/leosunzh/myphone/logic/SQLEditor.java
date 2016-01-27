package com.example.leosunzh.myphone.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.leosunzh.myphone.logic.MySQLHelper;
import com.example.leosunzh.myphone.logic.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作逻辑类
 * Created by leosunzh on 2015/12/12.
 */
public class SQLEditor {
    MySQLHelper helper;
    Context context;
    public SQLEditor(Context context){
        this.context = context;
        helper = new MySQLHelper(context);
    }

    /**
     * 增加数据
     * @param number
     * @param name
     */
    public void add(String id,String name,String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("name", name);
        values.put("number", number);
        db.insert("persons", null, values);
        db.close();
    }

    /**
     * 删除数据
     */
    public void delete(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("persons","name='"+name+"'",null);
        db.close();
    }

    /**
     * 修改数据
     */
    public void edit(String name,String newNumber,String newName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number",newNumber);
        values.put("name",newName);
        db.update("persons",values,"name='"+name+"'",null);
        db.close();
    }

    /**
     * 查询数据
     */
    public List<Person> query(String selection){
        List<Person> list = new ArrayList<Person>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("persons",null,selection,null,null,null,null);
        while (cursor.moveToNext()){
            String id = cursor.getString(1);
            String name = cursor.getString(2);
            String number = cursor.getString(3);
            list.add(new Person(id,name,number));
        }
        cursor.close();
        db.close();
        return list;
    }
}
