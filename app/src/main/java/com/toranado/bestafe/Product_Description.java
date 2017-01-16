package com.toranado.bestafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import uk.co.senab.photoview.PhotoViewAttacher;

public class Product_Description extends AppCompatActivity {
    String strProductId = "", strJsonResponse = "", strImagePath = "", strImageDes = "", strDesc = "";
    ImageView imgProduct;
    TextView txtDesc;
    Toolbar toolbar_product_description;
    PhotoViewAttacher mAttacher;
    private static final String TAG = Product_Description.class.getSimpleName();
    TextView txtSignIn;
    ImageView imgSignIn;
    public static final String PREFS_NAME = "LoginPrefes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__description);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        toolbar_product_description = (Toolbar) findViewById(R.id.toolbar_product_description);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
//        txtSignIn = (TextView) toolbar_product_description.findViewById(R.id.txtSignIn);
//        imgSignIn = (SetImage) toolbar_product_description.findViewById(R.id.imgSignIn);
        Intent intent = getIntent();
        strProductId = intent.getStringExtra("pid");
        strImagePath = intent.getStringExtra("imagePath");
        Glide.with(this).load(strImagePath).into(imgProduct);
        mAttacher = new PhotoViewAttacher(imgProduct);
//        txtSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
//                if (preferences.getString("logged", "").toString().equals("logged")) {
//                    Intent intent = new Intent(Product_Description.this, LoginMainActivity.class);
//                    intent.putExtra("id", preferences.getString("id", "").toString());
//                    startActivity(intent);
//                } else {
//                    Intent intentLogin = new Intent(Product_Description.this, LoginActivity.class);
//                    startActivity(intentLogin);
//                }
//            }
//        });
//        imgSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
//                if (preferences.getString("logged", "").toString().equals("logged")) {
//                    Intent intent = new Intent(Product_Description.this, LoginMainActivity.class);
//                    intent.putExtra("id", preferences.getString("id", "").toString());
//                    startActivity(intent);
//                } else {
//                    Intent intentImgLogin = new Intent(Product_Description.this, LoginActivity.class);
//                    startActivity(intentImgLogin);
//                }
//
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar_product_description.setNavigationIcon(R.drawable.left_arrow1);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar_product_description.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "getproduct_desc");
            jsonObject.put("pid", strProductId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonObject.length() > 0) {
            if (imgProduct != null) {
                new JsonData().execute(String.valueOf(jsonObject));
            }

        }
    }

    private class JsonData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String JsonDATA = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(Constant.strBaseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    return null;
                }
                strJsonResponse = buffer.toString();
                return strJsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            Log.d(TAG, "response data:" + strJsonResponse);
            return strJsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);
                Log.d(TAG, "description:" + jsonObject);
                String strMessage = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    strDesc = object.getString("description");
                    Spanned sp = Html.fromHtml(strDesc);
                    txtDesc.setText(sp);
                    Log.d(TAG, "description data :" + txtDesc.getText().toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
