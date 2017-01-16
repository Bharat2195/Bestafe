package com.toranado.bestafe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.toranado.bestafe.interfaces.DocumentDetail;
import com.toranado.bestafe.interfaces.PriceUpdate;
import com.toranado.bestafe.utils.Constant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DocumentListActivity extends AppCompatActivity implements PriceUpdate{

    String strMemberID = "", strJsonResponse = "",strDocumentResponse="";
    private static final String TAG = DocumentListActivity.class.getSimpleName();
    ArrayList<String> listId= new ArrayList<>();
    ArrayList<String> listName= new ArrayList<>();
    ProgressDialog pd;
    ListView document_listview;
    private Toolbar toolbar_document;
    private TextView txtTitle;
    private static int RESULT_LOAD_IMAGE = 1;
    private Bitmap bitmap;
    private Uri filePath;
    PriceUpdate priceUpdate;
    private Button btnUpload;
    ArrayList<String> listDocumentUpload=new ArrayList<>();
    ViewHolder holder = null;

    ViewHolder viewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        getSupportActionBar().hide();

        document_listview= (ListView) findViewById(R.id.document_listview);

        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.header_document_item,null);
        document_listview.addHeaderView(view);

        toolbar_document= (Toolbar) findViewById(R.id.toolbar_document);
        txtTitle= (TextView) toolbar_document.findViewById(R.id.txtTitle);

        txtTitle.setText("Document List");
        btnUpload= (Button) findViewById(R.id.btnUpload);

        toolbar_document.setNavigationIcon(R.drawable.arrowleft);
        toolbar_document.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mode", "kycdoc");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject.length() > 0) {
            new getDocumentData().execute(String.valueOf(jsonObject));
        }


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (listDocumentUpload!=null){

                    for (int i=0; i<listDocumentUpload.size();i++){

                        JSONObject object=new JSONObject();
                        try{
                            object.put("mode","kycdocupload");
                            object.put("memberid",LoginMainActivity.strId);
                            object.put("docid",listId.get(i));
                            object.put("basetring",listDocumentUpload.get(i));


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if (object.length()>0){
                            new submitDocument().execute(String.valueOf(object));
                        }
                    }

                }else {
                    Toast.makeText(DocumentListActivity.this, "Please Select document", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPriceUpdate(ImageView imageView) {


    }
    private class getDocumentData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DocumentListActivity.this);
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
                    String strId= responseObject.getString("id");
                    String strName= responseObject.getString("name");

                    listId.add(strId);
                    listName.add(strName);

                }

                if (document_listview!=null){
                    CustomAdapter customAdapter = new CustomAdapter(DocumentListActivity.this, R.layout.list_item_document, listId);
                    document_listview.setAdapter(customAdapter);
                }else {
                    Toast.makeText(DocumentListActivity.this, "Lisview null", Toast.LENGTH_SHORT).show();
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


            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(resource, null);

                holder.txtSrNo = (TextView) convertView.findViewById(R.id.txtSrNo);
                holder.txtDocumentName = (TextView) convertView.findViewById(R.id.txtDocumentName);
                holder.btnChooseFile = (Button) convertView.findViewById(R.id.btnChooseFile);
                holder.txtView = (TextView) convertView.findViewById(R.id.txtView);
                holder.imgView= (ImageView) convertView.findViewById(R.id.imgView);
                holder.txtName=(TextView)convertView.findViewById(R.id.txtChossenName);

                convertView.setTag(holder);
            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtSrNo.setText(items.get(position));
            holder.txtDocumentName.setText(listName.get(position));

            holder.btnChooseFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                     startActivityForResult(i, RESULT_LOAD_IMAGE);

                }
            });

            holder.txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.txtView.setVisibility(View.GONE);
                    holder.imgView.setVisibility(View.VISIBLE);
                    holder.imgView.setImageBitmap(bitmap);
                }
            });

//            priceUpdate.onPriceUpdate(holder.imgView);
//            if (bitmap!=null){
//                holder.txtView.setVisibility(View.GONE);
//                holder.imgView.setVisibility(View.VISIBLE);
//                holder.imgView.setImageBitmap(bitmap);
//                holder.txtName.setText(bitmap.toString());
//            }

            return convertView;
        }
    }

    private void onDataSet(ImageView imgView) {
        imgView.setVisibility(View.VISIBLE);
        imgView.setImageBitmap(bitmap);
    }

    private class ViewHolder {

        TextView txtSrNo, txtDocumentName,txtView,txtName;
        private Button btnChooseFile;
        private ImageView imgView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode ==RESULT_OK && null != data) {

            filePath=data.getData();

            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                Log.d(TAG, "bitmap path: "+bitmap.toString());
                listDocumentUpload.add(encodedString(bitmap));


                viewHolder=new ViewHolder();
                viewHolder.txtName=(TextView)findViewById(R.id.txtChossenName);
                viewHolder.txtView=(TextView)findViewById(R.id.txtView);
                viewHolder.imgView= (ImageView) findViewById(R.id.imgView);
                viewHolder.txtName.setText(filePath.toString());

                viewHolder.txtView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.txtView.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "txt Cliked", Toast.LENGTH_SHORT).show();
                        viewHolder.imgView.setVisibility(View.VISIBLE);
                        viewHolder.imgView.setImageBitmap(bitmap);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private String encodedString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d(TAG, "encoded image: "+encodedImage);
        return encodedImage;
    }

    private class submitDocument extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DocumentListActivity.this);
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
                strDocumentResponse = buffer.toString();
                Log.i(TAG, strDocumentResponse);
                //send to post execute
                return strDocumentResponse;
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
                JSONObject jsonObject = new JSONObject(strDocumentResponse);
                String strStatus = jsonObject.getString("status");
                String strMessage= jsonObject.getString("message");
                Toast.makeText(DocumentListActivity.this, strMessage, Toast.LENGTH_SHORT).show();
//                JSONArray array = jsonObject.getJSONArray("response");
//                for (int i = 0; i < array.length(); i++) {
//                    JSONObject responseObject = array.getJSONObject(i);
//
//
//                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
