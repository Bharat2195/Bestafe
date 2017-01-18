package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.CashoutInfo;
import com.citrus.sdk.payment.PaymentData;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;
import com.citrus.sdk.ui.activities.BaseActivity;
import com.citrus.sdk.ui.activities.CitrusUIActivity;
import com.citrus.sdk.ui.fragments.ResultFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;
import com.crashlytics.android.Crashlytics;
import com.orhanobut.logger.Logger;
import com.toranado.bestafe.paymet_class.AppEnvironment;
import com.toranado.bestafe.paymet_class.BaseApplication;
import com.toranado.bestafe.paymet_class.CustomDetailsActivity;
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

import io.fabric.sdk.android.Fabric;


public class PaymentActivity extends BaseActivity implements View.OnClickListener  {

    public static final String returnUrlLoadMoney = "https://salty-plateau-1529.herokuapp" +
            ".com/redirectUrlLoadCash.php";
    public static final String TAG = PaymentActivity.class.getSimpleName();
    private static final long MENU_DELAY = 300;
    public static String dummyMobile = "9769507476";
    public static String dummyEmail = "developercitrus@mailinator.com";
    public static String dummyAmount;
    private String strBV, strTotal = "";
    //    private Button logoutBtn;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private Toolbar toolbar_payment;
    private TextView txtTitle;

    public static ArrayList<String> listModelName = new ArrayList<>();
    public static ArrayList<String> listProductId = new ArrayList<>();
    public static ArrayList<String> listQunty = new ArrayList<>();
    public static ArrayList<String> listPrice = new ArrayList<>();
    public static ArrayList<String> listMpn = new ArrayList<>();
    private JSONObject jsonObject;
    private JSONArray jsonArray;

    private String strProductName = "", strProductId = "", strQunty = "", strPrice = "", strMemberId = "", JsonResponse = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());


        Intent intent = getIntent();
        listModelName = intent.getStringArrayListExtra("strProductName");
        Log.d(TAG, "product Name: " + listModelName);
        listProductId = intent.getStringArrayListExtra("strProductId");
        Log.d(TAG, "product id: " + listProductId);
        listQunty = intent.getStringArrayListExtra("strQunty");
        Log.d(TAG, "strQunty: " + listQunty);
        listPrice = intent.getStringArrayListExtra("strPrice");
        Log.d(TAG, "strPrice: " + listPrice);
        strMemberId = intent.getStringExtra("strMemberId");
        Log.d(TAG, "member id: " + strMemberId);
        listMpn = intent.getStringArrayListExtra("strBV");
        Log.d(TAG, "payment bv: " + listMpn);
        strTotal = intent.getStringExtra("strTotal");
        Log.d(TAG, "total price: " + strTotal);

        settings = getSharedPreferences("settings", MODE_PRIVATE);
//        logoutBtn = (Button) findViewById(R.id.logout_button);
        if (settings.getBoolean("is_prod_env", false)) {
            ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
        }
//        else {
//            ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
//        }
        setupCitrusConfigs();

        toolbar_payment = (Toolbar) findViewById(R.id.toolbar_payment);
        txtTitle = (TextView) toolbar_payment.findViewById(R.id.txtTitle);

        txtTitle.setText("Payment");

        toolbar_payment.setNavigationIcon(R.drawable.arrowleft);
        toolbar_payment.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        String[] strSplitPrice=strPrice.split("\\.");
//        dummyAmount=strSplitPrice[1];
//        Log.d(TAG, "payment spil price: "+dummyAmount);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews() {
        try {
            if (CitrusClient.getInstance(this) != null) {
                CitrusClient.getInstance(this).isUserSignedIn(new Callback<Boolean>() {
                    @Override
                    public void success(Boolean aBoolean) {
                        if (aBoolean) {
//                            logoutBtn.setVisibility(View.VISIBLE);
                        } else {
//                            logoutBtn.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void error(CitrusError citrusError) {

                    }
                });
            }
        } catch (Exception e) {
            Logger.e(e, "");
//            logoutBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.env_menu, menu);
        boolean isProdEnv = settings.getBoolean("is_prod_env", false);
        MenuItem sandbox_item = menu.findItem(R.id.env_sandbox);
        MenuItem prod_item = menu.findItem(R.id.env_prod);
        if (isProdEnv) {
            prod_item.setChecked(true);
        } else {
            sandbox_item.setChecked(true);
        }
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.env_prod:
//                setItem(item);
//                new Handler(getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
//                        editor = settings.edit();
//                        editor.putBoolean("is_prod_env", true);
//                        editor.apply();
////                        logoutBtn.setVisibility(View.GONE);
//                        CitrusClient.getInstance(PaymentActivity.this).signOut(new Callback<CitrusResponse>() {
//                            @Override
//                            public void success(CitrusResponse citrusResponse) {
//
//                            }
//
//                            @Override
//                            public void error(CitrusError error) {
//
//                            }
//                        });
//                        CitrusClient.getInstance(PaymentActivity.this).destroy();
//                        setupCitrusConfigs();
//                    }
//                }, MENU_DELAY);
//                return true;
//            case R.id.env_sandbox:
//                setItem(item);
//                new Handler(getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((BaseApplication) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
//                        editor = settings.edit();
//                        editor.putBoolean("is_prod_env", false);
//                        editor.apply();
////                        logoutBtn.setVisibility(View.GONE);
//                        CitrusClient.getInstance(PaymentActivity.this).signOut(new Callback<CitrusResponse>() {
//                            @Override
//                            public void success(CitrusResponse citrusResponse) {
//
//                            }
//
//                            @Override
//                            public void error(CitrusError error) {
//
//                            }
//                        });
//                        CitrusClient.getInstance(PaymentActivity.this).destroy();
//                        setupCitrusConfigs();
//                    }
//                }, MENU_DELAY);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void setItem(MenuItem item) {
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);
    }

    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((BaseApplication) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.PRODUCTION) {
            Toast.makeText(PaymentActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }
        CitrusFlowManager.initCitrusConfig(appEnvironment.getSignUpId(),
                appEnvironment.getSignUpSecret(), appEnvironment.getSignInId(),
                appEnvironment.getSignInSecret(), ContextCompat.getColor(this, R.color.white),
                PaymentActivity.this, appEnvironment.getEnvironment(), appEnvironment.getVanity(), appEnvironment.getBillUrl(),
                returnUrlLoadMoney, true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_payment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == CitrusFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            // You will get data here if transaction flow is started through pay options other than wallet
            TransactionResponse transactionResponse = data.getParcelableExtra(CitrusUIActivity
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            // You will get data here if transaction flow is started through wallet
            ResultModel resultModel = data.getParcelableExtra(ResultFragment.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
                // Decide what to do with this data
                Log.d(TAG, "Transaction response : " + transactionResponse.getJsonResponse());
                jsonObject = null;
                jsonArray = new JSONArray();

                for (int i = 0; i < listModelName.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("ProductName", listModelName.get(i));
                        jsonObject.put("ProductPrice", listPrice.get(i));
                        jsonObject.put("ProductMPN", listMpn.get(i));
                        jsonObject.put("ProductID", listProductId.get(i));
                        jsonObject.put("ProductQty", listQunty.get(i));
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
                    finalobject.put("amt", strTotal);
                    finalobject.put("CartData", jsonArray);
                    Log.d(TAG, "Cartdata:" + finalobject);
                    if (finalobject.length() > 0) {
                        new sendOrder().execute(String.valueOf(finalobject));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultModel != null && resultModel.getTransactionResponse() != null) {
                // Decide what to do with this data
                Log.d(TAG, "Result response : " + resultModel.getTransactionResponse().getTransactionId());
            } else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getRawResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "request code " + requestCode + " resultcode " + resultCode);
//        if (requestCode == CitrusFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
//                null) {
//            // You will get data here if transaction flow is started through pay options other than wallet
//            TransactionResponse transactionResponse = data.getParcelableExtra(CitrusUIActivity
//                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
//            // You will get data here if transaction flow is started through wallet
//            ResultModel resultModel = data.getParcelableExtra(ResultFragment.ARG_RESULT);
//            Log.d(TAG, "resultmodel response: "+resultModel);
//
//            // Check which object is non-null
//            if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
//                // Decide what to do with this data
//                Log.d(TAG, "transaction response" + transactionResponse.getJsonResponse());
//            } else if (resultModel != null && resultModel.getTransactionResponse() != null) {
//                // Decide what to do with this data
//
//                jsonObject = null;
//                jsonArray = new JSONArray();
//
//                for (int i = 0; i < listModelName.size(); i++) {
//                    jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("ProductName", listModelName.get(i));
//                        jsonObject.put("ProductPrice", listPrice.get(i));
//                        jsonObject.put("ProductMPN", listMpn.get(i));
//                        jsonObject.put("ProductID", listProductId.get(i));
//                        jsonObject.put("ProductQty", listQunty.get(i));
//                        Log.d(TAG, "data with:" + jsonArray);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    jsonArray.put(jsonObject);
//                }
//
//
//                try {
//                    JSONObject finalobject = new JSONObject();
//                    finalobject.put("mode", "postorder");
//                    finalobject.put("memberId", strMemberId);
//                    finalobject.put("wal", "1");
//                    finalobject.put("amt", strTotal);
//                    finalobject.put("CartData", jsonArray);
//                    Log.d(TAG, "Cartdata:" + finalobject);
//                    if (finalobject.length() > 0) {
//                        new sendOrder().execute(String.valueOf(finalobject));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Log.d(TAG, "result response" + resultModel.getTransactionResponse().getTransactionId());
//            } else {
//                Log.d(TAG, "Both objects are null!");
//            }
//        }
//
//    }

    private class sendOrder extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PaymentActivity.this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quick_pay:
                CitrusFlowManager.startShoppingFlow(PaymentActivity.this,
                        dummyEmail, dummyMobile, dummyAmount, false);
                break;
            case R.id.custom_button:
                Intent intentCutom = new Intent(PaymentActivity.this, CustomDetailsActivity.class);
                intentCutom.putStringArrayListExtra("strPrice", listPrice);
                startActivity(intentCutom);
//                startActivity(new Intent(PaymentActivity.this, CustomDetailsActivity.class));
                break;
            case R.id.wallet_button:
//                CitrusFlowManager.startWalletFlow(PaymentActivity.this, dummyEmail, dummyMobile);
                Intent intent = new Intent(PaymentActivity.this, MobileWalletActivity.class);
                intent.putExtra("Memberid", strMemberId);
                intent.putStringArrayListExtra("strPrice", listPrice);
                intent.putStringArrayListExtra("strProductId", listProductId);
                intent.putStringArrayListExtra("strProductName", listModelName);
                intent.putStringArrayListExtra("strBV", listMpn);
                intent.putStringArrayListExtra("strQunty", listQunty);
                intent.putExtra("strTotal", strTotal);
                startActivity(intent);
                break;
//            case R.id.pink:
//                CitrusFlowManager.startShoppingFlowStyle(PaymentActivity.this,
//                        dummyEmail, dummyMobile, dummyAmount, R.style.AppTheme_pink, true);
//                Toast.makeText(PaymentActivity.this, "Result Screen will Override", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.blue:
//                CitrusFlowManager.startShoppingFlowStyle(PaymentActivity.this,
//                        dummyEmail, dummyMobile, dummyAmount, R.style.AppTheme_blue, false);
//                break;
//            case R.id.green:
//                CitrusFlowManager.startShoppingFlowStyle(PaymentActivity.this,
//                        dummyEmail, dummyMobile, dummyAmount, R.style
//                                .AppTheme_Green, false);
//                break;
//            case R.id.purple:
//                CitrusFlowManager.startShoppingFlowStyle(PaymentActivity.this,
//                        dummyEmail, dummyMobile, dummyAmount, R.style
//                                .AppTheme_purple, false);
//                break;
//            case R.id.logout_button:
//                CitrusFlowManager.logoutUser(PaymentActivity.this);
////                logoutBtn.setVisibility(View.GONE);
//                break;
        }
    }

    @Override
    public void withdrawMoney(CashoutInfo cashoutInfo) {

    }

    @Override
    public void onWalletTransactionComplete(ResultModel resultModel, boolean getUserDetails) {

    }

    @Override
    public void showAmount(String amount) {

    }

    @Override
    public void showWalletBalance(String amount) {

    }

    @Override
    public void toggleAmountVisibility(int visibility) {

    }
}
