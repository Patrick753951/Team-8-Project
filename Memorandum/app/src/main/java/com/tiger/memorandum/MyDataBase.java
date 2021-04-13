package com.tiger.memorandum;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper{
    private final String TAG="MyDataBase";
    public static final String DB_FILENAME="database.db";
    public static final String TABLE_NAME="memorandum";

    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder timetable=new StringBuilder();
        timetable.append("create table ");
        timetable.append(TABLE_NAME);
        timetable.append(" (");
        timetable.append("uid integer primary key autoincrement,");
        timetable.append("time integer,");
        timetable.append("type integer,");
        timetable.append("num integer,");
        timetable.append("cur_num integer,");
        timetable.append("space_time integer,");
        timetable.append("txt text,");
        timetable.append("person text");
        timetable.append(")");
        sqLiteDatabase.execSQL(timetable.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long addRecord(Record record){
        SQLiteDatabase sql=getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("num",record.getNum());
        values.put("cur_num",1);
        values.put("person",record.getPerson());
        values.put("time",record.getTime());
        values.put("space_time",record.getSpaceTime());
        values.put("type",record.getType());
        values.put("txt",record.getText());
        long ret=sql.insert(TABLE_NAME,null,values);
        Log.i(TAG,"addRecord "+ret);
        return ret;
    }
    public  boolean deleteRecord(Record record) {
        SQLiteDatabase sql=getWritableDatabase();
        String[] args = {String.valueOf(record.getId())};
        long ret=sql.delete(TABLE_NAME,"uid=?",args);
        Log.i(TAG,"deleteRecord ret:"+ret);
        return true;
    }
    public boolean updateRecord(long id,int cueNum) {
        ContentValues values = new ContentValues();
        values.put("cur_num", cueNum);
        long index = getWritableDatabase().update(TABLE_NAME, values, "uid=?" , new String[]{"" + id});
        if (index != -1) {
            return true;
        } else {
            return false;
        }
    }



    public ArrayList<Record> getRecord(){
        ArrayList<Record> arrayList=new ArrayList<>();
        SQLiteDatabase sql=getReadableDatabase();
        Cursor cursor=sql.rawQuery("select * from "+TABLE_NAME+" order by time",null);
        if(cursor.moveToFirst()){
            do{
                Record record=new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex("uid")));
                record.setTime(cursor.getInt(cursor.getColumnIndex("time")));
                record.setCurNum(cursor.getInt(cursor.getColumnIndex("cur_num")));
                record.setType(cursor.getInt(cursor.getColumnIndex("type")));
                record.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                record.setSpaceTime(cursor.getInt(cursor.getColumnIndex("space_time")));
                record.setPerson(cursor.getString(cursor.getColumnIndex("person")));
                record.setText(cursor.getString(cursor.getColumnIndex("txt")));
                arrayList.add(record);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return arrayList;
    }

    public Record getRecordById(long id){
        SQLiteDatabase sql=getReadableDatabase();
        Cursor cursor = sql.query(TABLE_NAME,
                null,  "uid=?" ,
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        Record record=new Record();
        record.setId(cursor.getInt(cursor.getColumnIndex("uid")));
        record.setTime(cursor.getInt(cursor.getColumnIndex("time")));
        record.setCurNum(cursor.getInt(cursor.getColumnIndex("cur_num")));
        record.setType(cursor.getInt(cursor.getColumnIndex("type")));
        record.setNum(cursor.getInt(cursor.getColumnIndex("num")));
        record.setSpaceTime(cursor.getInt(cursor.getColumnIndex("space_time")));
        record.setPerson(cursor.getString(cursor.getColumnIndex("person")));
        record.setText(cursor.getString(cursor.getColumnIndex("txt")));
        cursor.close();
        return record;
    }
}
