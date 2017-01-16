package com.toranado.bestafe.adpter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.toranado.bestafe.R;
import com.toranado.bestafe.utils.Constant;
import com.toranado.bestafe.utils.StringUtils;

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

/**
 * Created by cphp on 06-Oct-16.
 */
public class AllCategoryFragment extends Fragment {

    private static final String TAG=AllCategoryFragment.class.getSimpleName();
    private String srtJsonResponse="";
    ArrayList<String> listCategoryName=new ArrayList<>();
    ArrayList<String>listCategoryID=new ArrayList<>();
    ArrayList<String>listImagePath=new ArrayList<>();
    ProgressDialog progressDialog;
    ProgressDialog pd;
    SwipeRefreshLayout mSwipeRefres;
    GridView grid;
//    int[] imageId={
//            R.drawable.mens1,
//            R.drawable.females2,
//            R.drawable.mens1,
//            R.drawable.females2,
//            R.drawable.mens1,
//            R.drawable.females2,
//            R.drawable.mens1,
//
//    };
//    ProgressDialog pd;

    
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_category,container,false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        grid=(GridView)getActivity().findViewById(R.id.grid);
        mSwipeRefres=(SwipeRefreshLayout)getActivity().findViewById(R.id.mSwipeRefres);

        mSwipeRefres.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        mSwipeRefres.setDistanceToTriggerSync(20);// in dips
        mSwipeRefres.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
        mSwipeRefres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                JSONObject jsonObject2 = new JSONObject();

                try {
                    jsonObject2.put("mode", "getcat");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jsonObject2.length() > 0) {
                    new postGetDetailRefersh().execute(String.valueOf(jsonObject2));

                }

            }
        });

        JSONObject jsonObject1 = new JSONObject();

        try {
            jsonObject1.put("mode", "getcat");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject1.length() > 0) {
            new postGetDetail().execute(String.valueOf(jsonObject1));

        }
    }

    private class postGetDetail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = StringUtils.createProgressDialog(getActivity());
                progressDialog.show();
            } else {
                progressDialog.show();
            }
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
                    // Stream was empty. No point in parsing.
                    return null;
                }
                srtJsonResponse = buffer.toString();
//response data
                Log.i(TAG, srtJsonResponse);
                //send to post execute
                return srtJsonResponse;

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
            return srtJsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (s!=null){
//                pd.dismiss();
//            }

            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
//            listCategoryName.clear();
//            listCategoryID.clear();
//            listImagePath.clear();
            try {
                JSONObject jsonObject = new JSONObject(srtJsonResponse);
                String strStatus = jsonObject.getString("status");
                String strMessagge = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for ( int i = 0; i<jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String strCategory_id = object.getString("category_id");
                    String strName = object.getString("name");
                    if (strName.contains("&amp;")) {
                        strName = strName.replace("&amp;", "&");
                    }
                    String strImagePath=object.getString("image");
                    String strReplaceImagePath=strImagePath.replace(" ","%20");
                    listImagePath.add(Constant.strImageURL+strReplaceImagePath);
                    listCategoryName.add(strName);
                    listCategoryID.add(strCategory_id);
                    Log.d(TAG, "listName:" + listCategoryName);
                }
                mSwipeRefres.setRefreshing(false);
                CustomGrid adapter=new CustomGrid(getActivity(),listCategoryName,listCategoryID,listImagePath);
                grid.setAdapter(adapter);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class postGetDetailRefersh extends AsyncTask<String, String, String> {


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
                    // Stream was empty. No point in parsing.
                    return null;
                }
                srtJsonResponse = buffer.toString();
//response data
                Log.i(TAG, srtJsonResponse);
                //send to post execute
                return srtJsonResponse;

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
            return srtJsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (s!=null){
//                pd.dismiss();
//            }
            listCategoryName.clear();
            listCategoryID.clear();
            listImagePath.clear();
            try {
                JSONObject jsonObject = new JSONObject(srtJsonResponse);
                String strStatus = jsonObject.getString("status");
                String strMessagge = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for ( int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String strCategory_id = object.getString("category_id");
                    String strName = object.getString("name");
                    if (strName.contains("&amp;")) {
                        strName = strName.replace("&amp;", "&");
                    }
                    String strImagePath=object.getString("image");
                    String strReplaceImagePath=strImagePath.replace(" ","%20");
                    listImagePath.add(Constant.strImageURL+strReplaceImagePath);
                    listCategoryName.add(strName);
                    listCategoryID.add(strCategory_id);
                    Log.d(TAG, "listName:" + listCategoryName);
                }
                mSwipeRefres.setRefreshing(false);
                CustomGrid adapter=new CustomGrid(getActivity(),listCategoryName,listCategoryID,listImagePath);
                grid.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
