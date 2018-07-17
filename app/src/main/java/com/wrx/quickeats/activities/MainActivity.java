package com.wrx.quickeats.activities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wrx.quickeats.bean.HomeResponse;
import com.wrx.quickeats.fragments.Home;
import com.wrx.quickeats.fragments.MapFragment;
import com.wrx.quickeats.fragments.MyOrder;
import com.wrx.quickeats.fragments.Profile;
import com.wrx.quickeats.util.BottomNavigationViewHelper;
import com.wrx.quickeats.fragments.MyFavourite;
import com.wrx.quickeats.R;
import com.wrx.quickeats.util.GPSTracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Home.HomeInterface {

    BottomNavigationView navigation;
    TextView tv_title;
    private Toolbar myToolbar;
    private ImageView img_homeLogo;
    TextView tool_text;
    GPSTracker gpsTracker;
    String fcm_type = "", fcm_message = "";
    public static boolean openApp = false;
    ArrayList<HomeResponse> homeResponseArrayList;

    public static Fragment getFragment() {
        return fragment;
    }

    public static void setFragment(Fragment fragment) {
        MainActivity.fragment = fragment;
    }

    static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme11);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
       // homeResponseArrayList=new ArrayList<>();
        gpsTracker = new GPSTracker(this, this);
        double lat = gpsTracker.getLatitude();
        double log = gpsTracker.getLongitude();
        //SharedPreferenceWriter.getInstance(this).writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, true);
        openApp = true;
        /*LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(messageReciever,
                new IntentFilter("Push"));*/
        try {
            fcm_type = getIntent().getStringExtra("type");
            fcm_message = getIntent().getStringExtra("message");
            if (!fcm_message.equals(""))
                Toast.makeText(this, fcm_message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        All(lat, log);


    }

    private BroadcastReceiver messageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getStringExtra("type").equals("")) {
                //callMyOrderApi();
                try {
                    fcm_type = intent.getStringExtra("type");
                    fcm_message = intent.getStringExtra("message");
                    if (!fcm_message.equals(""))
                        Toast.makeText(MainActivity.this, fcm_message, Toast.LENGTH_LONG).show();
                    if (getVisibleFragment() instanceof MyOrder) {
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            Window window = getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                        }
                        getSupportActionBar().show();
                        tv_title.setVisibility(View.VISIBLE);
                        tool_text.setVisibility(View.GONE);
                        tv_title.setText("My Orders");
                        img_homeLogo.setVisibility(View.GONE);

                        setToolbarBackground(false);

                        myToolbar.setNavigationIcon(null);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new MyOrder()).commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        @SuppressLint("RestrictedApi") List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.home:
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
                    }
                    getSupportActionBar().show();
//                    tv_title.setVisibility(View.VISIBLE);
//                    tv_title.setText("EAT");

                    img_homeLogo.setVisibility(View.VISIBLE);
                    tool_text.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.GONE);

                    setToolbarBackground(true);

                    Fragment fragment = new Home();
                    setFragment(fragment);

                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    myToolbar.setNavigationIcon(R.drawable.filter_home);

                    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(MainActivity.this, SearchFilter.class);
                            intent.putExtra("home_arrylist",homeResponseArrayList);
                            startActivity(intent);
                        }
                    });
                    return true;

                case R.id.search:
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }
                    img_homeLogo.setVisibility(View.GONE);

                    tv_title.setVisibility(View.GONE);
                    tool_text.setVisibility(View.GONE);
                    setToolbarBackground(false);
//                    Intent intent = new Intent(MainActivity.this, SearchMap.class);
//                    startActivityForResult(intent, 1);
                    Fragment frg=new MapFragment();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("homeResponseList",homeResponseArrayList);
                    frg.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, frg).commit();
                    myToolbar.setNavigationIcon(null);
                    getSupportActionBar().hide();
                    return true;

                case R.id.fav:
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }
                    getSupportActionBar().show();
                    tv_title.setVisibility(View.VISIBLE);
                    tool_text.setVisibility(View.GONE);
                    tv_title.setText("My Favourite");

                    setToolbarBackground(false);

                    img_homeLogo.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new MyFavourite()).commit();
//                    Intent intent1 = new Intent(MainActivity.this, MyFav.class);
//                    startActivity(intent1);
                    myToolbar.setNavigationIcon(null);
                    return true;

                case R.id.order:
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }
                    getSupportActionBar().show();
                    tv_title.setVisibility(View.VISIBLE);
                    tool_text.setVisibility(View.GONE);
                    tv_title.setText("My Orders");
                    img_homeLogo.setVisibility(View.GONE);

                    setToolbarBackground(false);

                    myToolbar.setNavigationIcon(null);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new MyOrder()).commit();
                    return true;

                case R.id.profile:
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    }
                    getSupportActionBar().show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new Profile()).commit();
                    tv_title.setVisibility(View.VISIBLE);
                    tool_text.setVisibility(View.GONE);
                    tv_title.setText("Profile");
                    img_homeLogo.setVisibility(View.GONE);

                    setToolbarBackground(false);

                    myToolbar.setNavigationIcon(R.drawable.settings);
                    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent1 = new Intent(MainActivity.this, Settings.class);
                            startActivity(intent1);
                        }
                    });

                    return true;
            }
            return false;
        }
    };

    private void setToolbarBackground(boolean isHome) {
        if (myToolbar != null) {
            if (isHome) {
                myToolbar.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.home_header_color));
            } else {
                myToolbar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.header_home));
            }

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        openApp = true;
        //SharedPreferenceWriter.getInstance(this).writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        openApp = true;
        //SharedPreferenceWriter.getInstance(this).writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        openApp = false;
        //SharedPreferenceWriter.getInstance(this).writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        openApp = false;
        //SharedPreferenceWriter.getInstance(this).writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openApp = false;
        //SharedPreferenceWriter.getInstance(this).writeBooleanValue(SharedPreferenceKey.appOpenOrNOt, false);
    }

    void All(double lat, double log) {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BottomNavigationViewHelper.disableShiftMode(navigation);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        tool_text = (TextView) myToolbar.findViewById(R.id.tool_text);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_homeLogo = (ImageView) findViewById(R.id.img_homeLogo);
        if (fcm_type != null) {
            navigation.setSelectedItemId(navigation.getMenu().getItem(3).getItemId());
//            getSupportActionBar().show();
//            tv_title.setVisibility(View.VISIBLE);
//            tool_text.setVisibility(View.GONE);
//            tv_title.setText("My Orders");
//            img_homeLogo.setVisibility(View.GONE);
//
//            setToolbarBackground(false);
//
//            myToolbar.setNavigationIcon(null);
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.content, new MyOrder()).commit();
        } else {
            Fragment fragment = new Home();
            setFragment(fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            myToolbar.setNavigationIcon(R.drawable.filter_home);

//        tv_title.setVisibility(View.VISIBLE);
//        tv_title.setText("EAT");


            img_homeLogo.setVisibility(View.VISIBLE);
            setToolbarBackground(true);


            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses=null;
            String cityName = "", stateName = "", countryName = "", countryCode = "", city = "";
            try {
                addresses = geocoder.getFromLocation(lat, log, 1);
                cityName = addresses.get(0).getAddressLine(0);
                //stateName = addresses.get(0).getAddressLine(1);
                // countryName = addresses.get(0).getAddressLine(2);
                city = addresses.get(0).getLocality();
                //countryCode = addresses.get(0).getCountryCode();

            } catch (IOException e) {
                e.printStackTrace();
                //                        addresses = geocoder.getFromLocation(lat, log, 1);
//                        countryName = addresses.get(0).getAddressLine(2);
//                        countryCode = addresses.get(0).getCountryCode();
                try {
                    cityName = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                } catch (NullPointerException exp) {
                    exp.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            if (city != null) {
                tool_text.setVisibility(View.VISIBLE);
                tool_text.setText(city);
            }
        }


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SearchFilter.class);
                intent.putExtra("home_arrylist",homeResponseArrayList);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {

            navigation.setSelectedItemId(navigation.getMenu().getItem(3).getItemId());
        }
    }

    @Override
    public void getHomeList(ArrayList<HomeResponse> homeResponseArrayListsss) {
        homeResponseArrayList=homeResponseArrayListsss;
    }

/*
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(MainActivity.this,"Exit",Toast.LENGTH_SHORT).show();

        final Dialog dialog = new Dialog(this, R.style.ThemeDialogCustom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCancelable(false);

        TextView txtYes =(TextView)dialog.findViewById(R.id.txtYes);
        TextView txtNo =(TextView)dialog.findViewById(R.id.txtNo);

        txtYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                dialog.dismiss();
            }
        });

        txtNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }*/

}
