package com.kmw.soom2.Common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    public class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(AlarmDataBase.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + AlarmDataBase.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public long insertColumn(String userid, String selectDay, long selectTime, int pushCheck) {
        ContentValues values = new ContentValues();
        values.put(AlarmDataBase.CreateDB.USERID, userid);
        values.put(AlarmDataBase.CreateDB.SELECT_DAY, selectDay);
        values.put(AlarmDataBase.CreateDB.SELECT_TIME, selectTime);
        values.put(AlarmDataBase.CreateDB.PUSH_CHECK, pushCheck);
        return mDB.insert(AlarmDataBase.CreateDB._TABLENAME0, null, values);

    }

    public boolean updateColumn(long id, String userid, String selectDay, long selectTime, int pushCheck){
        ContentValues values = new ContentValues();
        values.put(AlarmDataBase.CreateDB.USERID, userid);
        values.put(AlarmDataBase.CreateDB.SELECT_DAY, selectDay);
        values.put(AlarmDataBase.CreateDB.SELECT_TIME, selectTime);
        values.put(AlarmDataBase.CreateDB.PUSH_CHECK, pushCheck);

        return mDB.update(AlarmDataBase.CreateDB._TABLENAME0, values, "_id=" + id, null) > 0;
    }

    public boolean deleteColumn(long id){
        return mDB.delete(AlarmDataBase.CreateDB._TABLENAME0, "_id="+id, null) > 0;
    }

    public Cursor selectColumns(){
        return mDB.query(AlarmDataBase.CreateDB._TABLENAME0, null, null, null, null, null, null);
    }


    public void deleteAllColumns() {
        mDB.delete(AlarmDataBase.CreateDB._TABLENAME0, null, null);
    }

    public void create(){
        mDBHelper.onCreate(mDB);
    }

    public void close(){
        mDB.close();
    }
}
