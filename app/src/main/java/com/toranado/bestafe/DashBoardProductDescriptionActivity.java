package com.toranado.bestafe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
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

public class DashBoardProductDescriptionActivity extends AppCompatActivity {

    private static final String TAG = DashBoardProductDescriptionActivity.class.getSimpleName();
    private static String strImagePath = "", strPid = "", strDescitpion = "", strJsonResponse = "", strItemName = "", strPrice = "", strMpn = "", strCartJsonResponse = "";
    ImageView imgProduct, imgCartIcon, imgCart;
    ArrayList<String> listCartModel = new ArrayList<>();
    Bitmap bitmap;
    Toolbar toolbar_product_description;
    TextView txtDescription, txtPrice, txtPv, txtNo, txtSpecialPrice;
    int clickCount = 0;
    FloatingActionButton fab;
    FrameLayout frameLayout;
    Button btnBuyNow, btnCart;
    private String strDeviceID = "", strCartResponse = "", strSpecialPrice = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_product_description);
        getSupportActionBar().hide();

        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtPv = (TextView) findViewById(R.id.txtPv);
        frameLayout = (FrameLayout) findViewById(R.id.container);
        btnCart = (Button) findViewById(R.id.btnCart);
        txtSpecialPrice = (TextView) findViewById(R.id.txtSpecialPrice);
//        btnBuyNow=(Button)findViewById(R.id.btnBuyNow);

//        btnBuyNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(DashBoardProductDescriptionActivity.this,PaymentActivity.class);
//                startActivity(intent);
//            }
//        });

        strDeviceID = com.toranado.bestafe.utils.StringUtils.getDeviceId(getApplicationContext());
        Log.d(TAG, "description deveice id: " + strDeviceID);


        Intent intent = getIntent();

        strImagePath = intent.getStringExtra("strImagePath");
        strPid = intent.getStringExtra("strPid");
        strItemName = intent.getStringExtra("strItemName");
//        strPrice = intent.getStringExtra("strPrice");
        strMpn = intent.getStringExtra("strMpn");
        strSpecialPrice = intent.getStringExtra("strSpecialPrice");
//        Log.d(TAG, "strSpecialPrice: " + strPrice);


        txtPrice.setText("Rs."+strSpecialPrice);
//        txtSpecialPrice.setPaintFlags(txtSpecialPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//
//        if (!strPrice.equals("0")){
//            txtPrice.setText("Rs " + strPrice);
//        }

        txtPv.setText("Bv " + strMpn);


        CollapsingToolbarLayout collapsing_toolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setTitle("Details");
        toolbar_product_description = (Toolbar) findViewById(R.id.toolbar_product_description);
        toolbar_product_description.setNavigationIcon(R.drawable.arrowleft);
        toolbar_product_description.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgCart = (ImageView) toolbar_product_description.findViewById(R.id.imgCart);
        imgCartIcon = (ImageView) toolbar_product_description.findViewById(R.id.imgCartIcon);
        txtNo = (TextView) toolbar_product_description.findViewById(R.id.txtNo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObjectCart = new JSONObject();
                try {
                    jsonObjectCart.put("mode", "addtocart");
                    jsonObjectCart.put("pid", strPid);
                    jsonObjectCart.put("did", strDeviceID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (jsonObjectCart.length() > 0) {
                    new CartPostData().execute(String.valueOf(jsonObjectCart));
                }

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("mode", "cartdetail");
                    jsonObject.put("did", strDeviceID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jsonObject.length() > 0) {
                    new getCartdata().execute(String.valueOf(jsonObject));
                }
            }
        });

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mode", "cartdetail");
            jsonObject.put("did", strDeviceID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getCartdata().execute(String.valueOf(jsonObject));
        }

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listCartModel != null) {
                    Intent intentCart = new Intent(DashBoardProductDescriptionActivity.this, DashboardCartActivity.class);
                    startActivity(intentCart);
                } else {
                    Toast.makeText(DashBoardProductDescriptionActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fab.setImageResource(R.drawable.ic_wishlist_added_md);
                clickCount = clickCount + 1;
                if ((clickCount % 2) == 0) {
                    fab.setImageResource(R.drawable.heart);
                    Toast.makeText(DashBoardProductDescriptionActivity.this, "Remove from Wishlist", Toast.LENGTH_SHORT).show();
                } else {
                    fab.setImageResource(R.drawable.ic_wishlist_added_md);
                    JSONObject jsonObjectCart = new JSONObject();
                    try {
                        jsonObjectCart.put("mode", "addtocart");
                        jsonObjectCart.put("pid", strPid);
                        jsonObjectCart.put("did", strDeviceID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jsonObjectCart.length() > 0) {
                        new CartPostData().execute(String.valueOf(jsonObjectCart));
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("mode", "cartdetail");
                        jsonObject.put("did", strDeviceID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonObject.length() > 0) {
                        new getCartdata().execute(String.valueOf(jsonObject));
                    }
//                    Toast.makeText(DashBoardProductDescriptionActivity.this, "Successfully added to Wishlist", Toast.LENGTH_SHORT).show();
                }
//                if (clickCount==2){
//                    fab.setImageResource(R.drawable.heart);
//                }

            }
        });

        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intentImage = new Intent(DashBoardProductDescriptionActivity.this, DashBoard_product_show_image.class);
                intentImage.putExtra("strImagePath", strImagePath);
                startActivity(intentImage);


            }
        });


        getDescription();
        Picasso.with(getApplicationContext()).load(strImagePath).into(imgProduct);
//        new getBitmapImage().execute(strImagePath);


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
                JSONObject jsonObject = new JSONObject(strCartJsonResponse);
                Log.d(TAG, "cart response: " + jsonObject);

                String strMessage = jsonObject.getString("message");
                if (!strMessage.equals("No Data found")) {
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
                        String strSize = String.valueOf(listCartModel.size());
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

    private class CartPostData extends AsyncTask<String, String, String> {
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
                strCartResponse = buffer.toString();
                //send to post execute
                return strCartResponse;
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
                JSONObject jsonObject = new JSONObject(strCartResponse);
                Log.d(TAG, "json object data:" + jsonObject);
                String strStatus = jsonObject.getString("status");
                String strMessagge = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                Log.d(TAG, "json array data:" + jsonArray);
                String strRecord = jsonArray.getString(0);
                Toast.makeText(getApplicationContext(), strRecord, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Record Data:" + strRecord);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getDescription() {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("mode", "getproduct_desc");
            jsonObject.put("pid", strPid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jsonObject.length() > 0) {
            new GetJsonDes().execute(String.valueOf(jsonObject));
        }
    }

    private class getBitmapImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {

            String strURL = params[0];

            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(strURL).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgProduct.setImageBitmap(bitmap);
        }
    }

    private class GetJsonDes extends AsyncTask<String, String, String> {

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
            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);

                String strMessage = jsonObject.getString("message");
                if (strMessage.contains("Success")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        strDescitpion = object.getString("description");
                        txtDescription.setText(Html.fromHtml(Html.fromHtml(strDescitpion).toString()));

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
