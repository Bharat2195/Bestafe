package com.toranado.bestafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderConfirmationActivity extends AppCompatActivity {

    private static final String TAG=OrderConfirmationActivity.class.getSimpleName();
    private String strName,strPrice,strBV,strProductId;
    private Toolbar toolbar_order_confirmation;
    private TextView txtTitle;
    private ImageView imgCart,imgProduct;
    private Button btnADDAddress,btnPlaceOrder;
    private TextView txtName,txtAddress,txtAddress1,txtAddress2,txtMobile,txtProductName,txtPrice,txtSubTotalPrice,txtAllTotalPrice,
    txtFooterTotalPrice,txtMinus,txtPlus,txtQunty;
    int i=1;
    private final String Name="strName";
    private final String Price="strPrice";
    private final String BV="strBV";
    private final String ProductId="strProuctId";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);


        getSupportActionBar().hide();

        Intent intent = getIntent();

        strName = intent.getStringExtra(Name);
        strPrice = intent.getStringExtra(Price);
        strBV = intent.getStringExtra(BV);
        strProductId = intent.getStringExtra("strProductId");
        Log.d(TAG, "product id: "+strProductId);


        imgProduct=(ImageView)findViewById(R.id.imgProduct);
//        btnADDAddress=(Button)findViewById(R.id.btnADDAddress);
        txtQunty=(TextView) findViewById(R.id.txtQunty);
        btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrder);

        txtAddress=(TextView) findViewById(R.id.txtAddress);
//        txtAddress1=(TextView) findViewById(R.id.txtAddress1);
//        txtAddress2=(TextView) findViewById(R.id.txtAddress2);
        txtProductName=(TextView) findViewById(R.id.txtProductName);
        txtMobile=(TextView) findViewById(R.id.txtMobile);
        txtPrice=(TextView) findViewById(R.id.txtPrice);
        txtSubTotalPrice=(TextView) findViewById(R.id.txtSubTotalPrice);
        txtAllTotalPrice=(TextView) findViewById(R.id.txtAllTotalPrice);
        txtFooterTotalPrice=(TextView) findViewById(R.id.txtFooterTotalPrice);
        txtMinus=(TextView) findViewById(R.id.txtMinus);
        txtPlus=(TextView) findViewById(R.id.txtPlus);
        txtName=(TextView)findViewById(R.id.txtName);

        toolbar_order_confirmation=(Toolbar)findViewById(R.id.toolbar_order_confirmation);
        toolbar_order_confirmation.setNavigationIcon(R.drawable.arrowleft);
        toolbar_order_confirmation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtTitle= (TextView) toolbar_order_confirmation.findViewById(R.id.txtTitle);
        txtTitle.setText("Place Order");

//        imgCart=(SetImage)toolbar_order_confirmation.findViewById(R.id.imgCart);
//        imgCart.setVisibility(View.GONE);

        txtSubTotalPrice.setText("Rs. "+strPrice);
        txtAllTotalPrice.setText("RS. "+strPrice);
        txtFooterTotalPrice.setText("RS. "+strPrice);

        txtMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int minus=i--;
                txtQunty.setText(String.valueOf(minus));
                int orderValues=Integer.parseInt(txtQunty.getText().toString())*Integer.parseInt(strPrice);
                txtFooterTotalPrice.setText(String.valueOf(orderValues));
                txtFooterTotalPrice.setText("Rs. "+String.valueOf(orderValues));
                txtSubTotalPrice.setText("Rs. "+String.valueOf(orderValues));
                txtAllTotalPrice.setText("Rs. "+String.valueOf(orderValues));
            }
        });

        txtPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int plus=i++;
                txtQunty.setText(String.valueOf(plus));
                int orderValues=Integer.parseInt(txtQunty.getText().toString())*Integer.parseInt(strPrice);
                txtFooterTotalPrice.setText("Rs. "+String.valueOf(orderValues));
                txtSubTotalPrice.setText("Rs. "+String.valueOf(orderValues));
                txtAllTotalPrice.setText("Rs. "+String.valueOf(orderValues));
            }
        });

        txtPrice.setText("Rs. "+strPrice);
        txtProductName.setText(strName);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPayment=new Intent(OrderConfirmationActivity.this,CartLoginActivity.class);
                intentPayment.putExtra("strProductName",strName);
                intentPayment.putExtra("strProductId",strProductId);
                intentPayment.putExtra("strQunty",txtQunty.getText().toString());
                intentPayment.putExtra("strPrice",txtPrice.getText().toString());
                intentPayment.putExtra("strBV",strBV);
                startActivity(intentPayment);
            }
        });


    }
}
