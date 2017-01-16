package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class SponsorListReport extends AppCompatActivity {

    String strMemberID = "", strJsonResponse = "";
    private static final String TAG = SponsorListReport.class.getSimpleName();
    ArrayList<String> listSrNo = new ArrayList<>();
    ArrayList<String> listMemberID = new ArrayList<>();
    ArrayList<String> listMemberName = new ArrayList<>();
    ArrayList<String> listMobile = new ArrayList<>();
    ArrayList<String> listJoiningDate = new ArrayList<>();
    ArrayList<String> listActivationDate = new ArrayList<>();
    ProgressDialog pd;
    ListView sponsorlist_listview;
    private Toolbar toolbar_sponsor_list;
    private TextView txtTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_list_report);
        getSupportActionBar().hide();

        sponsorlist_listview= (ListView) findViewById(R.id.sponsorlist_listview);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.header_sponsor_list_item,null);
        sponsorlist_listview.addHeaderView(view);

        toolbar_sponsor_list= (Toolbar) findViewById(R.id.toolbar_sponsor_list);
        txtTitle= (TextView) toolbar_sponsor_list.findViewById(R.id.txtTitle);

        txtTitle.setText("Sponsor List");

        toolbar_sponsor_list.setNavigationIcon(R.drawable.arrowleft);
        toolbar_sponsor_list.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




//        listSrNo.add("Sr.No");
//        listMemberID.add("Member ID");
//        listMemberName.add("Member Name");
//        listMobile.add("Mobile");
//        listJoiningDate.add("Joining Date");
//        listActivationDate.add("Activation Date");

        Intent intent = getIntent();
        strMemberID = intent.getStringExtra("memberid");
        Log.d(TAG, "sponsor list memberid: "+strMemberID);
        JsonResponse();


    }


    private void JsonResponse() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mode", "sponsorlist");
            jsonObject.put("memberid", strMemberID);

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
            pd = new ProgressDialog(SponsorListReport.this);
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
                strJsonResponse = jsonObject.getString("message");
                JSONArray array = jsonObject.getJSONArray("response");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject responseObject = array.getJSONObject(i);
                    String strSrNo = responseObject.getString("id");
                    String strMemberName = responseObject.getString("membername");
                    String strMobile = responseObject.getString("mobile");
                    String strJoiningDate = responseObject.getString("entrydate1");
                    String strActivationDate = responseObject.getString("activationdate1");
                    String strMemberID = responseObject.getString("memberid");

                    listActivationDate.add(strActivationDate);
                    listSrNo.add(String.valueOf(i+1));
                    listJoiningDate.add(strJoiningDate);
                    listMemberName.add(strMemberName);
                    listMobile.add(strMobile);
                    listMemberID.add(strMemberID);
                }

                if (sponsorlist_listview!=null){
                    CustomAdapter customAdapter = new CustomAdapter(SponsorListReport.this, R.layout.listview_item_sponsorlist, listSrNo);
                    sponsorlist_listview.setAdapter(customAdapter);
                }else {
                    Toast.makeText(SponsorListReport.this, "Lisview null", Toast.LENGTH_SHORT).show();
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
                holder.txtMemberID = (TextView) convertView.findViewById(R.id.txtMemberID);
                holder.txtMemberName = (TextView) convertView.findViewById(R.id.txtMemberName);
                holder.txtMobile = (TextView) convertView.findViewById(R.id.txtMobile);
                holder.txtJoiningDate = (TextView) convertView.findViewById(R.id.txtJoiningDate);
                holder.txtActivationDate = (TextView) convertView.findViewById(R.id.txtActivationDate);

                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtSrNo.setText(items.get(position));
            holder.txtMemberID.setText(listMemberID.get(position));
            holder.txtMemberName.setText(listMemberName.get(position));
            holder.txtMobile.setText(listMobile.get(position));
            holder.txtJoiningDate.setText(listJoiningDate.get(position));
            holder.txtActivationDate.setText(listActivationDate.get(position));
//            Log.d(TAG, "getViewPosition: " + position);


            return convertView;

        }
    }
    private class ViewHolder {

        TextView txtSrNo, txtMemberID, txtMemberName, txtMobile, txtJoiningDate, txtActivationDate;
    }


}
