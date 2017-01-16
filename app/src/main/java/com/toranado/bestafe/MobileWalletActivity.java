package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class MobileWalletActivity extends AppCompatActivity {

    private static final String TAG=MobileWalletActivity.class.getSimpleName();
    private String strBalance="",strMemebrId="",strPrice="",strProductId="",strWalletBalance="";
    private Toolbar toolbar_wallet;
    private TextView txtTitle,txtAvailablePrice,Price,txtPrice,txtAmountPayablePrice;
    private CheckBox chkWallet;
    private Button btnOrder;
    private ProgressDialog pd;
    private String JsonResponse="",strBV="",strProductName="",strQunty="",strTotal="",strBalanceValues="";
    JSONObject jsonObject;
    JSONArray jsonArray;

    ArrayList<String> listModelName=new ArrayList<>();
    ArrayList<String>listProductId=new ArrayList<>();
    ArrayList<String>listQunty=new ArrayList<>();
    ArrayList<String>listPrice=new ArrayList<>();
    ArrayList<String>listMpn=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_wallet);

        getSupportActionBar().hide();


        Intent intent=getIntent();
//        strMemebrId=intent.getStringExtra("Memberid");
//        Log.d(TAG, "payment member id: "+strMemebrId);
//        String price=intent.getStringExtra("strPrice");
//        Log.d(TAG, "payment price : "+price);
//        String []strSpiltPric=price.split("\\.");
//        strPrice=strSpiltPric[1].replace(" ","");
//        Log.d(TAG, "replace price: "+strPrice);
//        strBV=intent.getStringExtra("strBV");
//        strProductId=intent.getStringExtra("strProductId");
//        Log.d(TAG, "payment product id: "+strProductId);
//        strProductName=intent.getStringExtra("strProductName");
//        strQunty=intent.getStringExtra("strQunty");
//        Log.d(TAG, "product name: "+strProductName);
//        Log.d(TAG, "product qunty: "+strQunty);

        strMemebrId=intent.getStringExtra("Memberid");
        Log.d(TAG, "payment member id: "+strMemebrId);
        listPrice=intent.getStringArrayListExtra("strPrice");
        Log.d(TAG, "payment price : "+listPrice);
        listMpn=intent.getStringArrayListExtra("strBV");
        listProductId=intent.getStringArrayListExtra("strProductId");
        Log.d(TAG, "payment product id: "+listProductId);
        listModelName=intent.getStringArrayListExtra("strProductName");
        listQunty=intent.getStringArrayListExtra("strQunty");
        Log.d(TAG, "product name: "+listModelName);
        Log.d(TAG, "product qunty: "+listQunty);
        strTotal=intent.getStringExtra("strTotal");
        Log.d(TAG, "total price: "+strTotal);

        toolbar_wallet= (Toolbar) findViewById(R.id.toolbar_wallet);
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        txtTitle.setText("Mobile Wallet");

        toolbar_wallet.setNavigationIcon(R.drawable.arrowleft);
        toolbar_wallet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtAmountPayablePrice= (TextView) findViewById(R.id.txtAmountPayablePrice);
        Price= (TextView) findViewById(R.id.Price);
        txtPrice= (TextView) findViewById(R.id.txtPrice);
        txtAmountPayablePrice= (TextView) findViewById(R.id.txtAmountPayablePrice);
        txtAvailablePrice= (TextView) findViewById(R.id.txtAvailablePrice);
        Price=(TextView)findViewById(R.id.Price);

        Price.setText("Price("+listQunty.size()+" item)");


        chkWallet= (CheckBox) findViewById(R.id.chkWallet);
        btnOrder= (Button) findViewById(R.id.btnOrder);

        txtPrice.setText("Rs "+strTotal);
        txtAmountPayablePrice.setText("Rs "+strTotal);

        getWalletBalance();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strBalance!=null && Integer.parseInt(strBalance)> Integer.parseInt(strTotal) && strBalanceValues!=null){

                    jsonObject = null;
                    jsonArray = new JSONArray();

                    for (int i=0; i<listModelName.size(); i++){
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put("ProductName", listModelName.get(i));
                            jsonObject.put("ProductPrice", listPrice.get(i));
                            jsonObject.put("ProductMPN", listMpn.get(i));
                            jsonObject.put("ProductID", listProductId.get(i));
                            jsonObject.put("ProductQty", listQunty.get(i));
                            Log.d(TAG, "data with:" + jsonArray);} catch (Exception e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(jsonObject);
                    }


                    try {
                        JSONObject finalobject = new JSONObject();
                        finalobject.put("mode", "postorder");
                        finalobject.put("memberId", strMemebrId);
                        finalobject.put("wal","1");
                        finalobject.put("amt",strTotal);
                        finalobject.put("CartData", jsonArray);
                        Log.d(TAG, "Cartdata:" + finalobject);
                        if (finalobject.length() > 0) {
                            new sendOrder().execute(String.valueOf(finalobject));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MobileWalletActivity.this, "Insufficient balance for purchases Item!!!", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void getWalletBalance() {
        JSONObject jsonObject=new JSONObject();

        try{
            jsonObject.put("mode","getwal");
            jsonObject.put("memberid",strMemebrId);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (jsonObject.length()>0){
            new getwalletBalance().execute(String.valueOf(jsonObject));
        }
    }

    private class sendOrder extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MobileWalletActivity.this);
            pd.setIndeterminate(false);
            pd.setMessage("Please Wait...");
            pd.setCanceledOnTouchOutside(false);
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

            return JsonResponse;
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
                String strMessagge = jsonObject.getString("message");
                String strResponse = jsonObject.getString("response");
                Toast.makeText(getApplicationContext(), strResponse, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "strResponse data:" + strResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class getwalletBalance extends AsyncTask<String,String,String> {

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
                strWalletBalance = buffer.toString();
                Log.i(TAG, JsonResponse);
                //send to post execute
                return strWalletBalance;
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

            return strWalletBalance;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(strWalletBalance);
                String strStatus = jsonObject.getString("status");
                String strMessage=jsonObject.getString("message");
                JSONArray jsonArray=jsonObject.getJSONArray("response");
                strBalanceValues=jsonArray.getString(0);
                if (strBalanceValues!=null && !strBalanceValues.equals("null")){
                    if (strBalanceValues.contains(".")){
                        String []splitValues=strBalanceValues.split("\\.");
                        strBalance=splitValues[0];
                        Log.d(TAG, "split values: "+strBalance);
                    }else {
                        strBalance=strBalanceValues;

                    }
                    Log.d(TAG, "Balnce json data: "+strBalance);
                    txtAvailablePrice.setText("Available balance :"+ "Rs."+strBalance);
                }else {
                    txtAvailablePrice.setText("Available balance :"+ "Rs."+"0");
                }





            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
