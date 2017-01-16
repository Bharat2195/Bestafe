package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderHistoryActivity extends AppCompatActivity {

    private static final String TAG=OrderHistoryActivity.class.getSimpleName();
    private Toolbar toolbar_order_history;
    private TextView txtTitle;
    ListView listview_order_history;
    private ProgressDialog pd;
    private String strJsonResponse="",strOrderResponse="",strSplitDate="";
    private ArrayList<String> listOrderId=new ArrayList<>();
    private ArrayList<String> listOrderProductId=new ArrayList<>();
    private ArrayList<String> listProductId=new ArrayList<>();
    private ArrayList<String> listName=new ArrayList<>();
    private ArrayList<String> listModel=new ArrayList<>();
    private ArrayList<String> listQuntity=new ArrayList<>();
    private ArrayList<String> listPrice=new ArrayList<>();
    private ArrayList<String> listOrderDate=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getSupportActionBar().hide();
        toolbar_order_history=(Toolbar)findViewById(R.id.toolbar_order_history);
        txtTitle=(TextView)toolbar_order_history.findViewById(R.id.txtTitle);
        txtTitle.setText("Order History");
        toolbar_order_history.setNavigationIcon(R.drawable.arrowleft);
        toolbar_order_history.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listview_order_history=(ListView)findViewById(R.id.listview_order_history);

        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("mode","orderhis");
            jsonObject.put("memberId",LoginMainActivity.strId);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (jsonObject.length()>0){
            new getHistory().execute(String.valueOf(jsonObject));
        }
    }

    private class getHistory extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(OrderHistoryActivity.this);
            pd.setIndeterminate(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Please Wait...");
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
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(strJsonResponse);
                String strStatus = jsonObject.getString("status");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i=0; i< jsonArray.length(); i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    String strOrderId=object.getString("order_id");
                    Log.d(TAG, "order id: "+strOrderId);
                    String strDate=object.getString("date_added");
                    String [] splitDate=strDate.split(" ");
                    strSplitDate=splitDate[0];
                    Log.d(TAG, "split date: "+strSplitDate);
                    listOrderId.add(strOrderId);
                    listOrderDate.add(strSplitDate);
                }

                Log.d(TAG, "list date length: "+listOrderDate.size());

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "list order id length:"+listOrderId.size());

            for (int j=0; j< listOrderId.size(); j++){
                JSONObject jsonObject=new JSONObject();
                try{
                    jsonObject.put("mode","orderhisdetail");
                    jsonObject.put("orderid",listOrderId.get(j));
                }catch (Exception e){
                    e.printStackTrace();
                }

                if (jsonObject.length()>0){
                    new getDetails().execute(String.valueOf(jsonObject));
                }
            }

        }
    }

    private class getDetails extends AsyncTask<String,String,String> {

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
                strOrderResponse = buffer.toString();
                Log.i(TAG, strOrderResponse);
                //send to post execute
                return strOrderResponse;
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

            return strOrderResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(strOrderResponse);
                String strStatus = jsonObject.getString("status");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String strorderProductId=object.getString("order_product_id");
                    String strOrderId=object.getString("order_id");
                    String strProductId=object.getString("product_id");
                    String strName=object.getString("name");
                    String strModel=object.getString("model");
                    String strQuntity=object.getString("quantity");
                    String strPrice=object.getString("price");
                    String strTotal=object.getString("total");
                    String strTax=object.getString("tax");
                    String strReward=object.getString("reward");

                    listOrderProductId.add(strorderProductId);
                    listModel.add(strModel);
                    listName.add(strName);
                    listPrice.add(strPrice);
                    listQuntity.add(strQuntity);
                    listProductId.add(strProductId);



                }

                Log.d(TAG, "listProduct id size: "+listOrderProductId.size());


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (listview_order_history!=null){

                CustomAdapter customAdapter = new CustomAdapter(OrderHistoryActivity.this, R.layout.litsview_item_order_history, listOrderProductId);
                listview_order_history.setAdapter(customAdapter);

            }else {
                Toast.makeText(OrderHistoryActivity.this, "Listview is null", Toast.LENGTH_SHORT).show();

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
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(resource, null);

                holder.txtOrderNo = (TextView) convertView.findViewById(R.id.txtOrderNo);
                holder.txtPrice = (TextView) convertView.findViewById(R.id.txtProductPrice);
//                holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
                holder.txtModelName = (TextView) convertView.findViewById(R.id.txtModelName);

                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtOrderNo.setText("Order No "+items.get(position));
            holder.txtPrice.setText(getResources().getString(R.string.Rs)+listPrice.get(position));
//            holder.txtDate.setText(listOrderDate.get(position));
            holder.txtName.setText(listName.get(position));
            holder.txtModelName.setText(listModel.get(position));


            return convertView;

        }
    }
    private class ViewHolder {

        TextView txtOrderNo, txtPrice, txtDate,txtName,txtModelName;
    }
}
