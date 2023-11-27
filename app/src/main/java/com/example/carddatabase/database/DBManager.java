package com.example.carddatabase.database;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String color, String type) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.COLOR, color);
        contentValue.put(DatabaseHelper.TYPE, type);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.COLOR, DatabaseHelper.TYPE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor search(String name, String color, String type) {
        System.out.println("Start of search");
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.COLOR, DatabaseHelper.TYPE };
//        String whereClause = "name ? AND color ? AND type ?";
        String whereClause = "name ";
        System.out.println("Name: -" + name + "-");
        System.out.println(name == "");
        System.out.println("Color: -" + color + "-");
        System.out.println(color.length());
        System.out.println("Type: -" + type + "-");
        System.out.println(type.length());
        if (name.length() > 0) {
            whereClause += "= \"";
            whereClause += name + "\"";
        }else {
            whereClause += " IS NOT NULL";
        }
        whereClause += " AND color ";
        if (color.length() > 0) {
            whereClause += "= \"";
            whereClause += color + "\"";
        }else {
            whereClause += "IS NOT NULL";
        }
        whereClause += " AND type ";
        if (type.length() > 0) {
            whereClause += "= \"";
            whereClause += type + "\"";
        }else {
            whereClause += "IS NOT NULL";
        }

        System.out.println(whereClause);

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, whereClause, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        System.out.println("End of search");
        return cursor;

    }

    public int update(long _id, String name, String color, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.COLOR, color);
        contentValues.put(DatabaseHelper.TYPE, type);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
