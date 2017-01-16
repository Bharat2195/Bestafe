package com.toranado.bestafe.adpter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toranado.bestafe.CategorySubCategoryActivity;
import com.toranado.bestafe.DashBoardCategoryActivity;
import com.toranado.bestafe.R;

import java.util.ArrayList;

/**
 * Created by cphp on 21-Sep-16.
 */
public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> listCategoryName;
    private ArrayList<String> listCategoryId;
    private ArrayList<String> listImagePath;
//    private final int[] imageId;
    private static final String TAG=CustomGrid.class.getSimpleName();


    public CustomGrid(Context mContext, ArrayList<String> listCategoryName,ArrayList<String>listCategoryId, ArrayList<String>listImagePath) {
        this.mContext = mContext;
        this.listCategoryName = listCategoryName;
        this.listCategoryId=listCategoryId;
        this.listImagePath = listImagePath;
    }

    @Override
    public int getCount() {
        return listCategoryName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;

        if (convertView==null){
            grid=new View(mContext);
            LayoutInflater layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid=layoutInflater.inflate(R.layout.grid_single,null);

        }else {
            grid=(View) convertView;
        }
        TextView textView=(TextView)grid.findViewById(R.id.grid_text);
        ImageView imageView=(ImageView)grid.findViewById(R.id.grid_image);
        textView.setText(listCategoryName.get(position));
//            imageView.setImageResource(imageId[position]);
        Picasso.with(mContext).load(listImagePath.get(position)).placeholder(R.drawable.ic_default).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCategory=new Intent(mContext, CategorySubCategoryActivity.class);
                intentCategory.putExtra("CategoryName",listCategoryName.get(position));
                intentCategory.putExtra("CategoryId",listCategoryId.get(position));
                mContext.startActivity(intentCategory);
            }
        });

        return grid;
    }
}
