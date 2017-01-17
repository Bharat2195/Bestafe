package com.toranado.bestafe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by cphp on 17-Jan-17.
 */
public class ChangeTransactionFragment extends Fragment {

    private static final String TAG = ChangeTransactionFragment.class.getSimpleName();
    String strOldPassword = "", strNewPassword = "", strConfirmPassword = "", strMemberid = "", JsonResponse = "", strMessagge = "", strChangePasswordResponse = "";
    @InjectView(R.id.viewLine)
    View viewLine;
    @InjectView(R.id.txtTransactionOldPassword)
    TextView txtTransactionOldPassword;
    @InjectView(R.id.etTransactionOldPassword)
    EditText etTransactionOldPassword;
    @InjectView(R.id.txtTransactionErrorOldPassword)
    TextView txtTransactionErrorOldPassword;
    @InjectView(R.id.txtTransactionNewPassword)
    TextView txtTransactionNewPassword;
    @InjectView(R.id.etTransactionNewPassword)
    EditText etTransactionNewPassword;
    @InjectView(R.id.txtTransactionConfirmPassword)
    TextView txtTransactionConfirmPassword;
    @InjectView(R.id.etTransactionConfirmPassword)
    EditText etTransactionConfirmPassword;
    @InjectView(R.id.txtTransactionErrorConPassword)
    TextView txtTransactionErrorConPassword;
    @InjectView(R.id.btnTransactionUpadateNow)
    Button btnTransactionUpadateNow;
//    TextView txtSignIn,txtTitle,txtErrorOldPassword,txtErrorConPassword;
//    private EditText etOldPassword, etNewPassword, etConfirmPassword;
//    private Button btnUpadateNow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_transaction_password, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        strMemberid = LoginMainActivity.strId;

        etTransactionOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


        btnTransactionUpadateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPassword = etTransactionOldPassword.getText().toString();
                strNewPassword = etTransactionOldPassword.getText().toString();
                strConfirmPassword = etTransactionConfirmPassword.getText().toString();

                if (StringUtils.isBlank(strOldPassword)) {
                    Toast.makeText(getActivity(), "Please Enter OldPassword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(strNewPassword)) {
                    Toast.makeText(getActivity(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (txtTransactionErrorOldPassword.getVisibility() == View.VISIBLE) {
                    Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isBlank(strConfirmPassword)) {
                    Toast.makeText(getActivity(), "Please Enter ConfirmPassword", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!strConfirmPassword.equals(strNewPassword)) {
                    Toast.makeText(getActivity(), "Both Password don't match", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "Please Enter Correct Old Password", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
                Toast.makeText(getActivity(), "Old Password Correct", Toast.LENGTH_SHORT).show();
                txtTransactionErrorOldPassword.setVisibility(View.GONE);
            } else {
                txtTransactionErrorOldPassword.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Old Password Incorrect", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), strChangePasswordResponse, Toast.LENGTH_SHORT).show();
                etTransactionOldPassword.getText().clear();
                etTransactionNewPassword.getText().clear();
                etTransactionConfirmPassword.getText().clear();
            } else {
                Toast.makeText(getActivity(), strChangePasswordResponse, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
