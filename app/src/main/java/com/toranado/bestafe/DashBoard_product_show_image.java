package com.toranado.bestafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DashBoard_product_show_image extends AppCompatActivity {

    ImageView imgCancle,imgProduct;

    PhotoViewAttacher mAttacher;

    String strImagePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_product_show_image);

        getSupportActionBar().hide();

        imgCancle= (ImageView) findViewById(R.id.imgCancle);
        imgProduct= (ImageView) findViewById(R.id.imgProduct);

        Intent intent=getIntent();
        strImagePath=intent.getStringExtra("strImagePath");

        Picasso.with(getApplicationContext()).load(strImagePath).into(imgProduct);
        mAttacher=new PhotoViewAttacher(imgProduct);

        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DashBoard_product_show_image.this, "Cliked!!!", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}
