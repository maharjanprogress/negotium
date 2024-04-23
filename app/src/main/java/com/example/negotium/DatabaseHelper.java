package com.example.negotium;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName= "Signup.db";
    byte[] imageInBytes;
    private Object Context;
    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Signup.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table allusers(userid INTEGER primary key autoincrement, name TEXT, email TEXT , password TEXT)");
        MyDatabase.execSQL("CREATE TABLE product (productid INTEGER PRIMARY KEY autoincrement, product_name TEXT NOT NULL, category TEXT, price TEXT NOT NULL, pic BLOB,description text,producer text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {
        MyDatabase.execSQL("drop table if exists allusers");
        MyDatabase.execSQL("drop table if exists product");
    }

    public Boolean insertData(String name, String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDatabase.insert("allusers", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Boolean checkEmail(String email){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public Boolean checkEmailPassword(String email, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ? and password = ?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public boolean insertDetails(Bitmap pic,String product_name,String producer,String description,String price,String category){

        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream objectByteOutputStream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG,100,objectByteOutputStream);
        imageInBytes=objectByteOutputStream.toByteArray();
        ContentValues values = new ContentValues();

        values.put("product_name",product_name);
        values.put("category",category);
        values.put("price",price);
        values.put("pic",imageInBytes);
        values.put("description",description);
        values.put("producer",producer);
        long id1=db.insert("product",null,values);
        if(id1<=0){
            return false;
        }
        else {
            return true;
        }

    }
    Cursor readAllData(){
        String query = "SELECT * FROM product";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
