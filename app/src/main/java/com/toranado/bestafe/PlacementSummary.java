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

public class PlacementSummary extends AppCompatActivity {

    String strMemberID = "", strJsonResponse = "", strMessagge = "", strResult = "", strLevelNO = "", strCount = "", strSum = "";

    private static final String TAG = PlacementSummary.class.getSimpleName();
    ProgressDialog pd;
    ListView listview_level_summary;

    ArrayList<String>listLevelNo=new ArrayList<>();
    ArrayList<String>listCount=new ArrayList<>();
    ArrayList<String>listSum=new ArrayList<>();
    ArrayList<String>listDetails=new ArrayList<>();
    private Toolbar toolbar_levelSummary;
    private TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement_summary);
        getSupportActionBar().hide();

        toolbar_levelSummary=(Toolbar)findViewById(R.id.toolbar_levelSummary);
        txtTitle=(TextView)findViewById(R.id.txtTitle);
        txtTitle.setText("Placement Summary");

        toolbar_levelSummary.setNavigationIcon(R.drawable.arrowleft);
        toolbar_levelSummary.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        strMemberID = intent.getStringExtra("memberid");
        Log.d(TAG, "strMemberId: "+strMemberID);



        listview_level_summary=(ListView)findViewById(R.id.listview_level_summary);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.header_level_summary,null);
        listview_level_summary.addHeaderView(view);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mode", "placementsummry");
            jsonObject.put("memberid", strMemberID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getBinaryLevelIncome().execute(String.valueOf(jsonObject));
        }

    }

    private class getBinaryLevelIncome extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PlacementSummary.this);
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.setMessage("Please Wait....");
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
                if (!strMessagge.equals("No Data found")){
                    JSONArray array = jsonObject.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        strLevelNO = object.getString("levelno");
                        strCount = object.getString("total");
//                        strSum = object.getString("sum(pv)");

                        listLevelNo.add(strLevelNO);
                        listCount.add(strCount);
//                        listSum.add(strSum);
                        listDetails.add("Detail");


                    }

                    if (listview_level_summary!=null){
                        CustomAdapter customAdapter = new CustomAdapter(PlacementSummary.this, R.layout.listview_item_level_summary, listDetails);
                        listview_level_summary.setAdapter(customAdapter);

                    }else {
                        Toast.makeText(PlacementSummary.this, "listview null", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), strMessagge, Toast.LENGTH_SHORT).show();
                    if (listview_level_summary!=null){
                        CustomAdapter customAdapter = new CustomAdapter(PlacementSummary.this, R.layout.listview_item_level_summary, listDetails);
                        listview_level_summary.setAdapter(customAdapter);

                    }else {
                        Toast.makeText(PlacementSummary.this, "listview null", Toast.LENGTH_SHORT).show();
                    }
                }
//

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
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(resource, null);




                holder.txtDetail = (TextView) convertView.findViewById(R.id.txtDetail);
                holder.txtLevelNo = (TextView) convertView.findViewById(R.id.txtLevelNo);
                holder.txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);
                convertView.setTag(holder);


            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtDetail.setText(items.get(position));
            holder.txtLevelNo.setText(listLevelNo.get(position));
            holder.txtTotal.setText(listCount.get(position));

            holder.txtDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PlacementSummary.this,PlacementSummaryClick.class);
                    intent.putExtra("strLevelNo",listLevelNo.get(position));
                    startActivity(intent);
                }
            });


            return convertView;

        }
    }

    private class ViewHolder {

        TextView txtDetail, txtLevelNo, txtTotal;
    }

}
