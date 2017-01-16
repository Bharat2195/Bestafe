package com.toranado.bestafe.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toranado.bestafe.DashBoardProductDescriptionActivity;
import com.toranado.bestafe.R;
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

/**
 * Created by cphp on 21-Sep-16.
 */
public class DashBoardCategoryAdapter extends RecyclerView.Adapter<DashBoardCategoryAdapter.ViewHolder> {


    ArrayList<String> alName;
    ArrayList<String> alImage;
    ArrayList<String> listPid;
    ArrayList<String> listPrice;
    ArrayList<String> listMpn;
    Context context;
    static Bitmap bitmap;
    Uri FilePath;
    private String strSpecialPriceResponse="";
    private static final String TAG=DashBoardCategoryAdapter.class.getSimpleName();
    ArrayList<String> listSpecialPrice=new ArrayList<>();


    public  DashBoardCategoryAdapter(Context context, ArrayList<String> alName, ArrayList<String> alImage,ArrayList<String>listPid,ArrayList<String>listPrice,ArrayList<String>listMpn) {
        super();
        this.context = context;
        this.alName = alName;
        this.alImage = alImage;
        this.listPid=listPid;
        this.listPrice=listPrice;
        this.listMpn=listMpn;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtItemName.setText(alName.get(i));
        final String strItemName=alName.get(i);
        final String strImagePath = alImage.get(i);
        final String strPid=listPid.get(i);
        final String strPrice=listPrice.get(i);
        final String strMpn=listMpn.get(i);

        Glide.with(context).load(strImagePath).placeholder(R.drawable.ic_default).into(viewHolder.imgThumbnail);

        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("mode", "getspecialprice");
            jsonObject1.put("pid", listPid.get(i));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (jsonObject1.length() > 0) {
            new getSpecialPrice().execute(String.valueOf(jsonObject1));
        }
//        loadBitmap(strImagePath);

//        new getBitmapImage().execute(strImagePath);

        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DashBoardProductDescriptionActivity.class);
                intent.putExtra("strImagePath", strImagePath);
                intent.putExtra("strPid",strPid);
                intent.putExtra("strItemName",strItemName);
                intent.putExtra("strPrice",strPrice);
                intent.putExtra("strMpn",strMpn);
                context.startActivity(intent);
            }
        });
//        viewHolder.imgThumbnail.setImageResource(alImage.get(i));

//        viewHolder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                if (isLongClick) {
//                    Toast.makeText(context, "#" + position + " - " + alName.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
//                    context.startActivity(new Intent(context, MainActivity.class));
//                } else {
//                    Toast.makeText(context, "#" + position + " - " + alName.get(position), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return alName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView txtItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }

//        public void setClickListener(ItemClickListener itemClickListener) {
//            this.clickListener = itemClickListener;
//        }


    }

    private class getSpecialPrice extends AsyncTask<String, String, String> {


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
                strSpecialPriceResponse = buffer.toString();
                Log.d(TAG, "json response: " + strSpecialPriceResponse);
                //send to post execute
                return strSpecialPriceResponse;
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

            return strSpecialPriceResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {
                JSONObject jsonObject = new JSONObject(strSpecialPriceResponse);
                String strMessage = jsonObject.getString("message");
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String strPrice = object.getString("price");
                    Log.d(TAG, "special price: " + strPrice);

                    String[] spiltPrice = strPrice.split("\\.");
                    String sPrice = spiltPrice[0];
                    Log.d(TAG, "spilt price: " + sPrice);
                    listSpecialPrice.add(sPrice);
//                    txtSPrice.setPaintFlags(txtSPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}




