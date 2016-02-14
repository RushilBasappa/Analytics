package com.miniproj.rushi.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rushil on 28-Jun-15.
 */

public class DatabaseOperations extends SQLiteOpenHelper {
    String CREATE_TABLE_Line = "CREATE TABLE " + TableInfo.Line_data_Table + "(" +
            TableInfo.Line_KEY_ID + " integer primary key autoincrement," +
            TableInfo.Line_xaxis + " TEXT," +
            TableInfo.Line_yaxis + " TEXT);";

    String CREATE_TABLE_LineCount = "CREATE TABLE " + TableInfo.Line_Count_Table + "(" +
            TableInfo.Line_KEY_ID + " integer primary key autoincrement," +
            TableInfo.Line_ser_name + " TEXT," +
            TableInfo.Line_PointsPerSeries + " INTEGER);";


    String CREATE_TABLE_Bar = "CREATE TABLE " + TableInfo.Bar_data_Table + "(" +
            TableInfo.Bar_KEY_ID + " integer primary key autoincrement," +
            TableInfo.Bar_xaxis + " INTEGER," +
            TableInfo.Bar_yaxis + " INTEGER);";

    String CREATE_TABLE_BarCount = "CREATE TABLE " + TableInfo.Bar_Count_Table + "(" +
            TableInfo.Bar_KEY_ID + " integer primary key autoincrement," +
            TableInfo.Bar_ser_name + " TEXT," +
            TableInfo.Bar_PointsPerSeries + " INTEGER);";


    String CREATE_TABLE_PIE = "CREATE TABLE " + TableInfo.Pie_table + "(" +
            TableInfo.Pie_ser_name + " TEXT," +
            TableInfo.Pie_ser_value + " INTEGER);";

    public DatabaseOperations(Context context) {

        super(context, TableInfo.Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_Line);
        db.execSQL(CREATE_TABLE_LineCount);
        db.execSQL(CREATE_TABLE_Bar);
        db.execSQL(CREATE_TABLE_BarCount);
        db.execSQL(CREATE_TABLE_PIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putLinePoints(DatabaseOperations db, String xdata, String ydata) {
        SQLiteDatabase SQ = db.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.Line_xaxis, xdata);
        CV.put(TableInfo.Line_yaxis, ydata);
        SQ.insert(TableInfo.Line_data_Table, null, CV);
    }
    public void putLineCount(DatabaseOperations db, String SerName, Integer count) {
        SQLiteDatabase SQ = db.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.Line_ser_name, SerName);
        CV.put(TableInfo.Line_PointsPerSeries, count);
        SQ.insert(TableInfo.Line_Count_Table, null, CV);
    }
    public Cursor getLinePoints(DatabaseOperations db) {
        SQLiteDatabase SQ = db.getReadableDatabase();
        String[] Columns = {TableInfo.Line_xaxis, TableInfo.Line_yaxis};
        Cursor CR = SQ.query(TableInfo.Line_data_Table, Columns, null, null, null, null, null);
        return CR;
    }
    public Cursor getLineCount(DatabaseOperations db) {
        SQLiteDatabase SQ = db.getReadableDatabase();
        String[] Columns = {TableInfo.Line_KEY_ID, TableInfo.Line_ser_name, TableInfo.Line_PointsPerSeries};
        Cursor CR = SQ.query(TableInfo.Line_Count_Table, Columns, null, null, null, null, null);
        return CR;
    }


    public void putBarPoints(DatabaseOperations db, double xdata, double ydata) {
        SQLiteDatabase SQ = db.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.Bar_xaxis, xdata);
        CV.put(TableInfo.Bar_yaxis, ydata);
        SQ.insert(TableInfo.Bar_data_Table, null, CV);
    }
    public void putBarCount(DatabaseOperations db, String SerName, Integer count) {
        SQLiteDatabase SQ = db.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.Bar_ser_name, SerName);
        CV.put(TableInfo.Bar_PointsPerSeries, count);
        SQ.insert(TableInfo.Bar_Count_Table, null, CV);
    }
    public Cursor getBarPoints(DatabaseOperations db) {
        SQLiteDatabase SQ = db.getReadableDatabase();
        String[] Columns = {TableInfo.Bar_xaxis, TableInfo.Bar_yaxis};
        Cursor CR = SQ.query(TableInfo.Bar_data_Table, Columns, null, null, null, null, null);
        return CR;
    }
    public Cursor getBarCount(DatabaseOperations db) {
        SQLiteDatabase SQ = db.getReadableDatabase();
        String[] Columns = {TableInfo.Bar_KEY_ID, TableInfo.Bar_ser_name, TableInfo.Bar_PointsPerSeries};
        Cursor CR = SQ.query(TableInfo.Bar_Count_Table, Columns, null, null, null, null, null);
        return CR;
    }


    public void putPiePoints(DatabaseOperations db, String SerName, Integer count) {
        SQLiteDatabase SQ = db.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put(TableInfo.Pie_ser_name, SerName);
        CV.put(TableInfo.Pie_ser_value, count);
        SQ.insert(TableInfo.Pie_table, null, CV);
    }
    public Cursor getPiePoints(DatabaseOperations db) {
        SQLiteDatabase SQ = db.getReadableDatabase();
        String[] Columns = {TableInfo.Pie_ser_name, TableInfo.Pie_ser_value};
        Cursor CR = SQ.query(TableInfo.Pie_table, Columns, null, null, null, null, null);
        return CR;
    }

}
