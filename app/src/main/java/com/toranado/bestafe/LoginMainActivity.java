package com.toranado.bestafe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class

LoginMainActivity extends AppCompatActivity {

//    Toolbar toolbar_login_main;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout container;
    private ExpandableListView expListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private static final String TAG = LoginMainActivity.class.getSimpleName();
    private HashMap<String, List<String>> listDataChild;
    public static int[] icon = {R.drawable.dashboard_icon, R.drawable.team_icon,R.drawable.account_icon,
            R.drawable.pinmodule_icon,R.drawable.account_icon,R.drawable.floor_icon,R.drawable.history,R.drawable.team_icon,};
    ArrayList<Integer> listIcon = new ArrayList<>();
    private static final int FILTER_ID = 0;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static  String strId = "";
    Fragment fragment;
    private Toolbar toolbar_login_main;
    TextView txtSignIn, txtNo, txtYes,txtTitle;
    ImageView imgSignIn;
    public static final String PREFS_NAME = "LoginPrefes";
    public static final String CART_PREFS_NAME = "CartLoginPrefes";
    LinearLayout btnChangePassword,btnChangeTransactionPassword, btn_profile, btnSponsorlist, btnLevelSummary, btnWithdrawal, btnLevelIncome, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        btn_profile = (LinearLayout) findViewById(R.id.btn_profile);
        btnSponsorlist = (LinearLayout) findViewById(R.id.btnSponsorlist);
        btnLevelSummary = (LinearLayout) findViewById(R.id.btnLevelSummary);
        btnWithdrawal = (LinearLayout) findViewById(R.id.btnWithdrawal);
        btnLevelIncome = (LinearLayout) findViewById(R.id.btnLevelIncome);
        btnLogout = (LinearLayout) findViewById(R.id.btnLogout);
        btnChangePassword= (LinearLayout) findViewById(R.id.btnChangePassword);
//        btnChangeTransactionPassword= (LinearLayout) findViewById(R.id.btnChangeTransactionPassword);

        toolbar_login_main = (Toolbar) findViewById(R.id.toolbar_login_main);
        txtTitle=(TextView)toolbar_login_main.findViewById(R.id.txtTitle);
        txtTitle.setText("Home");

        toolbar_login_main.setNavigationIcon(R.drawable.arrowleft);
        toolbar_login_main.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginMainActivity.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.activity_log_out);
                txtNo = (TextView) dialog.findViewById(R.id.txtNo);
                txtYes = (TextView) dialog.findViewById(R.id.txtYes);
                dialog.show();
                txtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                txtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences sharedPreferences1 = getSharedPreferences(CART_PREFS_NAME, 0);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.clear();
                        editor1.commit();

                        Intent intent = new Intent(LoginMainActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }
                });

            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentprofile=new Intent(LoginMainActivity.this,ViewProfileActivity.class);
                intentprofile.putExtra("id",strId);
                startActivity(intentprofile);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChangePWd=new Intent(LoginMainActivity.this,MembeChangePasswordActivity.class);
//                intentChangePWd.putExtra("memberid",strId);
                startActivity(intentChangePWd);
            }
        });

//        btnChangeTransactionPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentChnageTransactionPassword=new Intent(LoginMainActivity.this,TransactionPasswordActivity.class);
////                intentChnageTransactionPassword.putExtra("memberid",strId);
//                startActivity(intentChnageTransactionPassword);
//            }
//        });

        btnLevelIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIncome=new Intent(LoginMainActivity.this,LevelIncome.class);
                intentIncome.putExtra("memberid",strId);
                startActivity(intentIncome);
            }
        });

        btnSponsorlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPin=new Intent(LoginMainActivity.this,SponsorListReport.class);
                intentPin.putExtra("memberid",strId);
                startActivity(intentPin);
            }
        });

        btnLevelSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGenealogy=new Intent(LoginMainActivity.this,SponsorSummaryActivity.class);
                intentGenealogy.putExtra("memberid",strId);
                startActivity(intentGenealogy);
            }
        });

        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEwallet=new Intent(LoginMainActivity.this,WithdrawalReport.class);
                intentEwallet.putExtra("memberid",strId);
                startActivity(intentEwallet);
            }
        });

        btnLevelIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentIncome=new Intent(LoginMainActivity.this,LevelIncome.class);
                intentIncome.putExtra("memberid",strId);
                startActivity(intentIncome);
            }
        });


        mTitle = mDrawerTitle = getTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        ab.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        strId = intent.getStringExtra("id");
        Log.d(TAG, "Login Main Activity ID:" + strId);
        setUpDrawer();
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, container, toolbar_login_main, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        container.setDrawerListener(mDrawerToggle);
        makeActionOverflowMenuShown();
    }

    private void makeActionOverflowMenuShown() {
        try {
            final ViewConfiguration config = ViewConfiguration.get(this);
            final Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (final Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }

    private void setUpDrawer() {
        container = (DrawerLayout) findViewById(R.id.container);
        container.setDrawerListener(mDrawerListener);
        expListView = (ExpandableListView) findViewById(R.id.list_slidermenu);
        prepareListData();


        listAdapter = new ExpandableListAdapter(this, listDataHeader,
                listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // setbackground color for list that is selected in child group
                v.setSelected(true);


                if (childPosition == 8) {
                    Intent intent = new Intent(LoginMainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                Fragment fragment = null;
                switch (groupPosition) {
                    // dash board
                    case 0:
                        break;

                    // before you file
                    case 1:
                        switch (childPosition) {
                            case 0:
                                Intent intentLogin = new Intent(LoginMainActivity.this, ViewProfileActivity.class);
                                intentLogin.putExtra("id", strId);
                                startActivity(intentLogin);
                                break;
                            case 1:
                                Intent intentChangePassword = new Intent(LoginMainActivity.this, MembeChangePasswordActivity.class);
                                intentChangePassword.putExtra("tabIndex", "0");
                                startActivity(intentChangePassword);
                                //fragment=new fragment_change_password();
                                break;

                            case 2:
                                Intent intentTransactionPassword = new Intent(LoginMainActivity.this, MembeChangePasswordActivity.class);
                                intentTransactionPassword.putExtra("tabIndex", "1");
                                startActivity(intentTransactionPassword);
                                //fragment=new Fragment_transaction_password();
                                break;
                            default:
                                break;

                        }
                        break;
                    case 2:
                        switch (childPosition) {
                            case 0:
                                Intent intentSponsorList = new Intent(LoginMainActivity.this, SponsorListReport.class);
                                intentSponsorList.putExtra("memberid", strId);
                                startActivity(intentSponsorList);
                                break;
                            case 1:
                                Intent intentSponsorSummary = new Intent(LoginMainActivity.this, SponsorSummaryActivity.class);
                                intentSponsorSummary.putExtra("memberid", strId);
                                startActivity(intentSponsorSummary);
                                break;
                            case 2:
                                Intent intentPlacementSummary=new Intent(LoginMainActivity.this,PlacementSummary.class);
                                intentPlacementSummary.putExtra("memberid", strId);
                                startActivity(intentPlacementSummary);
                            default:
                                break;
                            //Toast.makeText(getApplicationContext(),childPosition,Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 3:
                        switch (childPosition) {
                            case 0:
                                Intent intentWithdrawal = new Intent(LoginMainActivity.this, WithdrawalReport.class);
                                intentWithdrawal.putExtra("memberid", strId);
                                startActivity(intentWithdrawal);
                                break;

                        }
                        break;

                    case 4:
                        switch (childPosition){
                            case 0:
                                Intent intentDocument=new Intent(LoginMainActivity.this,DocumentListActivity.class);
                                intentDocument.putExtra("memberid",strId);
                                startActivity(intentDocument);
                                break;
                        }
                        break;

                    case 5:
                        switch (childPosition) {
                            case 0:
                                Intent intentLevelIncome = new Intent(LoginMainActivity.this, LevelIncome.class);
                                intentLevelIncome.putExtra("memberid", strId);
                                startActivity(intentLevelIncome);
                            default:
                                break;
                        }
                        break;

                    case 6:
                        switch (childPosition) {
                            case 0:
                                Intent intentHistrory= new Intent(LoginMainActivity.this, OrderHistoryActivity.class);
                                startActivity(intentHistrory);
                            default:
                                break;
                        }
                        break;

                    case 7:
                        switch (childPosition) {
                            case 0:
                                final Dialog dialog = new Dialog(LoginMainActivity.this);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.activity_log_out);
                                txtNo = (TextView) dialog.findViewById(R.id.txtNo);
                                txtYes = (TextView) dialog.findViewById(R.id.txtYes);
                                dialog.show();
                                txtNo.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                txtYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.commit();

                                        SharedPreferences sharedPreferences1 = getSharedPreferences(CART_PREFS_NAME, 0);
                                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                        editor1.clear();
                                        editor1.commit();

                                        Intent intent = new Intent(LoginMainActivity.this, SplashSignupActivity.class);
                                        startActivity(intent);

                                    }
                                });
                                /*Intent intentLogout=new Intent(LoginMainActivity.this,LogOutActivity.class);
                                intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intentLogout);*/
                                break;

                        }
                        break;
                    default:
                        break;
                }
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,fragment,"").addToBackStack(null).commit();
                expListView.setItemChecked(childPosition, true);
                container.closeDrawer(Gravity.LEFT);*/
                return true;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            //            int previousGroup = -1;
//
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousGroup) ;
//                expListView.collapseGroup(previousGroup);
//                previousGroup = groupPosition;
//
//
//            }
            int previousGroup = -1;
            private int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousGroup) ;
//                expListView.collapseGroup(previousGroup);
//                previousGroup = groupPosition;
//            }
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState); // Sync the toggle state after
        // onRestoreInstanceState has
        // occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); // Pass any configuration
        // change to the drawer
        // toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);

        // creating dynamic textview to diisplay user name on actionbar
        TextView tv = new TextView(this);
        tv.setText("Sample");
        // tv.setTextColor(getResources().getColor(R.color.white));
        tv.setPadding(5, 0, 5, 0);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(14);

        menu.add(0, FILTER_ID, 1, "Sample").setActionView(tv)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Catch the events related to the drawer to arrange views according to this
    // action if necessary...
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
            getSupportActionBar().setTitle(mDrawerTitle);
            // calling onPrepareOptionsMenu() to hide action bar icons
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            getSupportActionBar().setTitle(mTitle);
            // calling onPrepareOptionsMenu() to show action bar icons
            invalidateOptionsMenu();
        }
    };

    private void prepareListData() {

        listDataChild = new HashMap<String, List<String>>();


        String[] array = getResources()
                .getStringArray(R.array.login_main);
        listDataHeader = Arrays.asList(array);


        // Adding child data
        // dash board

        List<String> listProfile = new ArrayList<String>();
        String[] strProfile = getResources().getStringArray(R.array.login_profile);
        listProfile = Arrays.asList(strProfile);

        // before you file
        List<String> listGenealogy = new ArrayList<String>();
        String[] strGenealogy = getResources()
                .getStringArray(R.array.login_genealogy);
        listGenealogy = Arrays.asList(strGenealogy);

        // profile
        List<String> listSupport = new ArrayList<String>();
        String[] strSupport = getResources().getStringArray(R.array.login_support);
        listSupport = Arrays.asList(strSupport);

        List<String> listLevelIncome = new ArrayList<String>();
        String[] strLevelIncome = getResources().getStringArray(R.array.login_levelInCom);
        listLevelIncome = Arrays.asList(strLevelIncome);

        List<String> listDocument=new ArrayList<>();
        String[] strDocument=getResources().getStringArray(R.array.document_upload);
        listDocument=Arrays.asList(strDocument);

        // income slip
        List<String> listLoginLogout = new ArrayList<String>();
        String[] strLoginLogout = getResources().getStringArray(R.array.login_logout);
        listLoginLogout = Arrays.asList(strLoginLogout);

        // federal deduction
        List<String> listPersonalCare = new ArrayList<String>();
        String[] strPersonalCare = getResources().getStringArray(R.array.personal_care);
        listPersonalCare = Arrays.asList(strPersonalCare);

        // provincial credits
        List<String> listDeals = new ArrayList<String>();
        String[] strDeals = getResources().getStringArray(
                R.array.deal);
        listDeals = Arrays.asList(strDeals);

        // expenses
        List<String> Logout = new ArrayList<String>();
        String[] strLogout = getResources().getStringArray(R.array.login_main_logout);
        Logout = Arrays.asList(strLogout);

        // review
        List<String> listLogin = new ArrayList<String>();
        String[] strLogin = getResources().getStringArray(R.array.login);
        listLogin = Arrays.asList(strLogin);

        List<String> listRegister = new ArrayList<String>();
        String[] strRegister = getResources().getStringArray(R.array.register);
        listRegister = Arrays.asList(strRegister);

        List<String> listHistory= new ArrayList<String>();
        String[] strHistrory= getResources().getStringArray(R.array.history);
        listHistory = Arrays.asList(strHistrory);

        // cra profile
        List<String> list_logout = new ArrayList<String>();
        List<String> list_general_report = new ArrayList<String>();
        String[] strGeneralReport = getResources().getStringArray(R.array.general_report);
        list_general_report = Arrays.asList(strGeneralReport);

        // submit
        List<String> l9 = new ArrayList<String>();

        // cloud drive
        List<String> l10 = new ArrayList<String>();

        // assigning values to menu and submenu
        listDataChild.put(listDataHeader.get(0), list_logout); // Header, Child
        // data
        listDataChild.put(listDataHeader.get(1), listProfile);
        listDataChild.put(listDataHeader.get(2), listGenealogy);
        listDataChild.put(listDataHeader.get(3), list_general_report);
        listDataChild.put(listDataHeader.get(4), listDocument);
        listDataChild.put(listDataHeader.get(5), listLevelIncome);
        listDataChild.put(listDataHeader.get(6), listHistory);
        listDataChild.put(listDataHeader.get(7), listLoginLogout);

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context,
                                     List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(
                    this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition,
                    childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_drawer, null);
            }

            /*if (getChildrenCount( groupPosition) ==0){
            }*/

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(
                    this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_drawer, null);
            }
            // changing the +/- on expanded list view
            TextView txt_plusminus = (TextView) convertView
                    .findViewById(R.id.plus_txt);
            if (isExpanded) {
                txt_plusminus.setText("-");
            } else {
                txt_plusminus.setText("+");
            }

            if (
                    getChildrenCount(groupPosition) == 0) {
                txt_plusminus.setText(" ");
                txt_plusminus.setText(" ");
            }
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            // lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            ImageView imgListGroup = (ImageView) convertView
                    .findViewById(R.id.ic_txt);
            imgListGroup.setImageResource(icon[groupPosition]);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        if (preferences.getString("logged", "").toString().equals("logged")) {
            Intent intent = new Intent(LoginMainActivity.this, SplashSignupActivity.class);
            startActivity(intent);
        } else {
            onBackPressed();
        }
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
//        if (preferences.getString("logged", "").toString().equals("logged")) {
//            Intent intent = new Intent(LoginMainActivity.this, FirstShowCategoryActivity.class);
//            startActivity(intent);
//        } else {
//            onBackPressed();
//        }
//    }
}
