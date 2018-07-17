package com.wrx.quickeats.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.wrx.quickeats.R;
import com.wrx.quickeats.adapter.MyCart_Adap;
import com.wrx.quickeats.bean.FMenuBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import org.w3c.dom.Text;

public class MyKart extends AppCompatActivity {

  View view;

  private Toolbar myToolbar;
  TextView tv_title, grand_total, sub_total;
  RecyclerView rv_mycart;
  MyCart_Adap myCart_adap;
  Button btn_continue;
  static Dialog dialog;
  ArrayList<FMenuBean> fMenuBeanArrayList;
  String restaurant_id;
  int total_item;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DataBindingUtil.setContentView(this, R.layout.fragment_my_kart);

/*    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      bundle.getString("restaurant_id");
    }
    if (bundle != null) {
      bundle.getInt("total_item", 0);
    }*/

    Mycart_id();
    try {
      fMenuBeanArrayList = (ArrayList<FMenuBean>) getIntent().getSerializableExtra("menuList");
      restaurant_id = getIntent().getStringExtra("restaurant_id");
      total_item = getIntent().getIntExtra("total_item", 0);
      //Toast.makeText(MyKart.this,"checkList: "+fMenuBeanArrayList.size(),Toast.LENGTH_LONG).show();

    } catch (Exception e) {
      e.printStackTrace();
    }

    myCart_adap = new MyCart_Adap(this, fMenuBeanArrayList, grand_total, sub_total);
    double finalTotal = getGrandTotal() + 100;
    grand_total.setText("KES " + finalTotal);
    rv_mycart.setAdapter(myCart_adap);
    btn_continue.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

double grandToatal = getGrandTotal();

        Intent intent = new Intent(MyKart.this, UserAddress.class);
        intent.putExtra("menuList", fMenuBeanArrayList);
        intent.putExtra("restaurant_id", restaurant_id);
        intent.putExtra("total_item", total_item);
        //intent.putExtra(sub_total);
        intent.putExtra("grandTotal", grandToatal);
        startActivity(intent);
      }
    });
  }

  private double getGrandTotal(){

    double grandToatal = 0;
    // diologue_activity();
    for (int i = 0; i < fMenuBeanArrayList.size(); i++) {
      grandToatal =
          grandToatal + (Double.parseDouble(fMenuBeanArrayList.get(i).getItem_price()) * Integer
              .parseInt(fMenuBeanArrayList.get(i).getQuantity()));
    }
    return grandToatal;
  }

  private void Mycart_id() {

    //sub_total = (TextView) findViewById(R.id.sub_total);
    tv_title = (TextView) findViewById(R.id.tv_title);
    grand_total = (TextView) findViewById(R.id.grand_total);
    btn_continue = (Button) findViewById(R.id.btn_continue);
    tv_title.setVisibility(View.VISIBLE);
    tv_title.setText("My Cart");

    myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
    myToolbar.setNavigationIcon(R.drawable.back);

    myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        Intent intent = new Intent();
        intent.putExtra("menu_list", fMenuBeanArrayList);
        setResult(102, intent);
        onBackPressed();
      }
    });
    rv_mycart = (RecyclerView) findViewById(R.id.rv_mycart);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    rv_mycart.setLayoutManager(linearLayoutManager);
  }

  public void diologue_activity() {

    dialog = new Dialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.order_dialogue);
    dialog.setCancelable(true);
    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

    Button btn_now = (Button) dialog.findViewById(R.id.btn_now);
    Button btn_later = (Button) dialog.findViewById(R.id.btn_later);

    btn_now.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        //getFragmentManager().beginTransaction().replace(R.id.search_container, new UserAddress()).addToBackStack("").commit();
        startActivity(new Intent(MyKart.this, UserAddress.class));
        dialog.cancel();
      }
    });

    btn_later.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
      }
    });

    dialog.show();
  }

  public static class DatePickerFragment extends DialogFragment
      implements DatePickerDialog.OnDateSetListener {
    Calendar c;

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
      c = Calendar.getInstance();
      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);
      DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
      //dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
      return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
      //btnDate.setText(ConverterDate.ConvertDate(year, month + 1, day));
      String myFormat = "yyyy-MM-dd";
      SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
      c.set(Calendar.YEAR, year);
      c.set(Calendar.MONTH, month);
      c.set(Calendar.DAY_OF_MONTH, day);
      dialog.dismiss();
      //            dateView.setText("" + day);
      //            monthView.setText("" + (month + 1));
      //            yearView.setText("" + year);
      //
      //            startDate = "" + sdf.format(c.getTime());

    }
  }
}
