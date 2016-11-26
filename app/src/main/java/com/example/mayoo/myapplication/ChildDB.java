package com.example.mayoo.myapplication;

/**
 * Created by mayoo on 11/26/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
    public ChildDB(Context _context) {
        context = _context;
        dbHelper = new SQLiteDB(context, DATABASE_NAME, null, 1);
    }

    // open db
    public ChildDB open() throws SQLException {
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


    public void insertEntry(String name, String birth, String gender,byte[] image) {

        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put("NAME", name);
        newValues.put("BIRTH", birth);
        newValues.put("GENDER", gender);
        newValues.put("IMAGE", image);


        // Insert the row into your table
        db.insert(tableName, null, newValues);
    }




    public void updateProducts(String product1, String product2, String product3, String userName) {

        ContentValues updatedValues = new ContentValues();

        // Assign values for each row.
        updatedValues.put("PRODUCT_1", product1);
        updatedValues.put("PRODUCT_2", product2);
        updatedValues.put("PRODUCT_3", product3);

        String where = "USERNAME = ?";

        db.update(tableName, updatedValues, where, new String[]{userName});
    }


    public void updateImage(byte[] image, String userName) {

        ContentValues updatedValues = new ContentValues();

        updatedValues.put("IMAGE", image);

        String where = "USERNAME = ?";

        db.update("LOGIN", updatedValues, where, new String[]{userName});

    }


}

