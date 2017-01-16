package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.adpter.CartPojo;
import com.toranado.bestafe.adpter.CustomGrid;
import com.toranado.bestafe.adpter.SubcategoryAdapter;
import com.toranado.bestafe.adpter.Subcategory_grid;
import com.toranado.bestafe.interfaces.ClickListner;
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

public class CategorySubCategoryActivity extends AppCompatActivity {

    private static final String TAG = CategorySubCategoryActivity.class.getSimpleName();
    private String strCategoryName = "", strCategoryId = "";
    private String strItemName = "", strCategory_id = "", strImage = "";

    private Toolbar toolbar_subcategory;
    private TextView txtTitle;
    private String strDeviceId = "", strCartJsonResponse = "";
    ArrayList<String> listItemName = new ArrayList<>();
    ArrayList<String> listCategoryID = new ArrayList<>();
    ArrayList<String> listImage = new ArrayList<>();
    ArrayList<String> listOrder = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefres;
    GridView grid_subcategory;
    ImageView imgEmptyData;
    ProgressDialog pd;
    //    ViewHolder holder = null;
    SubcategoryAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private StaggeredGridLayoutManager gridLayoutManager;
    private RecyclerView mRecyclerView;
    private RelativeLayout relative_layout_sort;
    private LinearLayout liner_layout_filter;
    SearchView mSearch;
    ImageView imgSearch;
    private static final int DIRECTION_UP = -1;

//    BottomSheetBehavior bottomSheetBehavior ;
//
//    BottomSheetDialog bottomSheetDialog ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sub_category);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        strCategoryName = intent.getStringExtra("CategoryName");
        strCategoryId = intent.getStringExtra("CategoryId");
        imgEmptyData = (ImageView) findViewById(R.id.imgEmptyData);
        toolbar_subcategory = (Toolbar) findViewById(R.id.toolbar_subcategory);
        txtTitle = (TextView) toolbar_subcategory.findViewById(R.id.txtTitle);
        toolbar_subcategory.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar_subcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        strDeviceId = com.toranado.bestafe.utils.StringUtils.getDeviceId(getApplicationContext());

        mSearch= (SearchView)toolbar_subcategory.findViewById(R.id.mSearch);

        imgSearch=(ImageView)toolbar_subcategory.findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSearch.setVisibility(View.GONE);
                mSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.GONE);
            }
        });
        mSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                imgSearch.setVisibility(View.VISIBLE);
                mSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });
//        relative_layout_sort= (RelativeLayout) findViewById(R.id.relative_layout_sort);
//        liner_layout_filter=(LinearLayout)findViewById(R.id.liner_layout_filter);

        txtTitle.setText(strCategoryName);

//        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.RelativeLayoutSheet));

//        mSwipeRefres = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefres);
//
//        mSwipeRefres.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
//                Color.RED, Color.CYAN);
//
//        mSwipeRefres.setDistanceToTriggerSync(20);// in dips
//        mSwipeRefres.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
//        mSwipeRefres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                JSONObject jsonObject = new JSONObject();
//
//                try {
//
//                    jsonObject.put("mode", "getsubcat");
//                    jsonObject.put("catid", strCategoryId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (jsonObject.length() > 0) {
//                    new getSubCategoryData().execute(String.valueOf(jsonObject));
//                }
//            }
//        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        gridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setGapStrategy(0);


        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("mode", "getsubcat");
            jsonObject.put("catid", strCategoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getSubCategoryData().execute(String.valueOf(jsonObject));
        }


        searchingData(mSearch);



    }

    private void searchingData(SearchView mSearch) {

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<String> filterList=new ArrayList<String>();

                for (int i=0; i<listItemName.size(); i++){

                    final  String text=listItemName.get(i).toLowerCase();
                    if (text.contains(newText.toLowerCase())){
                        filterList.add(listItemName.get(i));
                    }
                }

                gridLayoutManager = new StaggeredGridLayoutManager(3, 1);
                mRecyclerView.setLayoutManager(gridLayoutManager);

                adapter = new SubcategoryAdapter(CategorySubCategoryActivity.this, filterList, listCategoryID, listImage);
                mRecyclerView.setAdapter(adapter);

                return false;
            }
        });
    }


    private class getSubCategoryData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(CategorySubCategoryActivity.this);
            pd.setIndeterminate(false);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(false);
            pd.setCanceledOnTouchOutside(false);
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

            listImage.clear();
            listCategoryID.clear();
            listItemName.clear();

            if (pd.isShowing()){
                pd.dismiss();
            }

            try {
                JSONObject jsonObject = new JSONObject(strCartJsonResponse);
                String strMessage = jsonObject.getString("message");
                if (!strMessage.equals("No Data found")) {
                    imgEmptyData.setVisibility(View.GONE);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    Log.d(TAG, "json response: " + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strItemName = object.getString("name");
                        String strCategory_id = object.getString("category_id");
                        String strImage = object.getString("image");
                        String strReplaceImageURL = strImage.replace(" ", "%20");

                        listItemName.add(strItemName);
                        listCategoryID.add(strCategory_id);
                        listImage.add(Constant.strImageURL + strReplaceImageURL);
                    }


//                        adapter.notifyDataSetChanged();
                        adapter = new SubcategoryAdapter(CategorySubCategoryActivity.this, listItemName, listCategoryID, listImage);
                        mRecyclerView.setAdapter(adapter);
//                        mSwipeRefres.setRefreshing(false);




                } else {

                    imgEmptyData.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
