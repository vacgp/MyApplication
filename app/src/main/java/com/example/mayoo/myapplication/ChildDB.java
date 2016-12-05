package com.example.mayoo.myapplication;

/**
 * Created by mayoo on 11/26/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class ChildDB {

    private static final String DATABASE_NAME = "login.db";

    private static String tableName = "CHILD";

    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + ChildDB.tableName + "( " + "ID" + " integer primary key autoincrement,"
            + "NAME  text, BIRTH text, GENDER text, IMAGE blob );";

    // Variable to hold the database instance
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    private SQLiteDB dbHelper;

    //connect to db
    ChildDB(Context _context) {
        context = _context;
        dbHelper = new SQLiteDB(context, DATABASE_NAME, null, 1);
    }

    // open db
    public ChildDB open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //close db
    void close() {
        db.close();
    }


    void insertEntry(String name, String birth, String gender, byte[] image) {

        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put("NAME", name);
        newValues.put("BIRTH", birth);
        newValues.put("GENDER", gender);
        newValues.put("IMAGE", image);


        // Insert the row into your table
        db.insert(tableName, null, newValues);
    }


    public void updateImage(byte[] image, String userName) {

        ContentValues updatedValues = new ContentValues();

        updatedValues.put("IMAGE", image);

        String where = "USERNAME = ?";

        db.update("LOGIN", updatedValues, where, new String[]{userName});

    }

    int getChildID(String childName) {
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
        //Cursor cursor = db.query("LOGIN", null, where, new String[]{userName}, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE NAME='" + childName + "' ", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return 0;
        }

        cursor.moveToFirst();
        int childID = cursor.getInt(cursor.getColumnIndex("ID"));
        cursor.close();

        return childID;
    }

    public ArrayList<String> childInfo(int childID) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE ID=" + childID + "", null);
        ArrayList<String> getChildInfo = new ArrayList<String>();
        if (cursor.getCount() < 1) {
            cursor.close();
            return getChildInfo;
        }
        cursor.moveToFirst();

        getChildInfo.add(cursor.getString(1)); //NAME column
        getChildInfo.add(cursor.getString(2)); //BIRTH column
        getChildInfo.add(cursor.getString(3)); //GENDER column

        cursor.close();
        return getChildInfo;

    }

    byte[] gettingImage(int childID){
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE ID=" + childID + "", null);
        cursor.moveToFirst();
        byte[] image_byteArray = cursor.getBlob(cursor.getColumnIndex("IMAGE"));
        cursor.close();
        return image_byteArray;
    }


}

