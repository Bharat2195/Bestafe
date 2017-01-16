package com.toranado.bestafe.adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.toranado.bestafe.DashBoardProductDescriptionActivity;
import com.toranado.bestafe.Product_subcategory;
import com.toranado.bestafe.R;
import com.toranado.bestafe.interfaces.ItemClickListener;

import java.util.ArrayList;

/**
 * Created by cphp on 23-Nov-16.
 */
public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> listItemName;
    private ArrayList<String> listCategoryId;
    private ArrayList<String> listImage;
    private static final String TAG=Subcategory_grid.class.getSimpleName();
    private RelativeLayout relative_layout_sort;
    private LinearLayout liner_layout_filter;
    private BottomSheetDialog bottomSheetDialog;
    private  BottomSheetBehavior bottomSheetBehavior;

    ItemClickListener itemClickListener;



    public  SubcategoryAdapter(Context mContext, ArrayList<String> listItemName, ArrayList<String> listCategoryId, ArrayList<String> listImage) {
        super();
        this.mContext= mContext;
        this.listItemName = listItemName;
        this.listCategoryId = listCategoryId;
        this.listImage=listImage;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item_sub_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.txtItemName.setText(listItemName.get(i));
        final String strItemName=listItemName.get(i);
        final String strImagePath = listImage.get(i);
        final String strPid=listCategoryId.get(i);

        Glide.with(mContext).load(strImagePath).placeholder(R.drawable.ic_default).into(viewHolder.imgItem);
//        loadBitmap(strImagePath);

//        new getBitmapImage().execute(strImagePath);

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Product_subcategory.class);
                intent.putExtra("strName",strItemName);
                intent.putExtra("strPid", strPid);
                mContext.startActivity(intent);
            }
        });

//        relative_layout_sort.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//        viewHolder.imgItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DashBoardProductDescriptionActivity.class);
//                intent.putExtra("strImagePath", strImagePath);
//                intent.putExtra("strPid",strPid);
//                intent.putExtra("strItemName",strItemName);
//                intent.putExtra("strPrice",strPrice);
//                intent.putExtra("strMpn",strMpn);
//                mContext.startActivity(intent);
//            }
//        });
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
        return listItemName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgItem;
        public TextView txtItemName;
        public CardView card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            imgItem = (ImageView) itemView.findViewById(R.id.imgItem);
            txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            card_view=(CardView)itemView.findViewById(R.id.card_view);
//            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(this);
        }

//        public void setClickListener(ItemClickListener itemClickListener) {
//            this.clickListener = itemClickListener;
//        }


    }
}
