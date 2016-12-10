package com.example.mayoo.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

class SQLiteDB extends SQLiteOpenHelper
{
    SQLiteDB(Context context, String name, CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }


    // Called when no database exists in disk and the helper class needs to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db)
    {
        _db.execSQL(Helper.DATABASE_CREATE);
        _db.execSQL(ChildDB.DATABASE_CREATE);
        _db.execSQL(Vaccines.DATABASE_CREATE);


    }

    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
    {

        // Drop the old table and create a new one.
        _db.execSQL("DROP TABLE VAC IF EXISTS " + "TEMPLATE");
        _db.execSQL("DROP TABLE CHILD IF EXISTS " + "TEMPLATE");
        _db.execSQL("DROP TABLE VACCINES IF EXISTS " + "TEMPLATE");


        // Create a new one.
        onCreate(_db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}

