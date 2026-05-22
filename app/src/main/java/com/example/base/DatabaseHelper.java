package com.example.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="itemDB";
    private static final int DATABSE_VERSION = 1;
    private static final String TABLE_NAME = "items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IS_LIKE = "isLike";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IS_LIKE + " INTEGER )";
        db.execSQL(createTable);

        insertData(db);
    }

    public void insertData(SQLiteDatabase db){

        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
        db.execSQL( " INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME +","+ COLUMN_IS_LIKE + ") VALUES ('item',0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    public List<Item> getAllItems() {
        List<Item> items= new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        if(cursor.moveToFirst()){
            do {
                Item item = new Item(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LIKE)));
                items.add(item);
            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return items;
    }

    public void addItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_IS_LIKE, item.getIsLike());
        db.insert(TABLE_NAME, null, values);
    }
    public void updateItem(Item item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_IS_LIKE, item.getIsLike());
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteItems(List<Integer> ids){
        SQLiteDatabase db = this.getWritableDatabase();
        for(int id : ids){
            db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
    }
    public boolean isNameExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " = ? LIMIT 1",
                new String[]{name}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public int getSize(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME,null);

        int count =0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        return count;
    }
}
