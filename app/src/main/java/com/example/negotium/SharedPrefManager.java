package com.example.negotium;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
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
        return true;
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
