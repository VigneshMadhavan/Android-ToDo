package com.example.vimadhavan.todo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vimadhavan.todo.utils.Constants;

/**
 * Created by vimadhavan on 4/9/2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    private Context context;
    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME + " ("
                + Constants.KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
                + Constants.KEY_TITLE + " TEXT, "
                + Constants.KEY_DATE + " TEXT, "
                + Constants.KEY_STATUS + " INTEGER NOT NULL DEFAULT 0, "
                + Constants.KEY_DESCRIPTION + " TEXT"
                +")";

        db.execSQL(table1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }
}
