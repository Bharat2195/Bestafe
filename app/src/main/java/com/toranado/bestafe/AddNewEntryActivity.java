package com.toranado.bestafe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddNewEntryActivity extends AppCompatActivity {


    private static final String TAG = AddNewEntryActivity.class.getSimpleName();
    @InjectView(R.id.txtPanMsg)
    TextView txtPanMsg;
    @InjectView(R.id.rl_no_pancard)
    RelativeLayout rlNoPancard;
    @InjectView(R.id.txtBalance)
    TextView txtBalance;
    @InjectView(R.id.txtWithdrawalAmountTitle)
    TextView txtWithdrawalAmountTitle;
    @InjectView(R.id.etWithdrawalAmount)
    EditText etWithdrawalAmount;
    @InjectView(R.id.txtWithdrawalAmount)
    TextView txtWithdrawalAmount;
    @InjectView(R.id.txtAppliesTds)
    TextView txtAppliesTds;
    @InjectView(R.id.AppliesTds)
    TextView AppliesTds;
    @InjectView(R.id.txtProcessingFees)
    TextView txtProcessingFees;
    @InjectView(R.id.ProcessingFees)
    TextView ProcessingFees;
    @InjectView(R.id.txtTotalDeduction)
    TextView txtTotalDeduction;
    @InjectView(R.id.TotalDeduction)
    TextView TotalDeduction;
    @InjectView(R.id.txtPayableAmount)
    TextView txtPayableAmount;
    @InjectView(R.id.PayableAmount)
    TextView PayableAmount;
    @InjectView(R.id.txtTransactionPassword)
    TextView txtTransactionPassword;
    @InjectView(R.id.etTransactionPassword)
    EditText etTransactionPassword;
    @InjectView(R.id.txtErrorMessage)
    TextView txtErrorMessage;
    @InjectView(R.id.btnSubmit)
    Button btnSubmit;
    @InjectView(R.id.Balance)
    TextView Balance;
    @InjectView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @InjectView(R.id.mCard_view)
    CardView mCardView;
    @InjectView(R.id.WithdrawalAmount)
    TextView WithdrawalAmount;
    private Toolbar toolbar_add_new_entry;
    private TextView txtTitle;

    private String strJsonResponse = "", strBalance = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_entry);
        ButterKnife.inject(this);

        getSupportActionBar().hide();
        toolbar_add_new_entry = (Toolbar) findViewById(R.id.toolbar_add_new_entry);
        txtTitle = (TextView) toolbar_add_new_entry.findViewById(R.id.txtTitle);

        txtTitle.setText("Fund Withdrawal Request");

        toolbar_add_new_entry.setNavigationIcon(R.drawable.arrowleft);
        toolbar_add_new_entry.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getBalance();

        etWithdrawalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String strValue = s.toString();
                if (etWithdrawalAmount.getEditableText().equals(true) && !strValue.equals("")) {
                    {

                        int values = Integer.parseInt(strValue);
                        int intTDS = values * 5 / 100;
                        Log.d(TAG, "tds value: " + intTDS);
                        AppliesTds.setText("Rs. " + String.valueOf(intTDS));
                        int intProcessingFees = values * 5 / 100;
                        Log.d(TAG, "processingfees: " + intProcessingFees);
                        ProcessingFees.setText("Rs. " + String.valueOf(intProcessingFees));
                        int intTotaldeduction = intTDS + intProcessingFees;
                        Log.d(TAG, "total deduction: " + intTotaldeduction);
                        TotalDeduction.setText("Rs. " + String.valueOf(intTotaldeduction));
                        int intPaybleAmount = values + intTotaldeduction;
                        Log.d(TAG, "paybleamount: " + intPaybleAmount);
                        PayableAmount.setText("Rs. " + String.valueOf(intPaybleAmount));
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getBalance() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "getbalance");
            Log.d(TAG, "member id: " + LoginMainActivity.strId);
            jsonObject.put("memberid", LoginMainActivity.strId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getBalance().execute(String.valueOf(jsonObject));
        }
    }

    @OnClick(R.id.btnSubmit)
    public void onClick() {
    }

    private class getBalance extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String JsonDATA = params[0];

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(Constant.strBaseURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
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
                Log.i(TAG, strJsonResponse);
                //send to post execute
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

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);
                String strMessage = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                strBalance = jsonArray.getString(0);
                Log.d(TAG, "Balnce value: " + strBalance);
                Balance.setText("Rs. " + strBalance);

                if (strBalance.equals("You Don't Have Sufficient Balance")) {
                    etWithdrawalAmount.setEnabled(false);
                } else {
                    etWithdrawalAmount.setEnabled(true);
                    WithdrawalAmount.setText("Rs. " + strBalance);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
