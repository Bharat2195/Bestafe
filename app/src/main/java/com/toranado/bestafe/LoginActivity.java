package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    Toolbar login_toolbar;
    EditText etUserName, etPassword;
    Button btnLogin;
    String strUserName = "", strPassword = "", JsonResponse = "", strLogin_id = "",strMessagge ="",strCustomer_ID="";
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String PREFS_NAME="LoginPrefes";
    TextView txtSignUp,txtTitle;
    ImageView imgSignUp,imgBack,imgCancle;
    private TextView txtRegister;
    private Toolbar toolbar_login;
    private String strName="",strFirstname="";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        SharedPreferences preferences=getSharedPreferences(PREFS_NAME,0);
        if (preferences.getString("logged","").toString().equals("logged")){
            Intent intent=new Intent(LoginActivity.this,LoginMainActivity.class);
            intent.putExtra("id",preferences.getString("id","").toString());
            intent.putExtra("strFirstname",preferences.getString("strFirstname","").toString());
            startActivity(intent);
        }

//        toolbar_login = (Toolbar) findViewById(R.id.toolbar_login);
//        txtTitle= (TextView) toolbar_login.findViewById(R.id.txtTitle);
//        txtTitle.setText("LOGIN");
//
//        toolbar_login.setNavigationIcon(R.drawable.arrowleft);
//        toolbar_login.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        txtRegister= (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(LoginActivity.this,RegisterActity.class);
                intent.putExtra("name","noPayment");
                startActivity(intent);

            }
        });

      /*  txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoign=new Intent(LoginActivity.this,RegisterActity.class);
                startActivity(intentLoign);
            }
        });

        imgSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentImgLogin=new Intent(LoginActivity.this,RegisterActity.class);
                startActivity(intentImgLogin);
            }
        });*/
//      setSupportActionBar(login_toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        login_toolbar.setNavigationIcon(R.drawable.back);
//        getSupportActionBar().setHomeButtonEnabled(true);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                strUserName = etUserName.getText().toString();
                strPassword = etPassword.getText().toString();

                if (StringUtils.isBlank(strUserName)) {
                    Toast.makeText(getApplicationContext(), "Please Enter the UserName", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(strPassword)) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
                }

                JSONObject post_product = new JSONObject();

                try {

                    post_product.put("mode", "login");
                    post_product.put("memberid", strUserName);
                    post_product.put("password", strPassword);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (post_product.length() > 0) {
                    new getProductData().execute(String.valueOf(post_product));

                }
            }
        });
    }


    private class getProductData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCanceledOnTouchOutside(false);
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

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
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
//input stream
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
                JsonResponse = buffer.toString();
                Log.i(TAG, JsonResponse);
                //send to post execute
                return JsonResponse;
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing()) {
                pd.dismiss();

            }
            try {
                JSONObject jsonObject = new JSONObject(JsonResponse);
                String strStatus = jsonObject.getString("status");
                strMessagge = jsonObject.getString("message");
                JSONArray array=jsonObject.getJSONArray("response");
                for (int i=0;i<array.length(); i++){
                    JSONObject jsonObject1=array.getJSONObject(i);
                    strCustomer_ID=jsonObject1.getString("memberid");
                    Log.d(TAG,"customerID:"+strCustomer_ID);
                    strFirstname=jsonObject1.getString("firstname");
                    Log.d(TAG,"Firstname:"+strFirstname);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strMessagge.equals("Success")){
                Toast.makeText(getApplicationContext(),strMessagge,Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("logged","logged");
                editor.putString("id",strCustomer_ID);
                editor.putString("strFirstname",strFirstname);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, LoginMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("id",strCustomer_ID);
                intent.putExtra("strFirstname",strFirstname);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),strMessagge,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivity.this,SplashSignupActivity.class);
        startActivity(intent);
    }
}

