package com.example.mayoo.myapplication;

/**
 * Created by mayoo on 11/15/2016.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class Helper {

    private static final String DATABASE_NAME = "login.db";

    private static String tableName = "VAC";

    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + Helper.tableName + "( " + "ID" + " integer primary key autoincrement,"
            + "USERNAME  text, PASSWORD text," +
            " CHILD_1 text, CHILD_2 text, CHILD_3 text);";

    // Variable to hold the database instance
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    private SQLiteDB dbHelper;

    //connect to db
    public Helper(Context _context) {
        context = _context;
        dbHelper = new SQLiteDB(context, DATABASE_NAME, null, 1);
    }

    // open db
    public Helper open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //close db
    public void close() {
        db.close();
    }

    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }


    public void insertEntry(String userName, String password, String child1, String child2, String child3) {

        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put("USERNAME", userName);
        newValues.put("PASSWORD", password);

        newValues.put("CHILD_1", child1);
        newValues.put("CHILD_2", child2);
        newValues.put("CHILD_3", child3);


        // Insert the row into your table
        db.insert(tableName, null, newValues);
    }


    public String getSingleEntry(String userName) {
        //Cursor cursor = db.query("LOGIN", null, where, new String[]{userName}, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE USERNAME='" + userName + "' ", null);
        if (cursor.getCount() < 1) // UserName Not Exist
        {
            cursor.close();
            return "NOT EXIST";
        }

        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();

        return password;
    }


    public void updateChild(String child1, String child2, String child3, String userName) {

        ContentValues updatedValues = new ContentValues();

        // Assign values for each row.
        updatedValues.put("CHILD_1", child1);
        updatedValues.put("CHILD_2", child2);
        updatedValues.put("CHILD_3", child3);

        String where = "USERNAME = ?";

        db.update(tableName, updatedValues, where, new String[]{userName});
    }


    boolean userNameChecking(String userName) {
        String where = "USERNAME = ?";
        Cursor cursor = db.query(tableName, null, where, new String[]{userName}, null, null, null);
        if (cursor.getCount() > 0) // UserName Exists
        {
            cursor.close();
            return false;
        }
        return true;
    }

    public byte[] getImage(String userName) {
        // String s = Arrays.toString(b);
        String where = "USERNAME = ?";
        Cursor c = db.query("LOGIN", null, where, new String[]{userName}, null, null, null);
        c.moveToFirst();
        byte[] bb = c.getBlob(c.getColumnIndex("IMAGE"));
        c.close();
        return bb;
    }


    public void updateImage(byte[] image, String userName) {

        ContentValues updatedValues = new ContentValues();

        updatedValues.put("IMAGE", image);

        String where = "USERNAME = ?";

        db.update("LOGIN", updatedValues, where, new String[]{userName});

    }

    public ArrayList<Integer> getChildID(String userName) {
        //Cursor cursor = db.query("LOGIN", null, where, new String[]{userName}, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE USERNAME='" + userName + "' ", null);

        cursor.moveToFirst();

        String childID1 = cursor.getString(cursor.getColumnIndex("CHILD_1"));
        String childID2 = cursor.getString(cursor.getColumnIndex("CHILD_2"));
        String childID3 = cursor.getString(cursor.getColumnIndex("CHILD_3"));

        ArrayList<Integer> childIDs = new ArrayList<Integer>();
        if (!childID1.equals("-")) {
            cursor.close();
            childIDs.add(Integer.parseInt(childID1));
        } else if (!childID2.equals("-")) {
            cursor.close();
            childIDs.add(Integer.parseInt(childID2));
        } else if (!childID3.equals("-")) {
            cursor.close();
            childIDs.add(Integer.parseInt(childID3));
        } else {
            cursor.close();
            return childIDs;
        }

        return childIDs;


    }


}
