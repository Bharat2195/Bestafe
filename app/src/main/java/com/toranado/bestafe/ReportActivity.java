package com.toranado.bestafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

public class ReportActivity extends AppCompatActivity {
    String strMemberid="";
    Toolbar toolbar_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent=getIntent();
        strMemberid=intent.getStringExtra("memberid");
        getSupportActionBar().hide();
        toolbar_report=(Toolbar)findViewById(R.id.toolbar_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_report.setNavigationIcon(R.drawable.left_arrow1);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar_report.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
