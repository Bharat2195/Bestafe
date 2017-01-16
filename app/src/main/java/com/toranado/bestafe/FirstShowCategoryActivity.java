package com.toranado.bestafe;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.adpter.AllCategoryFragment;
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

public class FirstShowCategoryActivity extends AppCompatActivity {

    private static final String TAG=FirstShowCategoryActivity.class.getSimpleName();

    TabLayout tabLayout;
    private Toolbar toolbar_show_category;
    private ViewPager viewPager;
    private TextView txtTitle,txtNo;
    private ImageView imgCart,imgCartIcon;
    private String strDeviceId="",strCartJsonResponse="";

    private ArrayList<String>listCartModel=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_show_category);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar_show_category = (Toolbar) findViewById(R.id.toolbar_show_category);
        txtTitle = (TextView) toolbar_show_category.findViewById(R.id.txtTitle);
        txtTitle.setText("Category");
        imgCart=(ImageView)toolbar_show_category.findViewById(R.id.imgCart);
        imgCartIcon=(ImageView)toolbar_show_category.findViewById(R.id.imgCartIcon);
        txtNo=(TextView)toolbar_show_category.findViewById(R.id.txtNo);
        toolbar_show_category.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar_show_category.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        strDeviceId= com.toranado.bestafe.utils.StringUtils.getDeviceId(FirstShowCategoryActivity.this);
        Log.d(TAG, "category device id: "+strDeviceId);


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

        imgCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgCartIcon.getVisibility()==View.VISIBLE){
                    Intent intent=new Intent(FirstShowCategoryActivity.this,DashboardCartActivity.class);
                    startActivity(intent);
//                    Intent intent=new Intent(FirstShowCategoryActivity.this, TestCartActivity.class);
//                    startActivity(intent);
                }else {
                    Toast.makeText(FirstShowCategoryActivity.this, "No item in cart", Toast.LENGTH_SHORT).show();
                }
            }
        });





        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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
            try {
                JSONObject jsonObject = new JSONObject(strCartJsonResponse);
                Log.d(TAG, "cart response: "+jsonObject);

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
                        String strSize=String.valueOf(listCartModel.size());
                        txtNo.setText(strSize);

//                        String[] strSplit = strPrice.split("\\.");
//                        String values = strSplit[0];inte
//                        String strMpn=object.getString("mpn");

                    }


                } else {
                    txtNo.setVisibility(View.GONE);
                    imgCartIcon.setVisibility(View.GONE);
//                    txtNo.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllCategoryFragment(), "All Category");
        adapter.addFragment(new JustForYouFragment(), "JUST FOR YOU");


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
