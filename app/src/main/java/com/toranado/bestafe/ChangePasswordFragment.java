package com.toranado.bestafe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

/**
 * Created by cphp on 17-Jan-17.
 */
public class ChangePasswordFragment extends Fragment {
    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnUpadateNow;
    String strChangePwdRespose="",strChangePwdMessage="",strOldPassword = "", strNewPassword = "", strConfirmPassword = "", strMemberid = "",JsonResponse="",strMessagge ="",strChangePasswordResponse="";
    private static final String TAG = ChangePasswordFragment.class.getSimpleName();
    TextView txtErrorOldPassword,txtErrorConPassword, txtSignIn,txtTitle;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_password,container,false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        strMemberid=LoginMainActivity.strId;

        txtErrorConPassword= (TextView) getActivity().findViewById(R.id.txtErrorConPassword);
        txtErrorOldPassword= (TextView) getActivity().findViewById(R.id.txtErrorOldPassword);

        etOldPassword = (EditText) getActivity().findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) getActivity().findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) getActivity().findViewById(R.id.etConfirmPassword);
        btnUpadateNow = (Button) getActivity().findViewById(R.id.btnUpadateNow);

        etOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText editText;

                if (!hasFocus){
                    JSONObject jsonObject=new JSONObject();
                    try{
                        editText=(EditText)v;
                        String oldPassword=editText.getText().toString();
                        Log.d(TAG,"MemberId:"+oldPassword);
                        jsonObject.put("mode","checkpassword");
                        jsonObject.put("memberid",strMemberid);
                        jsonObject.put("password",oldPassword);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (jsonObject.length()>0){
                        new checkPassword().execute(String.valueOf(jsonObject));
                    }
                }
            }
        });

        etNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String strnewPassword=s.toString();

                try{
                    if (!strnewPassword.equalsIgnoreCase(etNewPassword.getText().toString())){
                        txtErrorConPassword.setVisibility(View.VISIBLE);
                    }else {
                        txtErrorConPassword.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btnUpadateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strOldPassword = etOldPassword.getText().toString();
                strNewPassword = etNewPassword.getText().toString();
                strConfirmPassword = etConfirmPassword.getText().toString();

                if (StringUtils.isBlank(strOldPassword)) {
                    Toast.makeText(getActivity(), "Please Enter OldPassword", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txtErrorOldPassword.getVisibility()==View.VISIBLE){
                    Toast.makeText(getActivity(), "Invaid Password", Toast.LENGTH_SHORT).show();
                }
                if (StringUtils.isBlank(strNewPassword)) {
                    Toast.makeText(getActivity(), "Please Enter New Password", Toast.LENGTH_SHORT).show();
                }
                if (StringUtils.isBlank(strConfirmPassword)) {
                    Toast.makeText(getActivity(), "Please Enter ConfirmPassword", Toast.LENGTH_SHORT).show();
                }

                if (!strConfirmPassword.equals(strNewPassword)){
                    Toast.makeText(getActivity(),"Both Password don't match",Toast.LENGTH_SHORT).show();
                }else if (strConfirmPassword.equals(strNewPassword)){

                    if (strMessagge.equals("Success")){
                        JSONObject jsonObject=new JSONObject();
                        try{
                            jsonObject.put("mode","changepassword");
                            jsonObject.put("memberid",strMemberid);
                            jsonObject.put("password",strNewPassword);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if (jsonObject.length()>0){
                            new Postdata().execute(String.valueOf(jsonObject));
                        }
                    }else {
                        Toast.makeText(getActivity(),"Please Enter Correct Old Password",Toast.LENGTH_SHORT).show();
                    }

                }

            }




        });



    }

    private class checkPassword  extends AsyncTask<String,String,String> {
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


            if (strMessagge.equals("Success")){
                Toast.makeText(getActivity(),"Old Password Correct",Toast.LENGTH_SHORT).show();
                txtErrorOldPassword.setVisibility(View.GONE);
            }else {
                Toast.makeText(getActivity(),"Old Password Incorrect",Toast.LENGTH_SHORT).show();
                txtErrorOldPassword.setVisibility(View.VISIBLE);
                etNewPassword.setClickable(false);
                etConfirmPassword.setClickable(false);
            }
        }
    }


    private class Postdata  extends AsyncTask<String,String,String>{
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

            if (strMessagge.equals("Success")){
                Toast.makeText(getActivity(),strChangePasswordResponse,Toast.LENGTH_SHORT).show();
                etOldPassword.getText().clear();
                etNewPassword.getText().clear();
                etConfirmPassword.getText().clear();
            }else {
                Toast.makeText(getActivity(),strChangePasswordResponse,Toast.LENGTH_SHORT).show();
                etNewPassword.setClickable(false);
                etConfirmPassword.setClickable(false);
            }
        }
    }

}
