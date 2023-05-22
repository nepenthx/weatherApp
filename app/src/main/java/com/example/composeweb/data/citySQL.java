package com.example.composeweb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class citySQL extends SQLiteOpenHelper {
    public static final String CREATE_CITY="create table city ("
            +"id integer primary key autoincrement,"
            +"name text)";
    private Context mContext;
    public citySQL(Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
        super(context, name, factory, version);
        context=mContext;
    }
        @Override
        public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_CITY);
        }
        @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
        {

        }
}
