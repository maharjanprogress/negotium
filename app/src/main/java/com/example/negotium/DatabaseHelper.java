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
        MyDatabase.execSQL("CREATE TABLE product (productid INTEGER PRIMARY KEY autoincrement, product_name TEXT NOT NULL, cateid INTEGER,subcate TEXT, price TEXT NOT NULL, pic BLOB,description text,producer text,FOREIGN KEY('cateid') REFERENCES 'category'('cateid'))");
        MyDatabase.execSQL("CREATE TABLE category(cateid INTEGER PRIMARY KEY autoincrement,category TEXT NOT NULL unique)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {
        MyDatabase.execSQL("drop table if exists allusers");
        MyDatabase.execSQL("drop table if exists product");
        MyDatabase.execSQL("drop table if exists category");
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
    public boolean insertDetails(Bitmap pic,String product_name,String producer,String description,String price,String category,String id){

        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream objectByteOutputStream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG,100,objectByteOutputStream);
        imageInBytes=objectByteOutputStream.toByteArray();
        ContentValues values = new ContentValues();

        values.put("product_name",product_name);
        values.put("subcate",category);
        values.put("price",price);
        values.put("pic",imageInBytes);
        values.put("description",description);
        values.put("producer",producer);
        values.put("cateid",id);
        long id1=db.insert("product",null,values);
        if(id1<=0){
            return false;
        }
        else {
            return true;
        }

    }
    public boolean insertCategory(String catename){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("category",catename);

        long id1=db.insert("category",null,values);
        if(id1<=0){
            return false;
        }
        else {
            return true;
        }

    }
    public Cursor getcatid(String catename){
        String query = "SELECT cateid FROM category WHERE category LIKE '"+catename+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor lol = db.rawQuery(query, null);
        return lol;
    }
    public Cursor getcatname(String catename){
        String query = "SELECT category FROM category WHERE cateid = "+catename+"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor lol = db.rawQuery(query, null);
        return lol;
    }
    public boolean updatedetails(Bitmap pic,String product_name,String producer,String description,String price,String category,int prodid,String cid){

        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream objectByteOutputStream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG,100,objectByteOutputStream);
        imageInBytes=objectByteOutputStream.toByteArray();
        String prodid1 = String.valueOf(prodid);
        ContentValues values = new ContentValues();

        values.put("product_name",product_name);
        values.put("cateid",cid);
        values.put("subcate",category);
        values.put("price",price);
        values.put("pic",imageInBytes);
        values.put("description",description);
        values.put("producer",producer);

        long id1=db.update("product",values,"productid=?",new String[]{prodid1});
        if(id1<=0){
            return false;
        }
        else {
            return true;
        }

    }
    public  Cursor getcategory(String catno){
        String query = "SELECT subcate FROM product WHERE cateid=" + catno;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }
    public Cursor readAllData(){
        String query = "SELECT * FROM product";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor readsomeData(String search){
        String query = "SELECT * FROM product WHERE product_name LIKE '%"+search+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor readsomecate(String search){
        String query = "SELECT * FROM category WHERE category LIKE '%"+search+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor readallcate(){
        String query = "SELECT * FROM category";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public boolean deleteData(int prodid){

        SQLiteDatabase db = this.getWritableDatabase();
        String prodid1 = String.valueOf(prodid);
        long id1=db.delete("product","productid=?", new String[]{prodid1});
        if(id1<=0){
            return false;
        }
        else {
            return true;
        }

    }
}
