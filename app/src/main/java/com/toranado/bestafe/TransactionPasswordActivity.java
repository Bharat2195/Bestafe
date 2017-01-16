package com.toranado.bestafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.utils.Constant;

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

public class TransactionPasswordActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefes";
    private static final String TAG = TransactionPasswordActivity.class.getSimpleName();
    Toolbar toolbar_transaction_password;
    String strOldPassword = "", strNewPassword = "", strConfirmPassword = "", strMemberid = "", JsonResponse = "", strMessagge = "", strChangePasswordResponse = "";
    TextView txtSignIn,txtTitle,txtErrorOldPassword,txtErrorConPassword;
    ImageView imgSignIn;
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnUpadateNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        toolbar_transaction_password = (Toolbar) findViewById(R.id.toolbar_transaction_password);
        txtTitle=(TextView)toolbar_transaction_password.findViewById(R.id.txtTitle);
        txtTitle.setText("Transaction Password");

        txtErrorConPassword= (TextView) findViewById(R.id.txtErrorConPassword);
        txtErrorOldPassword= (TextView) findViewById(R.id.txtErrorOldPassword);

        toolbar_transaction_password.setNavigationIcon(R.drawable.arrowleft);
        toolbar_transaction_password.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        txtSignIn = (TextView) toolbar_transaction_password.findViewById(R.id.txtSignIn);
//        imgSignIn = (SetImage) toolbar_transaction_password.findViewById(R.id.imgSignIn);
//
//
//        txtSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
//                if (preferences.getString("logged", "").toString().equals("logged")) {
//                    Intent intent = new Intent(TransactionPasswordActivity.this, LoginMainActivity.class);
//                    intent.putExtra("id", preferences.getString("id", "").toString());
//                    startActivity(intent);
//                } else {
//                    Intent intentLogin = new Intent(TransactionPasswordActivity.this, LoginActivity.class);
//                    startActivity(intentLogin);
//                }
//
//            }
//        });
//
//        imgSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
//                if (preferences.getString("logged", "").toString().equals("logged")) {
//                    Intent intent = new Intent(TransactionPasswordActivity.this, LoginMainActivity.class);
//                    intent.putExtra("id", preferences.getString("id", "").toString());
//                    startActivity(intent);
//                } else {
//                    Intent intentImgLogin = new Intent(TransactionPasswordActivity.this, LoginActivity.class);
//                    startActivity(intentImgLogin);
//                }
//
//            }
//        });
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        btnUpadateNow = (Button) findViewById(R.id.btnUpadateNow);

        Intent intent = getIntent();
        strMemberid = intent.getStringExtra("memberid");


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar_transaction_password.setNavigationIcon(R.drawable.left_arrow1);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        toolbar_transaction_password.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        etOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText;

                if (!hasFocus) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        editText = (EditText) v;
                        String oldPassword = editText.getText().toString();
                        Log.d(TAG, "MemberId:" + oldPassword);
                        jsonObject.put("mode", "checktranspassword");
                        jsonObject.put("memberid", strMemberid);
                        jsonObject.put("password", oldPassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonObject.length() > 0) {
                        new checkPassword().execute(String.valueOf(jsonObject));
                    }
                }
            }
        });


        btnUpadateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPassword = etOldPassword.getText().toString();
                strNewPassword = etNewPassword.getText().toString();
                strConfirmPassword = etConfirmPassword.getText().toString();

                if (StringUtils.isBlank(strOldPassword)) {
                    Toast.makeText(getApplicationContext(), "Please Enter OldPassword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(strNewPassword)) {
                    Toast.makeText(getApplicationContext(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtErrorOldPassword.getVisibility()==View.VISIBLE){
                    Toast.makeText(TransactionPasswordActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(strConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Please Enter ConfirmPassword", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!strConfirmPassword.equals(strNewPassword)) {
                    Toast.makeText(getApplicationContext(), "Both Password don't match", Toast.LENGTH_SHORT).show();
                } else if ((strConfirmPassword.equals(strNewPassword))) {

                    if (strMessagge.equals("Success")) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("mode", "changetranspassword");
                            jsonObject.put("memberid", strMemberid);
                            jsonObject.put("password", strNewPassword);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (jsonObject.length() > 0) {
                            new Postdata().execute(String.valueOf(jsonObject));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Correct Old Password", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });
    }

    private class checkPassword extends AsyncTask<String, String, String> {
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
            try {
                JSONObject jsonObject = new JSONObject(JsonResponse);
                String strStatus = jsonObject.getString("status");
                strMessagge = jsonObject.getString("message");

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strMessagge.equals("Success")) {
                Toast.makeText(getApplicationContext(), "Old Password Correct", Toast.LENGTH_SHORT).show();
                txtErrorOldPassword.setVisibility(View.GONE);
            } else {
                txtErrorOldPassword.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Old Password Incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Postdata extends AsyncTask<String, String, String> {
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
                strChangePasswordResponse = buffer.toString();
                Log.i(TAG, strChangePasswordResponse);
                //send to post execute
                return strChangePasswordResponse;
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
            try {
                JSONObject jsonObject = new JSONObject(strChangePasswordResponse);
                String strStatus = jsonObject.getString("status");
                strChangePasswordResponse = jsonObject.getString("message");

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strChangePasswordResponse.equals("Success")) {
                Toast.makeText(getApplicationContext(), strChangePasswordResponse, Toast.LENGTH_SHORT).show();
                etOldPassword.getText().clear();
                etNewPassword.getText().clear();
                etConfirmPassword.getText().clear();
            } else {
                Toast.makeText(getApplicationContext(), strChangePasswordResponse, Toast.LENGTH_SHORT).show();
            }
        }
    }


}
