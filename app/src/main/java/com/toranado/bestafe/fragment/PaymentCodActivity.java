package com.toranado.bestafe.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.PaymentActivity;
import com.toranado.bestafe.R;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PaymentCodActivity extends AppCompatActivity {

    private static final String TAG = PaymentCodActivity.class.getSimpleName();
    @InjectView(R.id.txtPriceDetails)
    TextView txtPriceDetails;
    @InjectView(R.id.view)
    View view;
    @InjectView(R.id.Price)
    TextView Price;
    @InjectView(R.id.txtPrice)
    TextView txtPrice;
    @InjectView(R.id.txtDelivary)
    TextView txtDelivary;
    @InjectView(R.id.Delivary)
    TextView Delivary;
    @InjectView(R.id.txtAmountPayable)
    TextView txtAmountPayable;
    @InjectView(R.id.txtAmountPayablePrice)
    TextView txtAmountPayablePrice;
    @InjectView(R.id.btnOrder)
    Button btnOrder;
    @InjectView(R.id.txtQunty)
    TextView txtQunty;
    @InjectView(R.id.txtQuntyValue)
    TextView txtQuntyValue;

    private Toolbar mToolbarCod;
    private TextView txtTitle;
    private String JsonResponse,strPrice, strTotalPrice,strMemberId;

    private  ArrayList<String> listProductId=new ArrayList<>();
    private  ArrayList<String> listProductName=new ArrayList<>();
    private  ArrayList<String> listProductPrice=new ArrayList<>();
    private  ArrayList<String> listProductQuntity=new ArrayList<>();
    private  ArrayList<String> listProductMpn=new ArrayList<>();
    int intValue=0;
    int intQuntity=0;

    private ProgressDialog pd;

    private JSONObject jsonObject;
    private JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cod);
        ButterKnife.inject(this);
        getSupportActionBar().hide();


//        Intent intent=new Intent(PaymentActivity.this, PaymentCodActivity.class);
//        intent.putExtra("strMemberId",strMemberId);
//        intent.putStringArrayListExtra("strProductName",listModelName);
//        intent.putStringArrayListExtra("strPrice", listPrice);
//        intent.putStringArrayListExtra("strQunty", listQunty);
//        intent.putStringArrayListExtra("strProductId",listProductId);
//        intent.putStringArrayListExtra("strMpn",listMpn);
//        startActivity(intent);

        Intent intent=getIntent();
        strMemberId=intent.getStringExtra("strMemberId");
        listProductName=intent.getStringArrayListExtra("strProductName");
        listProductPrice=intent.getStringArrayListExtra("strPrice");
        listProductQuntity=intent.getStringArrayListExtra("strQunty");
        listProductId=intent.getStringArrayListExtra("strProductId");
        listProductMpn=intent.getStringArrayListExtra("strMpn");

        for (int i=0; i<listProductPrice.size();i++){

            intValue +=Integer.parseInt(listProductPrice.get(i));

        }
        Log.d(TAG, "price values: "+intValue);

        for (int i=0;i<listProductQuntity.size(); i++){
            intQuntity +=Integer.parseInt(listProductQuntity.get(i));

        }
        Log.d(TAG, "quntity values: "+intQuntity);


        mToolbarCod = (Toolbar) findViewById(R.id.mToolbarCod);
        txtTitle = (TextView) mToolbarCod.findViewById(R.id.txtTitle);

        txtTitle.setText("Cod Option");
        mToolbarCod.setNavigationIcon(R.drawable.arrowleft);
        mToolbarCod.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtAmountPayablePrice.setText("Rs." +String.valueOf(intValue));
        txtQuntyValue.setText(String.valueOf(intQuntity));
        txtPrice.setText("Rs." +String.valueOf(intValue));

    }

    @OnClick(R.id.btnOrder)
    public void onClick() {

        jsonObject = null;
        jsonArray = new JSONArray();

        for (int i = 0; i < listProductName.size(); i++) {
            jsonObject = new JSONObject();
            try {
                jsonObject.put("ProductName", listProductName.get(i));
                jsonObject.put("ProductPrice", listProductPrice.get(i));
                jsonObject.put("ProductMPN", listProductMpn.get(i));
                jsonObject.put("ProductID", listProductId.get(i));
                jsonObject.put("ProductQty", listProductQuntity.get(i));
                Log.d(TAG, "data with:" + jsonArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }


        try {
            JSONObject finalobject = new JSONObject();
            finalobject.put("mode", "postorder");
            finalobject.put("memberId", strMemberId);
            finalobject.put("wal", "1");
            finalobject.put("amt", String.valueOf(intValue));
            finalobject.put("CartData", jsonArray);
            Log.d(TAG, "Cartdata:" + finalobject);
            if (finalobject.length() > 0) {
                new sendOrder().execute(String.valueOf(finalobject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class sendOrder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PaymentCodActivity.this);
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

}
