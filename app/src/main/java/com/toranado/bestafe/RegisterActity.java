package com.toranado.bestafe;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.utils.Constant;
import com.toranado.bestafe.utils.ValidationUtils;

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
import java.util.Calendar;

public class RegisterActity extends AppCompatActivity {

    private EditText etNomineeFullName, etMiddleName, etSponsorID, etPlacementId, etFirstname, etLastname, etAddress, etLandMark, etCity, etPincode, etState, etCountry, etDateofbirth, etEmail,
            etRelaNomineeFullName, etMobile, etPwd, etConfirmPwd, etSecurePassword, etConfirmSecurePassword, etPanno, etBankHolderName, etBankName, etAccountno, etIFSCCode, etBrachName;
    Button btnRegister, btnLogin;
    private int mYear, mMonth, mDay;
    CheckBox chkTearmCondition, chAutoSponsor;
    String strSponsorId = "", strPlacementId = "", strFirstName = "", strLastName = "", strAddress = "", strLandMark = "", strCity = "", strPincode = "", strState = "",
            strCountry, strDateOfBirth = "", strEmail = "", strMobile = "", strPwd = "", strConfirmPwd = "", strSecurePassword = "", strConfirmSecurePassword = "", strPanno = "", strBankHolderName = "", strBankName = "", strAccountno = "", strIFSCCode = "", strBranchName = "";
    String strRelaNomineeFullName, strMiddleName = "", strNomieeName = "", strJsonResponse = "", strDOB = "", JsonResponse = "", strMessagge = "", SponsorID = "", PlacementName = "", strCheckSponsor = "", strSponsorCheckResult = "", strSpnsotCheckResponse = "";
    private static final String TAG = RegisterActity.class.getSimpleName();
    Toolbar toolbar_register;
    TextView txtSignIn, txtTitle;
    ImageView imgSignIn, imgCalener;
    private String strName="";
    public static final String PREFS_NAME = "LoginPrefes";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_actity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        Intent intent=getIntent();
        strName=intent.getStringExtra("name");

        toolbar_register = (Toolbar) findViewById(R.id.toolbar_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_register.setNavigationIcon(R.drawable.arrowleft);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar_register.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        txtTitle = (TextView) toolbar_register.findViewById(R.id.txtTitle);
        txtTitle.setText("Registration");


        chAutoSponsor = (CheckBox) findViewById(R.id.chAutoSponsor);
        chkTearmCondition = (CheckBox) findViewById(R.id.chkTearmCondition);
        etSponsorID = (EditText) findViewById(R.id.etSponsorID);
        etPlacementId = (EditText) findViewById(R.id.etPlacementId);
        etFirstname = (EditText) findViewById(R.id.etFirstname);
        etLastname = (EditText) findViewById(R.id.etLastname);
        etMiddleName = (EditText) findViewById(R.id.etMiddleName);
        etNomineeFullName = (EditText) findViewById(R.id.etNomineeFullName);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etLandMark = (EditText) findViewById(R.id.etLandMark);
        etCity = (EditText) findViewById(R.id.etCity);
        etPincode = (EditText) findViewById(R.id.etPincode);
        etState = (EditText) findViewById(R.id.etState);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etDateofbirth = (EditText) findViewById(R.id.etDateofbirth);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etPwd = (EditText) findViewById(R.id.etPwd);
        etConfirmPwd = (EditText) findViewById(R.id.etConfirmPwd);
        etSecurePassword = (EditText) findViewById(R.id.etSecurePassword);
        etConfirmSecurePassword = (EditText) findViewById(R.id.etConfirmSecurePassword);
        etBankHolderName = (EditText) findViewById(R.id.etBankHolderName);
        etPanno = (EditText) findViewById(R.id.etPanno);
        etBankName = (EditText) findViewById(R.id.etBankName);
        etAccountno = (EditText) findViewById(R.id.etAccountno);
        etIFSCCode = (EditText) findViewById(R.id.etIFSCCode);
        etBrachName = (EditText) findViewById(R.id.etBrachName);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        imgCalener = (ImageView) findViewById(R.id.imgCalener);
        etRelaNomineeFullName = (EditText) findViewById(R.id.etRelaNomineeFullName);


        etSponsorID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText;

                if (!hasFocus) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        editText = (EditText) v;
                        String SponsorID = editText.getText().toString();
                        Log.d(TAG, "EditText Sponsor ID:" + SponsorID);
                        jsonObject.put("mode", "checksponsor");
                        jsonObject.put("sponsorid", SponsorID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonObject.length() > 0) {
                        new checkSponsorID().execute(String.valueOf(jsonObject));
                    }
                }
            }
        });

        chAutoSponsor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mode", "getautosponsor");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonObject.length() > 0) {
                        new GetSponsorID().execute(String.valueOf(jsonObject));
                    }
                } else if (!isChecked) {
                    etPlacementId.getText().clear();
                    etSponsorID.getText().clear();
                }
            }
        });
        imgCalener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        etDateofbirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                imgCalener.setVisibility(View.INVISIBLE);
            }
        });
        /*etDateofbirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar calendar = Calendar.getInstance();
                    mYear = calendar.get(Calendar.YEAR);
                    mMonth = calendar.get(Calendar.MONTH);
                    mDay = calendar.get(Calendar.DATE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            etDateofbirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();


                }
            }
        })*/
        ;

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSponsorId = etSponsorID.getText().toString();
                strPlacementId = etPlacementId.getText().toString();
                strFirstName = etFirstname.getText().toString();
                strMiddleName = etMiddleName.getText().toString();
                strLastName = etLastname.getText().toString();
                strAddress = etAddress.getText().toString();
                strLandMark = etLandMark.getText().toString();
                strCity = etCity.getText().toString();
                strPincode = etPincode.getText().toString();
                strState = etState.getText().toString();
                strCountry = etCountry.getText().toString();
                strDateOfBirth = etDateofbirth.getText().toString();
                strEmail = etEmail.getText().toString();
                strMobile = etMobile.getText().toString();
                strPwd = etPwd.getText().toString();
                strConfirmPwd = etConfirmPwd.getText().toString();
                strSecurePassword = etSecurePassword.getText().toString();
                strConfirmSecurePassword = etConfirmSecurePassword.getText().toString();
                strPanno = etPanno.getText().toString();
                strNomieeName = etNomineeFullName.getText().toString();
                strBankHolderName = etBankHolderName.getText().toString();
                strBankName = etBankName.getText().toString();
                strAccountno = etAccountno.getText().toString();
                strIFSCCode = etIFSCCode.getText().toString();
                strBranchName = etBrachName.getText().toString();
                strRelaNomineeFullName = etRelaNomineeFullName.getText().toString();


                if (StringUtils.isBlank(strSponsorId)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Sponsor Id", Toast.LENGTH_SHORT).show();
                } else if (strSponsorCheckResult.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Invalid Sponsor Id", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strFirstName)) {
                    Toast.makeText(getApplicationContext(), "Please Fill FirstName", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strMiddleName)) {
                    Toast.makeText(getApplicationContext(), "Please Fill MiddleName", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strLastName)) {
                    Toast.makeText(getApplicationContext(), "Please Fill LastName", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strAddress)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Address", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strCity)) {
                    Toast.makeText(getApplicationContext(), "Please Fill City", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strPincode)) {
                    Toast.makeText(getApplicationContext(), "Please Fill ZipCode", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strState)) {
                    Toast.makeText(getApplicationContext(), "Please Fill State", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strCountry)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Country", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strDateOfBirth)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Date of birth", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strEmail)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Email", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strEmail) || !ValidationUtils.checkEmail(strEmail)) {
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strMobile)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Mobile ", Toast.LENGTH_SHORT).show();
                } else if (strMobile.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Invalid Mobile  Number ", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strPwd)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Password ", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strConfirmPwd)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Confirm Password ", Toast.LENGTH_SHORT).show();
                } else if (!strConfirmPwd.equals(strPwd)) {
                    Toast.makeText(getApplicationContext(), "Your password and confirm password must be same", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strSecurePassword)) {
                    Toast.makeText(getApplicationContext(), "Please Fill Secure Password ", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isBlank(strConfirmSecurePassword)) {
                    Toast.makeText(getApplicationContext(), "Please Fill ConfirmSecure Password ", Toast.LENGTH_SHORT).show();
                } else if (!strConfirmSecurePassword.equals(strSecurePassword)) {
                    Toast.makeText(getApplicationContext(), "Your SecurePassword and confirm Secure Password must be same", Toast.LENGTH_SHORT).show();
                } else if (!chkTearmCondition.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please Select the Tearm Condition Checkbox ", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mode", "registration");
                        jsonObject.put("SponsorID", strSponsorId);
                        jsonObject.put("firstname", strFirstName);
                        jsonObject.put("middlename", strNomieeName);
                        jsonObject.put("lastname", strLastName);
                        jsonObject.put("Address", strAddress);
                        jsonObject.put("landmark", strLandMark);
                        jsonObject.put("City", strCity);
                        jsonObject.put("PinCode", strPincode);
                        jsonObject.put("state", strState);
                        jsonObject.put("dob", strDOB);
                        jsonObject.put("Email", strEmail);
                        jsonObject.put("Mobile", strMobile);
                        jsonObject.put("nomineefullname", strNomieeName);
                        jsonObject.put("relwithnominee", strRelaNomineeFullName);
                        jsonObject.put("Login_Password", strPwd);
                        jsonObject.put("transpass", strSecurePassword);
                        jsonObject.put("PANNo", strPanno);
                        jsonObject.put("bankhn", strBankHolderName);
                        jsonObject.put("bankname", strBankName);
                        jsonObject.put("branch", strBranchName);
                        jsonObject.put("accno", strAccountno);
                        jsonObject.put("ifsc", strIFSCCode);
                        jsonObject.put("type", "0");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jsonObject.length() > 0) {
                        new PostData().execute(String.valueOf(jsonObject));
                    }
                }
            }

        });

    }

    private class GetSponsorID extends AsyncTask<String, String, String> {
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
                JSONArray array = jsonObject.getJSONArray("response");
                String strArrayData = array.getString(0);
                Log.d(TAG, "ArryData:" + strArrayData);
                String[] splitData = strArrayData.split("\\^");
                SponsorID = splitData[0];
                PlacementName = splitData[1];
                Log.d(TAG, "Sponsor Id data:" + SponsorID);
                Log.d(TAG, "Placement Name:" + PlacementName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strMessagge.equals("Success")) {
                Toast.makeText(getApplicationContext(), strMessagge, Toast.LENGTH_SHORT).show();
                etSponsorID.setText(SponsorID);
                etPlacementId.setText(PlacementName);
            } else {
                Toast.makeText(getApplicationContext(), strMessagge, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class checkSponsorID extends AsyncTask<String, String, String> {
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
                strCheckSponsor = buffer.toString();
                Log.i(TAG, strCheckSponsor);
                //send to post execute
                return strCheckSponsor;
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
                JSONObject jsonObject = new JSONObject(strCheckSponsor);
                Log.d(TAG, "jsonObject data:" + jsonObject);
                String strStatus = jsonObject.getString("status");
                strMessagge = jsonObject.getString("message");
                strSponsorCheckResult = jsonObject.getString("result");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                Log.d(TAG, "Json array data:" + jsonArray);
                strSpnsotCheckResponse = jsonArray.getString(0);
                if (strSponsorCheckResult.equals("0")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Invalid Sponsor Id", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else if (!strSponsorCheckResult.equals("0")) {
                    etPlacementId.setText(strSpnsotCheckResponse);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PostData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(RegisterActity.this);
            pd.setMessage("Please wait.....");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.setIndeterminate(false);
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

            if (pd.isShowing()){
                pd.dismiss();
            }
            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);
                Log.d(TAG, "jsonObject data:" + jsonObject);
                String strStatus = jsonObject.getString("status");
                String strMessage = jsonObject.getString("message");
                String strResult = jsonObject.getString("result");
                String strMemberId = jsonObject.getString("memberid");
                Toast.makeText(getApplicationContext(), strMemberId, Toast.LENGTH_SHORT).show();
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                String strMeassage = jsonArray.getString(0);
                Toast.makeText(getApplicationContext(), strMeassage, Toast.LENGTH_SHORT).show();
                etSponsorID.getText().clear();
                etPlacementId.getText().clear();
                etFirstname.getText().clear();
                etLastname.getText().clear();
                etAddress.getText().clear();
                etLandMark.getText().clear();
                etCity.getText().clear();
                etDateofbirth.getText().clear();
                etEmail.getText().clear();
                etMobile.getText().clear();
                etPwd.getText().clear();
                etConfirmPwd.getText().clear();
                etSecurePassword.getText().clear();
                etConfirmSecurePassword.getText().clear();
                etPanno.getText().clear();
                etBankHolderName.getText().clear();
                etBrachName.getText().clear();
                etBankName.getText().clear();
                etAccountno.getText().clear();
                etIFSCCode.getText().clear();
                chAutoSponsor.setChecked(false);
                chkTearmCondition.setChecked(false);

                if (strName.equalsIgnoreCase("payment")){
                    Intent intent=new Intent(RegisterActity.this,CartLoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(RegisterActity.this,LoginActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
