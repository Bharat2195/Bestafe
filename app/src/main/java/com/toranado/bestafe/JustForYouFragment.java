package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.adpter.DashBoardCategoryAdapter;
import com.toranado.bestafe.utils.*;

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
public class JustForYouFragment extends Fragment {

    private static final String TAG = JustForYouFragment.class.getSimpleName();
    private static String strCategoryName = "", strCategoryId = "", strProductInfo = "", strModelName = "", strImagePath = "", strPid = "", strPrice = "";
    private ImageView imgCart;
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private StaggeredGridLayoutManager gridLayoutManager;
    ArrayList<String> alName = new ArrayList<>();
    ArrayList<String> alImage = new ArrayList<>();
    ArrayList<String> listPId = new ArrayList<>();
    ArrayList<String> listPrice = new ArrayList<>();
    ArrayList<String> listMpn = new ArrayList<>();
    ArrayList<String> listCartModel=new ArrayList<>();
    ImageView imgCartIcon;
    TextView txtNo;
    //    Spinner spinner_item;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayAdapter<String> adapter;
    static String strDeviceId = "", strCartJsonResponse = "";
    ProgressDialog pd;

    
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_just_for_you,container,false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        strDeviceId = com.toranado.bestafe.utils.StringUtils.getDeviceId(getActivity().getApplicationContext());
//        mSwipeRefreshLayout = (SwipeRefreshLayout)getActivity().findViewById(R.id.swifeRefresh);
//        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
//                Color.RED, Color.CYAN);
//        mSwipeRefreshLayout.setDistanceToTriggerSync(20);// in dips
//        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
////        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);

        mRecyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

//        Intent intent = getActivity(),getIntent();
//        strCategoryName = intent.getStringExtra("CategoryName");
//        strCategoryId = intent.getStringExtra("CategoryId");
//        adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item_dashboard,null);



//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                JSONObject jsonProductInfo = new JSONObject();
//
//                try {
//                    jsonProductInfo.put("mode", "getproductinfo");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (jsonProductInfo.length() > 0) {
//                    new getProductInfoReferesh().execute(String.valueOf(jsonProductInfo));
//                }
//                JSONObject jsonObject = new JSONObject();
//
//                try {
//
//                    jsonObject.put("mode", "cartdetail");
//                    jsonObject.put("did", strDeviceId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (jsonObject.length() > 0) {
//                    new getCartdata().execute(String.valueOf(jsonObject));
//                }
//
////                mSwipeRefreshLayout.setRefreshing(false);
//
//            }
//        });

//        JSONObject jsonObject = new JSONObject();
//
//        try {
//
//            jsonObject.put("mode", "cartdetail");
//            jsonObject.put("did", strDeviceId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (jsonObject.length() > 0) {
//            new getCartdata().execute(String.valueOf(jsonObject));
//        }
//        spinner_item.setAdapter(adapter);

        JSONObject jsonProductInfo = new JSONObject();

        try {
            jsonProductInfo.put("mode", "getproductinfo");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonProductInfo.length() > 0) {
            new getProductInfo().execute(String.valueOf(jsonProductInfo));
        }

//        toolbar_category_dashboard = (Toolbar) findViewById(R.id.toolbar_category_dashboard);
//        imgCart=(SetImage)toolbar_category_dashboard.findViewById(R.id.imgCart);
//        txtTitle = (TextView) toolbar_category_dashboard.findViewById(R.id.txtTitle);
//        imgCartIcon = (SetImage) toolbar_category_dashboard.findViewById(R.id.imgCartIcon);
//        txtNo=(TextView)toolbar_category_dashboard.findViewById(R.id.txtNo);
//        txtTitle.setText(strCategoryName);
//        imgCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentCart=new Intent(DashBoardCategoryActivity.this,DashboardCartActivity.class);
//                startActivity(intentCart);
//            }
//        });
//        toolbar_category_dashboard.setNavigationIcon(R.drawable.arrowleft);
//        toolbar_category_dashboard.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });




    }

    private class getProductInfo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pd==null){
                pd= com.toranado.bestafe.utils.StringUtils.createProgressDialog(getActivity());
                pd.show();
            }else {
                pd.show();
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
                strProductInfo = buffer.toString();
                //send to post execute
                return strProductInfo;
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

            return strProductInfo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            alName.clear();
            alImage.clear();
            listPId.clear();
            listPrice.clear();
            listMpn.clear();
            try {

                JSONObject jsonObject = new JSONObject(strProductInfo);
                Log.d(TAG, "productInfor Data:" + jsonObject);
//                if (jsonObject != null) {
//                    pd.dismiss();
//                }

                String strMessage = jsonObject.getString("message");
                if (strMessage.contains("Success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strModelName = object.getString("model");
                        strImagePath = object.getString("image");
                        strPid = object.getString("product_id");
                        strPrice = object.getString("price");
                        String[] strSplit = strPrice.split("\\.");
                        String values = strSplit[0];
                        String strMpn = object.getString("mpn");


                        alImage.add(strImagePath);
                        alName.add(strModelName);
                        listPId.add(strPid);
                        listPrice.add(values);
                        listMpn.add(strMpn);

                    }
                    pd.dismiss();
                    mAdapter = new DashBoardCategoryAdapter(getActivity(), alName, alImage, listPId, listPrice, listMpn);
                    mRecyclerView.setAdapter(mAdapter);
//                    mSwipeRefreshLayout.setRefreshing(false);

//                    mRecyclerView.scrollToPosition(0);


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private class getCartdata extends AsyncTask<String, String, String> {

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
                strCartJsonResponse = buffer.toString();
                //send to post execute
                return strCartJsonResponse;
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

            return strCartJsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listCartModel.clear();
            try {
//                mSwipeRefreshLayout.setRefreshing(false);
                JSONObject jsonObject = new JSONObject(strCartJsonResponse);

                String strMessage = jsonObject.getString("message");
                if (strMessage.contains("Success")) {
                    imgCartIcon.setVisibility(View.VISIBLE);
                    txtNo.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strModel = object.getString("model");
                        String strMpn = object.getString("mpn");
                        String strPrice = object.getString("price");
                        String strProductID = object.getString("product_id");

                        listCartModel.add(strModel);
                        String strSize=String.valueOf(listCartModel.size());
                        txtNo.setText(strSize);

//                        String[] strSplit = strPrice.split("\\.");
//                        String values = strSplit[0];
//                        String strMpn=object.getString("mpn");

                    }


                } else {
//                    imgCartIcon.setVisibility(View.GONE);
//                    txtNo.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getProductInfoReferesh extends AsyncTask<String, String, String> {

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
                strProductInfo = buffer.toString();
                //send to post execute
                return strProductInfo;
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

            return strProductInfo;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            alName.clear();
            alImage.clear();
            listPId.clear();
            listPrice.clear();
            listMpn.clear();
            try {

                JSONObject jsonObject = new JSONObject(strProductInfo);
                Log.d(TAG, "productInfor Data:" + jsonObject.toString());

                String strMessage = jsonObject.getString("message");
                if (strMessage.contains("Success")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strModelName = object.getString("model");
                        strImagePath = object.getString("image");
                        strPid = object.getString("product_id");
                        strPrice = object.getString("price");
                        String[] strSplit = strPrice.split("\\.");
                        String values = strSplit[0];
                        String strMpn = object.getString("mpn");


                        alImage.add(strImagePath);
                        alName.add(strModelName);
                        listPId.add(strPid);
                        listPrice.add(values);
                        listMpn.add(strMpn);

                    }
                    mAdapter = new DashBoardCategoryAdapter(getActivity(), alName, alImage, listPId, listPrice, listMpn);
                    mRecyclerView.setAdapter(mAdapter);
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    mRecyclerView.scrollToPosition(0);


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
