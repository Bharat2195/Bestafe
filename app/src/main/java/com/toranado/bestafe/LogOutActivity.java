package com.toranado.bestafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogOutActivity extends AppCompatActivity {


    public static final String PREFS_NAME = "LoginPrefes";
    public static final String CART_PREFS_NAME = "CartLoginPrefes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);


        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        SharedPreferences sharedPreferences1 = getSharedPreferences(CART_PREFS_NAME, 0);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.commit();


        Intent intent = new Intent(LogOutActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
