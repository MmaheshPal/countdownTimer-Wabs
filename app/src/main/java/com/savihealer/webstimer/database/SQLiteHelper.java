package com.savihealer.webstimer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by devuser on 06-05-2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "timer.db" ;
    public final static String TABLE_NAME = "session" ;


    public SQLiteHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+"(srno INTEGER PRIMARY KEY AUTOINCREMENT,session TEXT)";
        db.execSQL(query);
        Log.i("Database", "Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertSessionString(String session){
        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("session",session);
        database.insert(TABLE_NAME,null,contentValues);
        Log.i("Session Inserted",session+"");
        database.close();
    }

    public void deleteRecord(int position){
        SQLiteDatabase database = this.getWritableDatabase();
        String whereClause = "srno" + "=?";
        String[] whereArgs = new String[] { String.valueOf(position) };
        database.delete(TABLE_NAME, whereClause, whereArgs);
        database.close();
    }


    public void deleteRecord(String session){
        SQLiteDatabase database = this.getWritableDatabase();
        String whereClause = "session" + "=?";
        String[] whereArgs = new String[] { String.valueOf(session) };
        database.delete(TABLE_NAME, whereClause, whereArgs);
        database.close();
    }

    public ArrayList<String> getList(){
        ArrayList<String> items = new ArrayList<String>();
        String selectQuery = "SELECT session FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do {
                items.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return items;
    }

    public void updateSessionData(String sessionString,String oldString) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("session", sessionString);
        database.update(TABLE_NAME, values, "session=?", new String[]{oldString});
        database.close();
    }
}
