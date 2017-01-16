package com.toranado.bestafe.paymet_class;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.Utils;
import com.toranado.bestafe.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CustomDetailsActivity extends BaseActivity {

    EditText emailEt;
    EditText mobileEt;
    private TextView txtTitle;
    @InjectView(R.id.toolbar_payment_option)
    Toolbar toolbar_payment_option;
    ArrayList<String> listPrice=new ArrayList<>();
    private static final String TAG=CustomDetailsActivity.class.getSimpleName();
    int sum=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

//        getSupportActionBar().hide();
        emailEt = (EditText) findViewById(R.id.email_et);
        mobileEt = (EditText) findViewById(R.id.mobile_et);
        Intent intent=getIntent();
        listPrice=intent.getStringArrayListExtra("strPrice");
        Log.d(TAG, "listprice data: "+listPrice);

        for (int i=0; i<listPrice.size();i++){
            sum+=Integer.parseInt(listPrice.get(i));
        }

        txtTitle=(TextView)toolbar_payment_option.findViewById(R.id.txtTitle);
        txtTitle.setText("Payment Option");

        toolbar_payment_option.setNavigationIcon(R.drawable.arrowleft);
        toolbar_payment_option.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.quick_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDetails()) {
                    String mobile = mobileEt.getText().toString().trim();
                    String email = emailEt.getText().toString().trim();
                    CitrusFlowManager.startShoppingFlow(CustomDetailsActivity.this, email, mobile, String.valueOf(sum), false);
//                    CitrusFlowManager.startShoppingFlow(CustomDetailsActivity.this, email, mobile, "10", false);
                }
            }
        });
        findViewById(R.id.wallet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateDetails()) {
                    String mobile = mobileEt.getText().toString().trim();
                    String email = emailEt.getText().toString().trim();
                    CitrusFlowManager.startWalletFlow(CustomDetailsActivity.this, email, mobile);
                }
            }
        });
    }

    private boolean validateDetails() {
        String mobile = mobileEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Mobile Empty", Toast.LENGTH_SHORT).show();
            mobileEt.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email Empty", Toast.LENGTH_SHORT).show();
            emailEt.requestFocus();
            return false;
        } else if (!Utils.validate(email)) {
            Toast.makeText(this, "Email address is not valid", Toast.LENGTH_SHORT).show();
            emailEt.requestFocus();
            return false;
        } else {

            return true;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_custom_details;
    }

}
