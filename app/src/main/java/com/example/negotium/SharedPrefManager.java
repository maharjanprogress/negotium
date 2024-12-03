package com.example.negotium;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SharedPrefManager {
    Bitmap please;
    DatabaseHelper db;
    private static SharedPrefManager instance;
//    private ImageLoader imageLoader;
    private static Context ctx;
    private static final String SHARED_PREF_NAME= "mysharedpref12";
    private static final String KEY_USERNAME= "username";
    private static final String KEY_USER_EMAIL= "useremail";
    private static final String KEY_USER_ID= "userid";

    private SharedPrefManager(Context context) {
        ctx = context;

//        imageLoader = new ImageLoader(requestQueue,
//                new ImageLoader.ImageCache() {
//                    private final LruCache<String, Bitmap>
//                            cache = new LruCache<String, Bitmap>(20);
//
//                    @Override
//                    public Bitmap getBitmap(String url) {
//                        return cache.get(url);
//                    }
//
//                    @Override
//                    public void putBitmap(String url, Bitmap bitmap) {
//                        cache.put(url, bitmap);
//                    }
//                });
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public boolean userlogin(int id, String username,String email){

        SharedPreferences sharedpref= ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putInt(KEY_USER_ID,id);
        editor.putString(KEY_USER_EMAIL,email);
        editor.putString(KEY_USERNAME,username);
        editor.apply();
        db = new DatabaseHelper(ctx);
        lol();
        return true;
    }

    private void lol() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Constants.URL_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject obj = new JSONObject(s);
                            JSONArray jsonArray = (JSONArray)obj.getJSONArray("productid");
                            JSONArray jsonArray1 = (JSONArray)obj.getJSONArray("product_name");
                            JSONArray jsonArray2 = (JSONArray)obj.getJSONArray("category");
                            JSONArray jsonArray3 = (JSONArray)obj.getJSONArray("subcate");
                            JSONArray jsonArray4 = (JSONArray)obj.getJSONArray("price");
                            JSONArray jsonArray5 = (JSONArray)obj.getJSONArray("pic");
                            JSONArray jsonArray6 = (JSONArray)obj.getJSONArray("description");
                            JSONArray jsonArray7 = (JSONArray)obj.getJSONArray("producer");
                            if(jsonArray != null){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    please = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.negotiumlogo);
                                    db.insertDetails(please,jsonArray1.getString(i),jsonArray7.getString(i),jsonArray6.getString(i),jsonArray4.getString(i),jsonArray2.getString(i),jsonArray3.getString(i));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ctx, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
//                params.put("username",username);
//                params.put("password",password);
                return params;
            }
        };
        RequestHandler.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public boolean isLoggedIn(){

        SharedPreferences sharedpref= ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedpref.getString(KEY_USERNAME,null)!=null){
            return true;
        }
        else return false;
    }

    public boolean logout(){

        SharedPreferences sharedpref= ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.clear();
        editor.apply();
        return true;
    }
    public String getUsername(){
        SharedPreferences sharedpref= ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedpref.getString(KEY_USERNAME,null);
    }
    public Integer getUserid(){
        SharedPreferences sharedpref= ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedpref.getInt(KEY_USER_ID,1);
    }
    public String getUserEmail(){
        SharedPreferences sharedpref= ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedpref.getString(KEY_USER_EMAIL,null);
    }


//    public ImageLoader getImageLoader() {
//        return imageLoader;
//    }
}
