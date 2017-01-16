package com.toranado.bestafe.adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toranado.bestafe.DashBoardProductDescriptionActivity;
import com.toranado.bestafe.Product_subcategory;
import com.toranado.bestafe.R;

import java.util.ArrayList;

/**
 * Created by cphp on 21-Sep-16.
 */
public class Subcategory_grid extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> listItemName;
    private ArrayList<String> listCategoryId;
    private ArrayList<String> listImage;
    private static final String TAG=Subcategory_grid.class.getSimpleName();

    public Subcategory_grid(Context mContext, ArrayList<String> listItemName, ArrayList<String>listCategoryId, ArrayList<String>listImage) {
        this.mContext = mContext;
        this.listItemName = listItemName;
        this.listCategoryId=listCategoryId;
        this.listImage = listImage;
    }

    @Override
    public int getCount() {
        return listItemName.size();
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
        View grid_item;


        if (convertView==null){

            grid_item=new View(mContext);
            LayoutInflater layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid_item=layoutInflater.inflate(R.layout.grid_item_sub_category,null);

//
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intentCategory=new Intent(mContext, CategorySubCategoryActivity.class);
//                    intentCategory.putExtra("CategoryName",listName.get(position));
//                    intentCategory.putExtra("CategoryId",listCategoryId.get(position));
//                    mContext.startActivity(intentCategory);
//                }
//            });
        }else {
            grid_item=(View) convertView;
        }

        TextView textView=(TextView)grid_item.findViewById(R.id.txtItemName);
        ImageView imageView=(ImageView)grid_item.findViewById(R.id.imgItem);
        CardView cardView=(CardView)grid_item.findViewById(R.id.card_view);
        textView.setText(listItemName.get(position));
        Log.d(TAG," subacategory image path:"+listImage.get(position));
        Picasso.with(mContext).load(listImage.get(position)).placeholder(R.drawable.ic_default).into(imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Product_subcategory.class);
                intent.putExtra("strName",listItemName.get(position));
                intent.putExtra("strPid", listCategoryId.get(position));
                mContext.startActivity(intent);
            }
        });

        return grid_item;
    }
}
