package com.example.mayoo.myapplication;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mayoo on 12/9/2016.
 */

public class Vaccines {
    private static final String DATABASE_NAME = "login.db";

    private static String tableName = "VACCINES";

    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table " + Vaccines.tableName + "( " + "ID" + " integer primary key autoincrement,"
            + "NAME  text, VAC_DATE text, INFO text );";

    // Variable to hold the database instance
    private SQLiteDatabase db;

    // Context of the application using the database.
    private final Context context;

    private SQLiteDB dbHelper;

    //connect to db
    Vaccines(Context _context) {
        context = _context;
        dbHelper = new SQLiteDB(context, DATABASE_NAME, null, 1);
    }

    // open db
    public Vaccines open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //close db
    void close() {
        db.close();
    }
}
