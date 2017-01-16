package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.adpter.CartItem;
import com.toranado.bestafe.adpter.CartPojo;
import com.toranado.bestafe.interfaces.ClickListner;
import com.toranado.bestafe.utils.*;
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
import java.util.List;

public class DashboardCartActivity extends AppCompatActivity {

    private static final String TAG = DashboardCartActivity.class.getSimpleName();
    private Toolbar toolbar_cart;
    private ListView mListview;
    private TextView txtTitle;
    private String strDeviceId = "", strCartJsonResponse = "",strRemoveResponse="";
    ArrayList<String> listModelName = new ArrayList<>();
    ArrayList<String> listMpn = new ArrayList<>();
    ArrayList<String> listPrice = new ArrayList<>();
    ArrayList<String> listProductId = new ArrayList<>();
    ArrayList<String> listQunty = new ArrayList<>();
    ArrayList<CartPojo> listItem = new ArrayList<>();
    int itemQty = 1;
    int totalQty;
    private ImageView imgCart;
    ArrayAdapter<String> spinner_adapter;
    Button btnContinue;
    TextView txtTotalPrice;
//    ViewHolder holder = null;

    SwipeRefreshLayout mSwipeRefres;
    private ProgressDialog pd;
    int i;
    int sum = 0;
    CustomAdapter customAdapter;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_cart);

        getSupportActionBar().hide();
        toolbar_cart = (Toolbar) findViewById(R.id.toolbar_cart);
        mSwipeRefres = (SwipeRefreshLayout) findViewById(R.id.mSwipeRefres);
        imgCart = (ImageView) toolbar_cart.findViewById(R.id.imgCart);
        imgCart.setVisibility(View.GONE);
        txtTitle = (TextView) toolbar_cart.findViewById(R.id.txtTitle);
        strDeviceId = com.toranado.bestafe.utils.StringUtils.getDeviceId(getApplicationContext());
        Log.d(TAG, "deveice id: "+strDeviceId);
        toolbar_cart.setNavigationIcon(R.drawable.arrowleft);
        toolbar_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mListview = (ListView) findViewById(R.id.mListview);
        LayoutInflater layoutInflater=getLayoutInflater();
        view=layoutInflater.inflate(R.layout.rv_footer_item,null);
        txtTotalPrice=(TextView)view.findViewById(R.id.txtTotalPrice);
        btnContinue= (Button) view.findViewById(R.id.btnContinue);

        final CartPojo cartPojo=new CartPojo();
        txtTotalPrice.setText(cartPojo.getStrPrice());

        mListview.addFooterView(view);

        //        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                Toast.makeText(DashboardCartActivity.this, "itemClicked"+position, Toast.LENGTH_SHORT).show();
        //            }
        //        });


        mSwipeRefres.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        mSwipeRefres.setDistanceToTriggerSync(20);// in dips
        mSwipeRefres.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used'

        mSwipeRefres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("mode", "cartdetail");
                    jsonObject.put("did", strDeviceId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jsonObject.length() > 0) {
                    new getCartdata().execute(String.valueOf(jsonObject));
                }

            }
        });


//        listQunty.add("1");
//        listQunty.add("2");
//        listQunty.add("3");
//        listQunty.add("4");
//        listQunty.add("5");

        txtTitle.setText("Shopping Cart");

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("mode", "cartdetail");
            jsonObject.put("did", strDeviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getCartdata().execute(String.valueOf(jsonObject));
        }


    }

    private class getCartdata extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DashboardCartActivity.this);
            pd.setIndeterminate(false);
            pd.setMessage("Please Message");
            pd.setCancelable(false);
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

            if (pd.isShowing()) {
                pd.dismiss();
            }

            listItem.clear();
            try {
                JSONObject jsonObject = new JSONObject(strCartJsonResponse);
                Log.d(TAG, "cart response: " + jsonObject);

                String strMessage = jsonObject.getString("message");
                if (!strMessage.equals("No Data found")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String strModel = object.getString("model");
                        String strMpn = object.getString("mpn");
                        String strPrice = object.getString("price");
                        String strProductID = object.getString("product_id");
                        Log.d(TAG, "strProduct id: "+strProductID);
                        String[] strSplit = strPrice.split("\\.");
                        String values = strSplit[0];


                        CartPojo cartPojo = new CartPojo();

                        cartPojo.setStrModelName(strModel);
                        cartPojo.setStrMpn(strMpn);
                        cartPojo.setStrPrice(values);
                        cartPojo.setStrProduct_id(strProductID);
                        listItem.add(cartPojo);
                        listModelName.add(strModel);
                        listMpn.add(strMpn);
                        listPrice.add(values);
                        listProductId.add(strProductID);
                        listQunty.add("1");


                    }
                    for ( i=0; i<listPrice.size(); i++){
                        sum += Integer.parseInt(listPrice.get(i));
                    }
                    txtTotalPrice.setText("Rs "+String.valueOf(sum));

                    btnContinue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intentPayment=new Intent(DashboardCartActivity.this,CartLoginActivity.class);
                            intentPayment.putStringArrayListExtra("strProductName",listModelName);
                            intentPayment.putStringArrayListExtra("strProductId",listProductId);
                            intentPayment.putStringArrayListExtra("strQunty",listQunty);
                            intentPayment.putStringArrayListExtra("strPrice",listPrice);
                            intentPayment.putStringArrayListExtra("strBV",listMpn);
                            intentPayment.putExtra("strTotal",String.valueOf(sum));
                            startActivity(intentPayment);
                        }
                    });



                    mSwipeRefres.setRefreshing(false);

                    if (mListview != null) {
                        customAdapter = new CustomAdapter(getApplicationContext(), R.layout.dashboard_cart_item, listItem);
                        mListview.setAdapter(customAdapter);


                    } else {
                        Toast.makeText(DashboardCartActivity.this, "listView is null", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(DashboardCartActivity.this, "No item in Cart!!!", Toast.LENGTH_SHORT).show();
                    mSwipeRefres.setRefreshing(false);
                    mListview.removeFooterView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class CustomAdapter extends ArrayAdapter<CartPojo> implements ClickListner {


        List<CartPojo> items;
        Context context;
        int resource;


        public CustomAdapter(Context context, int resource, List<CartPojo> items) {
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


                holder.txtProductTitle = (TextView) convertView.findViewById(R.id.txtProductTitle);
                holder.txtProductPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
                holder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
                holder.txtTotalPrice = (TextView) convertView.findViewById(R.id.txtTotalPrice);
                holder.txtSubTotalPrice=(TextView)convertView.findViewById(R.id.txtSubTotalPrice) ;

                holder.txtShipping = (TextView) convertView.findViewById(R.id.txtShipping);
                holder.txtColor = (TextView) convertView.findViewById(R.id.txtColor);
                holder.txtSubTotal = (TextView) convertView.findViewById(R.id.txtSubTotal);
                holder.txtShippingPrice = (TextView) convertView.findViewById(R.id.txtShippingPrice);

//                holder.chkItem = (CheckBox) convertView.findViewById(R.id.chkItem);
                holder.txtRemove= (TextView) convertView.findViewById(R.id.txtRemove);
                holder.etQuantity= (EditText) convertView.findViewById(R.id.etQuantity);

//                holder.imgProduct = (SetImage) convertView.findViewById(R.id.imgProduct);
//                holder.btnBuy = (Button) convertView.findViewById(R.id.btnBuy);

                holder.mRelative_layout = (RelativeLayout) convertView.findViewById(R.id.mRelative_layout);
                convertView.setTag(holder);

            } else {


                holder = (ViewHolder) convertView.getTag();
            }

            final CartPojo cartPojo = items.get(position);

            holder.txtProductTitle.setText(cartPojo.getStrModelName());
            holder.txtProductPrice.setText("Rs " + cartPojo.getStrPrice());
            holder.txtTotalPrice.setText("Rs " + cartPojo.getStrPrice());
            holder.txtSubTotalPrice.setText("Rs "+cartPojo.getStrPrice());


            final ViewHolder finalHolder = holder;
            holder.etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                    String strQuantity=s.toString();
                    Log.d(TAG, "qunatitty of product: "+strQuantity);

                    if (!strQuantity.equals("")){
                        int total=Integer.parseInt(strQuantity)* Integer.parseInt(cartPojo.getStrPrice());
                        Log.d(TAG, "total price: "+total);
//                        holder=new ViewHolder();
                        finalHolder.txtProductPrice.setText("Rs "+String.valueOf(total));
                        finalHolder.txtTotalPrice.setText("Rs "+String.valueOf(total));
                        finalHolder.txtSubTotalPrice.setText("Rs "+String.valueOf(total));
                    }

                }
            });

            holder.txtRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("mode", "removeproduct");
                        jsonObject.put("did", com.toranado.bestafe.utils.StringUtils.getDeviceId(getApplicationContext()));
                        jsonObject.put("pid", cartPojo.getStrProduct_id());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jsonObject.length() > 0) {
                        new removeData().execute(String.valueOf(jsonObject));
//                        Intent intent = new Intent(DashboardCartActivity.this, ShowAllCartData.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                       startActivity(intent);

                    }
                }
            });






//            holder.mRelative_layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intentOrderConfirmationActivity = new Intent(DashboardCartActivity.this, OrderConfirmationActivity.class);
//                    intentOrderConfirmationActivity.putExtra("strName", cartPojo.getStrModelName());
//                    intentOrderConfirmationActivity.putExtra("strPrice", cartPojo.getStrPrice());
//                    intentOrderConfirmationActivity.putExtra("strBV", cartPojo.getStrMpn());
//                    intentOrderConfirmationActivity.putExtra("strProductId", cartPojo.getStrProduct_id());
//                    startActivity(intentOrderConfirmationActivity);
//                }
//            });

//            holder.btnBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intentOrderConfirmationActivity = new Intent(DashboardCartActivity.this, OrderConfirmationActivity.class);
//                    intentOrderConfirmationActivity.putExtra("strName", cartPojo.getStrModelName());
//                    intentOrderConfirmationActivity.putExtra("strPrice", cartPojo.getStrPrice());
//                    intentOrderConfirmationActivity.putExtra("strBV", cartPojo.getStrMpn());
//                    intentOrderConfirmationActivity.putExtra("strProductId", cartPojo.getStrProduct_id());
//                    startActivity(intentOrderConfirmationActivity);
//
//                }
//            });


            return convertView;

        }

        @Override
        public void setText(int position, TextView txtView, int values) {
            txtView.setText(String.valueOf(values));


        }
    }


    public class ViewHolder {

        CheckBox chkItem;
        ImageView imgProduct, imgPlus, imgMinus;
         public TextView txtRemove,txtSubTotalPrice, txtPlus, txtMinus, txtProductTitle, txtProductPrice, txtTotalPrice, txtQty, txtShipping, txtColor, txtSubTotal, txtShippingPrice, txtTotal;
        Button btnBuy;
        private EditText etQuantity;
        Spinner spinnerQty;
        RelativeLayout mRelative_layout;


    }

    private class removeData extends AsyncTask<String, String, String> {

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
                strRemoveResponse = buffer.toString();
                Log.i("response", strRemoveResponse);
                return strRemoveResponse;
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
                        Log.e("response", "Error closing stream", e);
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(strRemoveResponse);
                String strStatus = jsonObject.getString("status");
                String strMessagge = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                if (jsonArray != null) {
                    String strResponse = jsonArray.getString(0);
                    Toast.makeText(getApplicationContext(), strResponse, Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject();

                    try {

                        object.put("mode", "cartdetail");
                        object.put("did", strDeviceId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (jsonObject.length() > 0) {
                        new getCartdata().execute(String.valueOf(object));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No record found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
