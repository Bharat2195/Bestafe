package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.util.ArrayList;

public class CartLoginActivity extends AppCompatActivity {

    private static final String TAG=CartLoginActivity.class.getSimpleName();
    private String strProductName,strProductId,strQunty,strPrice,strMemberId;

    Toolbar login_toolbar;
    EditText etUserName, etPassword;
    Button btnLogin;
    String strTotal="",strBV="", strUserName = "", strPassword = "", JsonResponse = "", strLogin_id = "",strMessagge ="",strCustomer_ID="";
    TextView txtSignUp,txtTitle;
    ImageView imgSignUp,imgBack,imgCancle;
    private TextView txtRegister;
    private ProgressDialog pd;
    ArrayList<String>listModelName=new ArrayList<>();
    ArrayList<String>listProductId=new ArrayList<>();
    ArrayList<String>listQunty=new ArrayList<>();
    ArrayList<String>listPrice=new ArrayList<>();
    ArrayList<String>listMpn=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_login);

        getSupportActionBar().hide();
        Intent intent=getIntent();
//        strProductName=intent.getStringExtra("strProductName");
//        strProductId=intent.getStringExtra("strProductId");
//        strQunty=intent.getStringExtra("strQunty");
//        strPrice=intent.getStringExtra("strPrice");
//        strBV=intent.getStringExtra("strBV");

        listModelName=intent.getStringArrayListExtra("strProductName");
        listProductId=intent.getStringArrayListExtra("strProductId");
        listQunty=intent.getStringArrayListExtra("strQunty");
        listPrice=intent.getStringArrayListExtra("strPrice");
        listMpn=intent.getStringArrayListExtra("strBV");
        strTotal=intent.getStringExtra("strTotal");
        Log.d(TAG, "total price: "+strTotal);

        txtRegister= (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(CartLoginActivity.this,RegisterActity.class);
                intent.putExtra("name","payment");
                startActivity(intent);

            }
        });

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
            pd=new ProgressDialog(CartLoginActivity.this);
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
                    String strFirstname=jsonObject1.getString("firstname");
                    Log.d(TAG,"Firstname:"+strFirstname);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strMessagge.equals("Success")){
                Toast.makeText(getApplicationContext(),strMessagge,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartLoginActivity.this, PaymentActivity.class);
//                intent.putExtra("strProductName",strProductName);
//                intent.putExtra("strProductId",strProductId);
//                intent.putExtra("strQunty",strQunty);
//                intent.putExtra("strPrice",strPrice);
//                intent.putExtra("strMemberId",strCustomer_ID);
//                intent.putExtra("strBV",strBV);

                intent.putStringArrayListExtra("strProductName",listModelName);
                intent.putStringArrayListExtra("strProductId",listProductId);
                intent.putStringArrayListExtra("strQunty",listQunty);
                intent.putStringArrayListExtra("strPrice",listPrice);
                intent.putExtra("strMemberId",strCustomer_ID);
                intent.putStringArrayListExtra("strBV",listMpn);
                intent.putExtra("strTotal",strTotal);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),strMessagge,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
