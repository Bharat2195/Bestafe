package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class LevelIncome extends AppCompatActivity {
    Toolbar toolbar_levelIncome;
    String strMemberid = "", strJsonResponse = "", strId = "", strEntryDate = "", status = "",strTotalValues="",
            strMessagge = "", strMemberId = "", strFromID = "", strLevelno = "", strPv = "", strAmount = "", strOrderID = "", strOrderPv = "", strMemberName = "";
    private static final String TAG = LevelIncome.class.getSimpleName();
    ArrayList<String> listSrNo = new ArrayList<>();
    ArrayList<String> listEntrydate = new ArrayList<>();
    ArrayList<String> listFromId = new ArrayList<>();
    ArrayList<String> listFromName = new ArrayList<>();
    ArrayList<String> listPV = new ArrayList<>();
    ArrayList<String> listLevelNo=new ArrayList<>();
    ArrayList<String> listStatus=new ArrayList<>();
    ArrayList<String> listAmount=new ArrayList<>();
    ArrayList<String> listOrderNo = new ArrayList<>();
    ProgressDialog pd;
    ListView listview_levelIncome;
    private TextView txtTitle,txtTotalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_income);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        listview_levelIncome= (ListView) findViewById(R.id.listview_levelIncome);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.header_level_income,null);
        listview_levelIncome.addHeaderView(view);
        View view1=inflater.inflate(R.layout.footer_level_income,null);
        txtTotalAmount=(TextView)view1.findViewById(R.id.txtTotalAmount);
        listview_levelIncome.addFooterView(view1);
        Intent intent = getIntent();
        strMemberid = intent.getStringExtra("memberid");

        toolbar_levelIncome = (Toolbar) findViewById(R.id.toolbar_levelIncome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_levelIncome.setNavigationIcon(R.drawable.arrowleft);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar_levelIncome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txtTitle= (TextView) toolbar_levelIncome.findViewById(R.id.txtTitle);
        txtTitle.setText("Level Income");
        getJson();
    }

    private void getJson() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "levelincome");
            jsonObject.put("memberid", strMemberid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new PostData().execute(String.valueOf(jsonObject));
        }
    }


    private class PostData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LevelIncome.this);
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
                strMessagge = jsonObject.getString("message");
//                Toast.makeText(getApplicationContext(), strMessagge, Toast.LENGTH_SHORT).show();
                String strResult = jsonObject.getString("result");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    strId = object.getString("id");
                    Log.d(TAG, "ID Level income:" + strId);
                    strEntryDate = object.getString("entrydate");
                    Log.d(TAG, "Enrtydata is:" + strEntryDate);
                    strMemberId = object.getString("memberid");
                    Log.d(TAG, "Memberid data:" + strMemberId);
                    strFromID = object.getString("fromid");
                    Log.d(TAG, "From id data:" + strFromID);
                    strLevelno = object.getString("levelno");
                    Log.d(TAG, "Levelno data:" + strLevelno);
                    strPv = object.getString("pv");
                    Log.d(TAG, "Pv data:" + strPv);
                    strAmount = object.getString("amount");
                    Log.d(TAG, "Amount data:" + strAmount);
                    strOrderPv = object.getString("orderpv");
                    Log.d(TAG, "Order PV data:" + strOrderPv);
                    strOrderID = object.getString("orderid");
                    Log.d(TAG, "Order Id data:" + strOrderID);
                    status = object.getString("status");
                    Log.d(TAG, "Status data:" + status);
                    strMemberName = object.getString("membername");
                    Log.d(TAG, "MemberName data:" + strMemberName);

                    listSrNo.add(strId);
                    listEntrydate.add(strEntryDate);
                    listFromId.add(strFromID);
                    listFromName.add(strMemberName);
                    listPV.add(strPv);
                    listLevelNo.add(strLevelno);
                    listStatus.add(status);
                    listAmount.add(strAmount);
                    listOrderNo.add(strOrderID);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (listview_levelIncome!=null){

                CustomAdapter customAdapter = new CustomAdapter(LevelIncome.this, R.layout.listview_item_level_income, listSrNo);
                listview_levelIncome.setAdapter(customAdapter);

            }else {
                Toast.makeText(LevelIncome.this, "Listview is null", Toast.LENGTH_SHORT).show();

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

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(resource, null);

                holder.txtSrNo = (TextView) convertView.findViewById(R.id.txtSrNo);
                holder.txtEntryDate = (TextView) convertView.findViewById(R.id.txtEntryDate);
                holder.txtFromId = (TextView) convertView.findViewById(R.id.txtFromId);
                holder.txtFromName = (TextView) convertView.findViewById(R.id.txtFromName);
                holder.txtLevelNo = (TextView) convertView.findViewById(R.id.txtLevelNo);
                holder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
                holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
                holder.txtOrderNo = (TextView) convertView.findViewById(R.id.txtOrderNo);

                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtSrNo.setText(items.get(position));
            holder.txtEntryDate.setText(listEntrydate.get(position));
            holder.txtFromId.setText(listEntrydate.get(position));
            holder.txtFromName.setText(listFromName.get(position));
            holder.txtLevelNo.setText(listLevelNo.get(position));
            holder.txtStatus.setText(listStatus.get(position));
            holder.txtAmount.setText(listAmount.get(position));
            holder.txtOrderNo.setText(listOrderNo.get(position));

            int sum=0;
            for (int i=0; i<listAmount.size(); i++) {
                sum += Integer.parseInt(listAmount.get(i));
            }

                txtTotalAmount.setText(String.valueOf(sum));

            return convertView;

        }
    }

    private class ViewHolder {

        TextView txtSrNo, txtEntryDate, txtFromId,txtFromName,txtLevelNo,txtStatus,txtAmount,txtOrderNo;
    }

}
