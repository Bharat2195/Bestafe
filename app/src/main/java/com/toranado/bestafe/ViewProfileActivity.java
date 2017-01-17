package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.adpter.Profile;
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
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    String strProductResponse = "", strLoginId = "", strUpdateResponse = "";
    private List<Profile> listProfile = new ArrayList<>();
    //    TextView txtHomeMoNo, txtMemberName, FirstName, txtLastName, txtEmail, txtPassword, txtTransactionPwd, txtDob, txtHomeAddress, txtState,
//            txtCity, txtPincode, txtBankname, txtBranchName, txtBankAccountNo, txtBankIFSCCode;
    private static final String TAG = ViewProfileActivity.class.getSimpleName();
    ProgressDialog pd;
    Profile profile;
    private TextView txtTitle;
    private Toolbar toolbar_view_profile;
    private Button btnUpadateNow;
    private boolean update;
    private int id;
    public static final String PREFS_NAME="ProfilePrefes";
    private EditText etNomineeFullName, etRelationshipwithNominee, etSponsorID, etPlacementId, etFirstName, etLastName, etDob, etPanNo, etEmail, etMobile, etAddress, etCity, etPincode, etState,
            etBankName, etAccountNo, etIFSCCode, etBranchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        toolbar_view_profile = (Toolbar) findViewById(R.id.toolbar_view_profile);
        txtTitle = (TextView) toolbar_view_profile.findViewById(R.id.txtTitle);
        txtTitle.setText("Edit Profile");
        toolbar_view_profile.setNavigationIcon(R.drawable.arrowleft);
        toolbar_view_profile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnUpadateNow = (Button) findViewById(R.id.btnUpadateNow);
        etSponsorID = (EditText) findViewById(R.id.etSponsorID);
        etPlacementId = (EditText) findViewById(R.id.etPlacementId);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etDob = (EditText) findViewById(R.id.etDob);
        etPanNo = (EditText) findViewById(R.id.etPanNo);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etCity = (EditText) findViewById(R.id.etCity);
        etPincode = (EditText) findViewById(R.id.etPincode);
        etState = (EditText) findViewById(R.id.etState);
        etBankName = (EditText) findViewById(R.id.etBankName);
        etAccountNo = (EditText) findViewById(R.id.etAccountNo);
        etIFSCCode = (EditText) findViewById(R.id.etIFSCCode);
        etBranchName = (EditText) findViewById(R.id.etBranchName);
        etRelationshipwithNominee = (EditText) findViewById(R.id.etRelationshipwithNominee);
        etNomineeFullName = (EditText) findViewById(R.id.etNomineeFullName);

        SharedPreferences preferences=getSharedPreferences(PREFS_NAME,0);
        if (preferences.getString("id","").toString().equals("1")){

//            etEmail.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
//            etMobile.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
//            etAddress.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
//            etCity.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
//            etPincode.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
//            etState.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));

            if (etNomineeFullName.getText().toString()!=null){
                etNomineeFullName.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etNomineeFullName.setEnabled(false);
            }

            if (etRelationshipwithNominee.getText().toString()!=null){
                etRelationshipwithNominee.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etRelationshipwithNominee.setEnabled(false);
            }

            if (etBankName.getText().toString()!=null){
                etBankName.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etBankName.setEnabled(false);
            }

            if (etAccountNo.getText().toString()!=null){
                etAccountNo.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etAccountNo.setEnabled(false);
            }

            if (etIFSCCode.getText().toString()!=null){
                etIFSCCode.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etIFSCCode.setEnabled(false);
            }

            if (etBranchName.getText().toString()!=null){
                etBranchName.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etBranchName.setEnabled(false);
            }

            if (etDob.getText().toString()!=null){
                etDob.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etDob.setEnabled(false);
            }

            if (etPanNo.getText().toString()!=null){
                etPanNo.setBackground(getResources().getDrawable(R.drawable.select_fill_edit_profile));
                etPanNo.setEnabled(false);
            }


            etDob.clearFocus();
//            etEmail.setEnabled(false);
//            etMobile.setEnabled(false);
//            etAddress.setEnabled(false);
//            etCity.setEnabled(false);
//            etPincode.setEnabled(false);
//            etState.setEnabled(false);

        }



        Intent intent = getIntent();
        strLoginId = intent.getStringExtra("id");
        Log.d(TAG, " id data is:" + strLoginId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "memberprofile");
            jsonObject.put("memberid", strLoginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonObject.length() > 0) {
            new getData().execute(String.valueOf(jsonObject));

        }


        btnUpadateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strSponsorId = etSponsorID.getText().toString();
                String strPlacementId = etPlacementId.getText().toString();
                String strFirstName = etFirstName.getText().toString();
                String strLastName = etLastName.getText().toString();
                String strDob = etDob.getText().toString();
                String strPannp = etPanNo.getText().toString();
                String strEmail = etEmail.getText().toString();
                String strMobile = etMobile.getText().toString();
                String strAddress = etAddress.getText().toString();
                String strCity = etCity.getText().toString();
                String strPincode = etPincode.getText().toString();
                String strState = etState.getText().toString();
                String strNomineeFullName = etNomineeFullName.getText().toString();
                String strRelationshipwithNominee = etRelationshipwithNominee.getText().toString();
                String strBankName = etBankName.getText().toString();
                String strAccountNo = etAccountNo.getText().toString();
                String strIFSCCode = etIFSCCode.getText().toString();
                String strBranchName = etBranchName.getText().toString();


                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mode", "geteditprofile");
                    jsonObject.put("memberid", LoginMainActivity.strId);
                    jsonObject.put("SponsorID", strSponsorId);
                    jsonObject.put("placementid", strPlacementId);
                    jsonObject.put("firstname", strFirstName);
                    jsonObject.put("lastname", strLastName);
                    jsonObject.put("dob", strDob);
                    jsonObject.put("panno", strPannp);
                    jsonObject.put("Email", strEmail);
                    jsonObject.put("mobile", strMobile);
                    jsonObject.put("Address", strAddress);
                    jsonObject.put("city", strCity);
                    jsonObject.put("zip", strPincode);
                    jsonObject.put("state", strState);
                    jsonObject.put("nomineename", strNomineeFullName);
                    jsonObject.put("relwithnominee", strRelationshipwithNominee);
                    jsonObject.put("bankname", strBankName);
                    jsonObject.put("accno", strAccountNo);
                    jsonObject.put("ifsc", strIFSCCode);
                    jsonObject.put("branch", strBranchName);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jsonObject.length() > 0) {

                    new updateProfile().execute(String.valueOf(jsonObject));
                }


            }
        });
    }

    private class getData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ViewProfileActivity.this);
            pd.setIndeterminate(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Please Wait..");
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
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    return null;
                }
                strProductResponse = buffer.toString();
                Log.i(TAG, strProductResponse);
                return strProductResponse;
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

            return strProductResponse;
        }

        //**        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                pd.dismiss();
            }
            try {
                JSONObject jsonObject = new JSONObject(strProductResponse);
                String strStatus = jsonObject.getString("status");
                String strMessagge = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                if (!jsonArray.equals("null")) {
                    Log.d(TAG, "response data:" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String strId = object.getString("id");
                        String strSponsorId = object.getString("sponsorid");
                        String strPlacementID = object.getString("placementid");
                        String strNomineeName = object.getString("nomineename");
                        String strMemberName = object.getString("membername");
                        String strLastName = object.getString("lastname");
                        String strFirstName = object.getString("firstname");
                        String strDob = object.getString("dob");
                        String strAddress = object.getString("address");
                        String strState = object.getString("state");
                        String strCity = object.getString("city");
                        String strPincode = object.getString("pincode");
                        String strMobileNo = object.getString("mobile");
                        Log.d(TAG, "Mobile data:" + strMobileNo);
                        String strPanNo = object.getString("panno");
                        String strEmail = object.getString("email");
                        String strBankName = object.getString("bankname");
                        String strBranchName = object.getString("branchname");
//                        String strBranchCity=object.getString("branchcity");
                        String strBankAccountNo = object.getString("bankaccno");
                        String strIfsccode = object.getString("ifsccode");
                        String strPassword = object.getString("password");
                        String strMemberId = object.getString("memberid");
                        String strTransaction_password = object.getString("transaction_password");
                        String strRelwithnominee = object.getString("relwithnominee");

                        profile = new Profile();
                        profile.setId(strId);
                        profile.setSponsorid(strSponsorId);
                        profile.setMembername(strMemberName);
                        profile.setLastname(strLastName);
                        profile.setPassword(strPassword);
                        profile.setFirstname(strFirstName);
                        profile.setEmail(strEmail);
                        profile.setAddress(strAddress);
                        profile.setBankaccountno(strBankAccountNo);
                        profile.setBankname(strBankName);
                        profile.setBranchname(strBranchName);
                        profile.setCity(strCity);
                        profile.setState(strState);
                        profile.setDob(strDob);
                        profile.setPincode(strPincode);
                        profile.setMobile(strMobileNo);
                        profile.setPanno(strPanNo);
                        profile.setNomineename(strNomineeName);
                        profile.setPlacementid(strPlacementID);
                        profile.setRelwithnominee(strRelwithnominee);
//                        profile.setBranchcity(strBranchCity);
                        profile.setIfsccode(strIfsccode);
                        profile.setMemberid(strMemberId);
                        profile.setTransaction_password(strTransaction_password);
                        listProfile.add(profile);

                        String mobileno = profile.getMobile();
                        Log.d(TAG, "Mobile no profile" + mobileno);
                        etSponsorID.setText(profile.getSponsorid());
                        etPlacementId.setText(profile.getPlacementid());
                        etMobile.setText(profile.getMobile());
                        etFirstName.setText(profile.getFirstname());
                        etLastName.setText(profile.getLastname());
                        etAccountNo.setText(profile.getBankaccountno());
                        etIFSCCode.setText(profile.getIfsccode());
                        etBankName.setText(profile.getBankname());
                        etBranchName.setText(profile.getBranchname());
                        etEmail.setText(profile.getEmail());
                        etDob.setText(profile.getDob());
                        etLastName.setText(profile.getLastname());
                        etState.setText(profile.getState());
                        etAddress.setText(profile.getAddress());
//                        txtPassword.setText(profile.getPassword());
                        etPincode.setText(profile.getPincode());
                        etCity.setText(profile.getCity());
                        etPanNo.setText(profile.getPanno());
                        etNomineeFullName.setText(profile.getNomineename());
                        etRelationshipwithNominee.setText(profile.getRelwithnominee());
//                        txtTransactionPwd.setText(profile.getTransaction_password());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No Data found", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private class updateProfile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ViewProfileActivity.this);
            pd.setIndeterminate(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Please Wait..");
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
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    return null;
                }
                strUpdateResponse = buffer.toString();
                Log.i(TAG, strUpdateResponse);
                return strUpdateResponse;
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

            return strUpdateResponse;
        }

        //**        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                pd.dismiss();
            }
            try {
                JSONObject jsonObject = new JSONObject(strUpdateResponse);
                String strStatus = jsonObject.getString("status");
                String strMessagge = jsonObject.getString("message");
                if (!strMessagge.equalsIgnoreCase("No Data found")){
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    Log.d(TAG, "response data:" + jsonArray);
                    String strResponse=jsonArray.getString(0);
                    Toast.makeText(ViewProfileActivity.this, strResponse, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "ViewProfile update Response: "+strResponse);

                    SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,0);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("id","1");
                    editor.commit();

                }else {

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

