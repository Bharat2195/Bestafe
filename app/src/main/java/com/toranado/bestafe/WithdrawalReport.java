package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class WithdrawalReport extends AppCompatActivity {

    String strMemberID = "", strJsonResponse = "";
    private static final String TAG = WithdrawalReport.class.getSimpleName();
    ArrayList<String> listSrNo = new ArrayList<>();
    ArrayList<String> listEntryDate= new ArrayList<>();
    ArrayList<String> listAmount= new ArrayList<>();
    ArrayList<String> listTDS= new ArrayList<>();
    ArrayList<String> listProcessingFees= new ArrayList<>();
    ArrayList<String> listPaybleAmount= new ArrayList<>();
    ArrayList<String> listStatus= new ArrayList<>();
    ProgressDialog pd;
    ListView listview_withdrawal;
    private TextView txtTitle;
    private Toolbar toolbar_withdrawal;
    private Button btnAddNewEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_report);

        getSupportActionBar().hide();

        toolbar_withdrawal= (Toolbar) findViewById(R.id.toolbar_withdrawal);
        txtTitle= (TextView) toolbar_withdrawal.findViewById(R.id.txtTitle);

        txtTitle.setText("Withdrawal Report");


        toolbar_withdrawal.setNavigationIcon(R.drawable.arrowleft);
        toolbar_withdrawal.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnAddNewEntry= (Button) findViewById(R.id.btnAddNewEntry);

        btnAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd=new Intent(WithdrawalReport.this,AddNewEntryActivity.class);
                startActivity(intentAdd);
            }
        });





        listview_withdrawal= (ListView) findViewById(R.id.listview_withdrawal);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.header_withdrawal_item,null);
        listview_withdrawal.addHeaderView(view);

//        Intent intent = getIntent();
        strMemberID =LoginMainActivity.strId;
        Log.d(TAG, "withdrawal memberid: "+strMemberID);
        JsonResponse();
    }
    private void JsonResponse() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mode", "withdrawreport");
            jsonObject.put("memberid",strMemberID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getReportData().execute(String.valueOf(jsonObject));
        }

    }

    private class getReportData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(WithdrawalReport.this);
            pd.setIndeterminate(false);
            pd.setMessage("Please Wait....");
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
                String strMessage = jsonObject.getString("message");
                if (!strMessage.equals("No Data found")){
                    JSONArray array = jsonObject.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject responseObject = array.getJSONObject(i);
                        String strMemberId= responseObject.getString("memberid");
                        String strAmount= responseObject.getString("amount");
                        String strPayableAmount= responseObject.getString("payableamount");
                        String strTDS= responseObject.getString("tds");
                        String strAdminCharge= responseObject.getString("admin_charge");
                        String strEntryDate=responseObject.getString("entry_date");
                        String status=responseObject.getString("status");
                        String strEntryNo=responseObject.getString("entryno");


                        listSrNo.add(String.valueOf(i));
                        listEntryDate.add(strEntryDate);
                        listAmount.add(strAmount);
                        listTDS.add(strTDS);
                        listProcessingFees.add(strAdminCharge);
                        listStatus.add(status);
                        listPaybleAmount.add(strPayableAmount);
                    }

                    if (listview_withdrawal!=null){
                        CustomAdapter customAdapter = new CustomAdapter(WithdrawalReport.this, R.layout.listview_item_withdrawal, listSrNo);
                        listview_withdrawal.setAdapter(customAdapter);
                    }else {
                        Toast.makeText(WithdrawalReport.this, "Lisview null", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(WithdrawalReport.this, "No data found", Toast.LENGTH_SHORT).show();
                    if (listview_withdrawal!=null){
                        CustomAdapter customAdapter = new CustomAdapter(WithdrawalReport.this, R.layout.listview_item_withdrawal, listSrNo);
                        listview_withdrawal.setAdapter(customAdapter);
                    }else {
                        Toast.makeText(WithdrawalReport.this, "Lisview null", Toast.LENGTH_SHORT).show();
                    }
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
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(resource, null);




                holder.txtSrNo = (TextView) convertView.findViewById(R.id.txtSrNo);
                holder.txtEntryDate = (TextView) convertView.findViewById(R.id.txtEntryDate);
                holder.txtAmount = (TextView) convertView.findViewById(R.id.txtAmount);
                holder.txtTDS = (TextView) convertView.findViewById(R.id.txtTDS);
                holder.txtProcessingFees = (TextView) convertView.findViewById(R.id.txtProcessingFees);
                holder.txtPayableAmount = (TextView) convertView.findViewById(R.id.txtPayableAmount);
                holder.txtStatus=(TextView)convertView.findViewById(R.id.txtStatus);

                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtSrNo.setText(items.get(position));
            holder.txtEntryDate.setText(listEntryDate.get(position));
            holder.txtAmount.setText(listAmount.get(position));
            holder.txtTDS.setText(listTDS.get(position));
            holder.txtProcessingFees.setText(listProcessingFees.get(position));
            holder.txtPayableAmount.setText(listPaybleAmount.get(position));
            holder.txtStatus.setText(listStatus.get(position));


            return convertView;

        }
    }

    private class ViewHolder {

        TextView txtSrNo, txtEntryDate, txtAmount, txtTDS, txtProcessingFees, txtPayableAmount,txtStatus;
    }



}
