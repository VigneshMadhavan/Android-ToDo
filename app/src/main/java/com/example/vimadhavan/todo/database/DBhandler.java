package com.example.vimadhavan.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;


import com.example.vimadhavan.todo.model.Task;
import com.example.vimadhavan.todo.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by vimadhavan on 4/9/2017.
 */

public class DBhandler {
    private SQLiteDatabase db;
    private final Context context;
    private final DBhelper dbHelper;
    private static DBhandler db_handler = null;

    public static DBhandler getInstance(Context context){
        try{
            if(db_handler == null){
                db_handler = new DBhandler(context);

            }
            db_handler.open();
        }catch(IllegalStateException e){
            //db_helper already open
        }
        return db_handler;
    }

    public DBhandler(Context context) {

        this.context = context;
        this.dbHelper = new DBhelper(context, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
    }
    public void close() {
        try {
            if (db.isOpen()) {
                db.close();
            }
        }catch (Exception e){

        }

    }



    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open database Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }




    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> tasks=new ArrayList<Task>();

        String query = "SELECT  * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);


        Task task=null;
        try {
            if (cursor.moveToFirst()) {
                do {
                    task = new Task();
                    task.setId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                    task.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
                    task.setTargetDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndex(Constants.KEY_STATUS)));
                    task.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }


        Collections.sort(tasks);

        return tasks;

    }

    public ArrayList<Task> getAllCompletedTasks(){
        ArrayList<Task> tasks=new ArrayList<Task>();

        String query = "SELECT  * FROM " + Constants.TABLE_NAME +" WHERE "+Constants.KEY_STATUS +"=1";
        Cursor cursor = db.rawQuery(query, null);


        Task task=null;
        try {
            if (cursor.moveToFirst()) {
                do {
                    task = new Task();
                    task.setId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                    task.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
                    task.setTargetDate(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATE)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndex(Constants.KEY_STATUS)));
                    task.setDescription(cursor.getString(cursor.getColumnIndex(Constants.KEY_DESCRIPTION)));
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }


        Collections.sort(tasks);

        return tasks;

    }


    public long addTask(Task task) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.KEY_TITLE,task.getTitle());
        initialValues.put(Constants.KEY_DESCRIPTION, task.getDescription());
        initialValues.put(Constants.KEY_DATE,task.getTargetDate());

        return db.insert(Constants.TABLE_NAME , null, initialValues);
    }

    public int updateTask(Task task){
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Constants.KEY_TITLE,task.getTitle());
        updatedValues.put(Constants.KEY_DESCRIPTION, task.getDescription());
        updatedValues.put(Constants.KEY_DATE,task.getTargetDate());
        updatedValues.put(Constants.KEY_STATUS,task.getStatus());

        return db.update(Constants.TABLE_NAME,updatedValues,Constants.KEY_ID +"=?",new String []{String.valueOf(task.getId())});
    }

    public int deleteTask(Task task){

        return db.delete(Constants.TABLE_NAME,Constants.KEY_ID +"=?",new String []{String.valueOf(task.getId())});
    }



}
