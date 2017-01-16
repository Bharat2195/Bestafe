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

public class SponsorSummaryClickActivity extends AppCompatActivity {

    private static final String TAG=SponsorSummaryClickActivity.class.getSimpleName();

    String strMemberID = "", strJsonResponse = "",strLevelNo="";
    ArrayList<String> listEntryDate= new ArrayList<>();
    ArrayList<String> listMemberId= new ArrayList<>();
    ArrayList<String> listMemberName= new ArrayList<>();
    ArrayList<String> listEmail= new ArrayList<>();
    ArrayList<String> listMobile= new ArrayList<>();
    ArrayList<String> listLevelNo= new ArrayList<>();
    ProgressDialog pd;
    ListView listview_sponsor_summary_detail;
    private TextView txtTitle;
    private Toolbar toolbar_sponsor_summary_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_summary_click);

        getSupportActionBar().hide();

        toolbar_sponsor_summary_detail= (Toolbar) findViewById(R.id.toolbar_sponsor_summary_detail);
        txtTitle= (TextView) toolbar_sponsor_summary_detail.findViewById(R.id.txtTitle);

        Intent intent=getIntent();
        strLevelNo=intent.getStringExtra("strLevelNo");
        Log.d(TAG, "level summary click level no: "+strLevelNo);

        txtTitle.setText("Detail Of Level -"+strLevelNo);

        toolbar_sponsor_summary_detail.setNavigationIcon(R.drawable.arrowleft);
        toolbar_sponsor_summary_detail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        listview_sponsor_summary_detail= (ListView) findViewById(R.id.listview_sponsor_summary_detail);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.header_placement_summary_item,null);
        listview_sponsor_summary_detail.addHeaderView(view);

//        Intent intent = getIntent();
        strMemberID =LoginMainActivity.strId;
        Log.d(TAG, "withdrawal memberid: "+strMemberID);
        JsonResponse();
    }

    private void JsonResponse() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mode", "sponsordetailclick");
            jsonObject.put("memberid",strMemberID);
            jsonObject.put("levelno",strLevelNo);

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
            pd = new ProgressDialog(SponsorSummaryClickActivity.this);
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
                        String strEntryDate= responseObject.getString("entrydate");
                        String strLevelNo= responseObject.getString("levelno");
                        String strMemberId= responseObject.getString("memberid");
                        String strMemberName= responseObject.getString("mn");
                        String strEmail= responseObject.getString("email");
                        String strMobile= responseObject.getString("mobile");


                        listEntryDate.add(strEntryDate);
                        listMemberId.add(strMemberId);
                        listMemberName.add(strMemberName);
                        listEmail.add(strEmail);
                        listMobile.add(strMobile);
                        listLevelNo.add(strLevelNo);
                    }

                    if (listview_sponsor_summary_detail!=null){
                        CustomAdapter customAdapter = new CustomAdapter(SponsorSummaryClickActivity.this, R.layout.list_item_placement_summary, listEntryDate);
                        listview_sponsor_summary_detail.setAdapter(customAdapter);
                    }else {
                        Toast.makeText(SponsorSummaryClickActivity.this, "Lisview null", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SponsorSummaryClickActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    if (listview_sponsor_summary_detail!=null){
                        CustomAdapter customAdapter = new CustomAdapter(SponsorSummaryClickActivity.this, R.layout.list_item_placement_summary, listEntryDate);
                        listview_sponsor_summary_detail.setAdapter(customAdapter);
                    }else {
                        Toast.makeText(SponsorSummaryClickActivity.this, "Lisview null", Toast.LENGTH_SHORT).show();
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




                holder.txtEntryDate = (TextView) convertView.findViewById(R.id.txtEntryDate);
                holder.txtMemberID = (TextView) convertView.findViewById(R.id.txtMemberID);
                holder.txtMemberName = (TextView) convertView.findViewById(R.id.txtMemberName);
                holder.txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
                holder.txtMobile = (TextView) convertView.findViewById(R.id.txtMobile);
                holder.txtlevel = (TextView) convertView.findViewById(R.id.txtlevel);

                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtEntryDate.setText(items.get(position));
            holder.txtMemberID.setText(listMemberId.get(position));
            holder.txtMemberName.setText(listMemberName.get(position));
            holder.txtEmail.setText(listEmail.get(position));
            holder.txtMobile.setText(listMobile.get(position));
            holder.txtlevel.setText(listLevelNo.get(position));


            return convertView;

        }
    }

    private class ViewHolder {

        TextView txtEntryDate, txtMemberID, txtMemberName, txtEmail,txtMobile,txtlevel;
    }


}
