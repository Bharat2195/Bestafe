package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.toranado.bestafe.adpter.CartPojo;
import com.toranado.bestafe.adpter.SubcategoryAdapter;
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

public class Product_subcategory extends AppCompatActivity {

    private static final String TAG = Product_subcategory.class.getSimpleName();
    private Toolbar toolbar_product_subcategory;
    private ListView listviewProductSubCategory;
    private TextView txtTitle, txtSpecialPrice;
    private String strJsonResponse = "", strCategoryId = "", strSubcategoryName = "", strSpecialPriceResponse = "",strPrice="";
    ArrayList<String> listModelName = new ArrayList<>();
    ArrayList<String> listMpn = new ArrayList<>();
    ArrayList<String> listImage = new ArrayList<>();
    ArrayList<String> listPrice = new ArrayList<>();
    ArrayList<String> listProductId = new ArrayList<>();
    ArrayList<String> listFreeShipping = new ArrayList<>();
    ArrayList<String> listOrder = new ArrayList<>();
    ArrayList<String> listSpecialPrice = new ArrayList<>();
    ProgressDialog pd;
    SwipeRefreshLayout mSwipeRefres;
    SearchView mSearch;
    ImageView imgSearch;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_subcategory);

        getSupportActionBar().hide();
        Intent intent = getIntent();
        strCategoryId = intent.getStringExtra("strPid");
        strSubcategoryName = intent.getStringExtra("strName");
//        mSwipeRefres = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefres);
        toolbar_product_subcategory = (Toolbar) findViewById(R.id.toolbar_product_subcategory);
        listviewProductSubCategory = (ListView) findViewById(R.id.listviewProductSubCategory);
        txtTitle = (TextView) toolbar_product_subcategory.findViewById(R.id.txtTitle);

        if (strSubcategoryName.length()>21){
            String strSplitName=strSubcategoryName.substring(0,21);
            Log.d(TAG, "spliname: "+strSplitName);
            txtTitle.setText(strSplitName+" ....");
        }else {
            txtTitle.setText(strSubcategoryName);
        }

        toolbar_product_subcategory.setNavigationIcon(R.drawable.arrowleft);
        toolbar_product_subcategory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listviewProductSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentProductDesctiption = new Intent(Product_subcategory.this, DashBoardProductDescriptionActivity.class);
                intentProductDesctiption.putExtra("strImagePath", listImage.get(position));
                intentProductDesctiption.putExtra("strPid", listProductId.get(position));
                intentProductDesctiption.putExtra("strItemName", listModelName.get(position));
                Log.d(TAG, "listspecial price data: " + listSpecialPrice);
//                intentProductDesctiption.putExtra("strPrice", listSpecialPrice.get(position));
                intentProductDesctiption.putExtra("strSpecialPrice", listPrice.get(position));
                intentProductDesctiption.putExtra("strMpn", listMpn.get(position));
                startActivity(intentProductDesctiption);
            }
        });

        mSearch= (SearchView)toolbar_product_subcategory.findViewById(R.id.mSearch);

        imgSearch=(ImageView)toolbar_product_subcategory.findViewById(R.id.imgSearch);
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

//        mSwipeRefres.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
//                Color.RED, Color.CYAN);
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
//                    jsonObject.put("mode", "getproductinfo");
//                    jsonObject.put("cid", strCategoryId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (jsonObject.length() > 0) {
//                    new getProductData().execute(String.valueOf(jsonObject));
//                }
//
//            }
//        });


        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("mode", "getproductinfo");
            jsonObject.put("cid", strCategoryId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getProductData().execute(String.valueOf(jsonObject));
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

                for (int i=0; i<listProductId.size(); i++){

                    final  String text=listModelName.get(i).toLowerCase();
                    if (text.contains(newText.toLowerCase())){
                        filterList.add(listModelName.get(i));
                    }
                }

                customAdapter = new CustomAdapter(getApplicationContext(), R.layout.list_item_product_subcategory, listModelName);
                listviewProductSubCategory.setAdapter(customAdapter);

                return false;
            }
        });
    }


    private class getProductData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(Product_subcategory.this);
            pd.setMessage("Please wait...");
            pd.setIndeterminate(false);
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
                strJsonResponse = buffer.toString();
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

            return strJsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pd.isShowing()){
                pd.dismiss();
            }

            listProductId.clear();
            listModelName.clear();
            listImage.clear();
            listPrice.clear();

//            if (pd.isShowing()){
//                pd.dismiss();
//            }
            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);
                String strMessage = jsonObject.getString("message");
                if (!strMessage.contains("No data found")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String strProduct_id = object.getString("product_id");
                        String strModelName = object.getString("model");
                        String strImage = object.getString("image");
                        String strPrice = object.getString("price");
                        String[] spiltPrice = strPrice.split("\\.");
                        String strActualPrice = spiltPrice[0];
                        String strMpn = object.getString("mpn");

                        listProductId.add(strProduct_id);
                        listModelName.add(strModelName);
                        listImage.add(strImage);
                        listPrice.add(strActualPrice);
                        listFreeShipping.add("Free Shipping");
                        listOrder.add("Order");
                        listMpn.add(strMpn);

                    }
//                    mSwipeRefres.setRefreshing(false);
                    if (listviewProductSubCategory != null) {
                        customAdapter = new CustomAdapter(getApplicationContext(), R.layout.list_item_product_subcategory, listModelName);
                        listviewProductSubCategory.setAdapter(customAdapter);


                    } else {
                        Toast.makeText(Product_subcategory.this, "listView is null", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(Product_subcategory.this, "No data Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getRefereshData extends AsyncTask<String, String, String> {

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
                strJsonResponse = buffer.toString();
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

            return strJsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            listProductId.clear();
            listModelName.clear();
            listImage.clear();
            listPrice.clear();

            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);

                String strMessage = jsonObject.getString("message");
                if (!strMessage.contains("No data found")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String strProduct_id = object.getString("product_id");
                        String strModelName = object.getString("model");
                        String strImage = object.getString("image");
                        String strPrice = object.getString("price");
                        String[] spiltPrice = strPrice.split("\\.");
                        String strActualPrice = spiltPrice[0];
                        String strMpn = object.getString("mpn");

                        listProductId.add(strProduct_id);
                        listModelName.add(strModelName);
                        listImage.add(strImage);
                        listPrice.add(strActualPrice);
                        listFreeShipping.add("Free Shipping");
                        listOrder.add(String.valueOf(i) + " Order");
                        listMpn.add(strMpn);


                    }
//                    mSwipeRefres.setRefreshing(false);
                    if (listviewProductSubCategory != null) {
                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), R.layout.list_item_product_subcategory, listModelName);
                        listviewProductSubCategory.setAdapter(customAdapter);

                    } else {
                        Toast.makeText(Product_subcategory.this, "listView is null", Toast.LENGTH_SHORT).show();
                    }


                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class CustomAdapter extends ArrayAdapter<String> {


        List<String> items;
        Context context;
        int resource;


        public CustomAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);

            this.context = context;
            this.resource = resource;
            this.items = items;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(resource, null);

                holder.imgProduct = (ImageView) convertView.findViewById(R.id.imgProduct);
                holder.txtProductTitle = (TextView) convertView.findViewById(R.id.txtProductTitle);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                holder.txtFreeShopping = (TextView) convertView.findViewById(R.id.txtFreeShopping);
                holder.txtrOrder = (TextView) convertView.findViewById(R.id.txtrOrder);
                holder.txtSpecialPrice = (TextView) convertView.findViewById(R.id.txtSpecialPrice);

                convertView.setTag(holder);


            } else {


                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtProductTitle.setText(items.get(position));
            holder.txtPrice.setText("Rs."+listPrice.get(position));
//            holder.txtSpecialPrice.setPaintFlags(holder.txtSpecialPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtFreeShopping.setText(listFreeShipping.get(position));
            holder.txtrOrder.setText(listOrder.get(position));
            Picasso.with(getApplicationContext()).load(listImage.get(position)).placeholder(R.drawable.ic_default).into(holder.imgProduct);


//            JSONObject jsonObject1 = new JSONObject();
//            try {
//                jsonObject1.put("mode", "getspecialprice");
//                jsonObject1.put("pid", listProductId.get(position));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (jsonObject1.length() > 0) {
//                new getSpecialPrice(holder.txtPrice).execute(String.valueOf(jsonObject1),listPrice.get(position));
//            }
//            holder.txtSpecialPrice.setText(listSpecialPrice.get(position));
//            holder.txtSpecialPrice.setPaintFlags(holder.txtSpecialPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            return convertView;

        }
    }


    private class ViewHolder {

        ImageView imgProduct;
        TextView txtProductTitle, txtPrice, txtFreeShopping, txtrOrder, txtSpecialPrice;


    }

    private class getSpecialPrice extends AsyncTask<String, String, String> {

        TextView txtSPrice;

        public getSpecialPrice(TextView txtSpecialPrice) {
            this.txtSPrice = txtSpecialPrice;
        }

        @Override
        protected String doInBackground(String... params) {
            String JsonDATA = params[0];
            strPrice=params[1];

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
                strSpecialPriceResponse = buffer.toString();
                Log.d(TAG, "json response: " + strSpecialPriceResponse);
                //send to post execute
                return strSpecialPriceResponse;
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

            return strSpecialPriceResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject jsonObject = new JSONObject(strSpecialPriceResponse);
                String strMessage = jsonObject.getString("message");
                if (!strMessage.equals("No Data found")){
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strPrice = object.getString("price");
                        Log.d(TAG, "special price: " + strPrice);

                        String[] spiltPrice = strPrice.split("\\.");
                        String sPrice = spiltPrice[0];
                        Log.d(TAG, "spilt price: " + sPrice);
                        listSpecialPrice.add(sPrice);
                        txtSPrice.setText("Rs." + sPrice);
//                    txtSPrice.setPaintFlags(txtSPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }else {
                    Log.d(TAG, "null price: "+strPrice);
//                    txtSPrice.setText("Rs." + strPrice);
                    listSpecialPrice.add("0");
                    txtSPrice.setVisibility(View.GONE);
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
