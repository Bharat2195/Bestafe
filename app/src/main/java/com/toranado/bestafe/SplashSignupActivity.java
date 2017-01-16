package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.toranado.bestafe.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SplashSignupActivity extends AppCompatActivity {

    ArrayList<String> listDataName = new ArrayList<String>();
    ArrayList<String> listCategory_id = new ArrayList<String>();
    ArrayList<String> listProductCategory_id = new ArrayList<String>();
    public static final String PREFS_NAME="LoginPrefes";
    ArrayList<String> listProduct_id = new ArrayList<String>();
    String strUserName = "", strPassword = "", strProductResponse = "", JsonResponse = "", strLogin_id = "", strGetDetails = "", strProductId = "";
    private static final String TAG = SplashSignupActivity.class.getSimpleName();

    private TextView txtSkip,txtNewCustomer,txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();

//        txtSkip=(TextView)findViewById(R.id.txtSkip);
        txtNewCustomer=(TextView)findViewById(R.id.txtNewCustomer);
        txtLogin=(TextView)findViewById(R.id.txtLogin);


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin=new Intent(SplashSignupActivity.this,LoginActivity.class);
                startActivity(intentLogin);
            }
        });
        txtNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister=new Intent(SplashSignupActivity.this,FirstShowCategoryActivity.class);
                startActivity(intentRegister);
            }
        });

    }


    //    private class postGetDetail extends AsyncTask<String, String, String> {
//
//
//        @Override
//        protected String doInBackground(String... params) {
//            String JsonDATA = params[0];
//
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            try {
//                URL url = new URL(Constant.strBaseURL);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setDoOutput(true);
//                // is output buffer writter
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("Accept", "application/json");
////set headers and method
//                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
//                writer.write(JsonDATA);
//// json data
//                writer.close();
//                InputStream inputStream = urlConnection.getInputStream();
////input stream
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    return null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String inputLine;
//                while ((inputLine = reader.readLine()) != null)
//                    buffer.append(inputLine + "\n");
//                if (buffer.length() == 0) {
//                    // Stream was empty. No point in parsing.
//                    return null;
//                }
//                strGetDetails = buffer.toString();
////response data
//                Log.i(TAG, strGetDetails);
//                //send to post execute
//                return strGetDetails;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(TAG, "Error closing stream", e);
//                    }
//                }
//            }
//            return strGetDetails;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                JSONObject jsonObject = new JSONObject(strGetDetails);
//                String strStatus = jsonObject.getString("status");
//                String strMessagge = jsonObject.getString("message");
//                JSONArray jsonArray = jsonObject.getJSONArray("response");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String strCategory_id = object.getString("category_id");
//                    String strName = object.getString("name");
//                    if (strName.contains("&amp;")) {
//                        strName = strName.replace("&amp;", "&");
//                    }
//                    listDataName.add(strName);
//                    listCategory_id.add(strCategory_id);
//                    Log.d(TAG, "listName:" + listDataName);
//                }
////                listDataName.add("Login");
//
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return;

    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        this.finish();
//        System.exit(0);
//
////        SharedPreferences setting = getSharedPreferences(PREFS_NAME, 0);
////        if (setting.getString("logged", "").toString().equals("logged")) {
////            Intent intent = new Intent(SplashSignupActivity.this, SplashSignupActivity.class);
////            startActivity(intent);
////        } else {
////            return;
////        }
//
//    }



}
