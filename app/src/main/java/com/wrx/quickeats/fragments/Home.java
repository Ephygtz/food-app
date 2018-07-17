package com.wrx.quickeats.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.wrx.quickeats.activities.MyKart;
import com.wrx.quickeats.activities.SearchFilter;
import com.wrx.quickeats.bean.HomeResponse;
import com.wrx.quickeats.database.SharedPreferenceWriter;
import com.wrx.quickeats.util.SharedPreferenceKey;
import com.wrx.quickeats.R;
import com.wrx.quickeats.activities.Notification;
import com.wrx.quickeats.adapter.HomeAdapter;
import com.wrx.quickeats.bean.CommonResponse;
import com.wrx.quickeats.retrofit.ApiClient;
import com.wrx.quickeats.retrofit.ApiInterface;
import com.wrx.quickeats.retrofit.MyDialog;
import com.wrx.quickeats.util.GPSTracker;

import java.util.ArrayList;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Home extends Fragment implements SearchFilter.FilterInterface {

  View view;
  private RecyclerView rv_home;
  ArrayList<Integer> viewpagerimage = new ArrayList();
  private HomeAdapter homeAdapter;
  GPSTracker gpsTracker;
  ArrayList<HomeResponse> homeResponseArrayList;
  ArrayList<HomeResponse> homeResponseArrayListClone;
  ArrayList<HomeResponse> arrayList;
  HomeInterface homeInterface;
  ArrayList<HomeResponse> homeList;
  static double PI_RAD = Math.PI / 180.0;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    view = inflater.inflate(R.layout.home_frag, container, false);
    //FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
    ////ColorStateList colorStateList = new ColorStateList(new  int[0][], new int[]{0xffffcc00});
    //fab.setBackgroundTintList(ColorStateList.valueOf(0xffffff));
    //
    //fab.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View view) {
    //    Intent cartIntent  = new Intent(getActivity(), MyKart.class);
    //    startActivity(cartIntent);
    //  }
    //});

    setHasOptionsMenu(true);
    homeInterface = (HomeInterface) getActivity();
    All_ID();
    gpsTracker = new GPSTracker(getActivity(), getActivity());

    //TODO: SET THE IMAGE ADAPTER
    viewpagerimage.add(R.drawable.food);
    viewpagerimage.add(R.drawable.food);
    viewpagerimage.add(R.drawable.food);
    viewpagerimage.add(R.drawable.food);
    viewpagerimage.add(R.drawable.food);

    PagerContainer mContainer = (PagerContainer) view.findViewById(R.id.pager_container);
    //mContainer.setOverlapEnabled(true);
    final ViewPager pager = mContainer.getViewPager();
    PagerAdapter adapter = new MyPagerAdapter();
    pager.setAdapter(adapter);
    pager.setCurrentItem(1);
    pager.setOffscreenPageLimit(adapter.getCount());
    pager.setClipChildren(false);

    new CoverFlow.Builder().with(pager)
        .pagerMargin(-65f)
        .scale(0.3f)
        .spaceSize(0f)
        .rotationY(0f)
        .build();
    callHomeApi();

    return view;
  }

  private void All_ID() {

    rv_home = (RecyclerView) view.findViewById(R.id.rv_home);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    rv_home.setLayoutManager(linearLayoutManager);
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    inflater.inflate(R.menu.notification, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // handle item selection
    switch (item.getItemId()) {
      case R.id.not_menu:

        Intent intent = new Intent(getContext(), Notification.class);
        startActivity(intent);

        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public void getFilterTypeOrName(String resType, String resName) {
    homeList = new ArrayList<>();
    homeResponseArrayList.clear();
    homeResponseArrayList.addAll(homeResponseArrayListClone);
    for (HomeResponse homeResponse : homeResponseArrayList) {

      if ((homeResponse.getRest_type().contains(resType.trim()) && resType.trim().length() > 0) || (
          homeResponse.getRest_name().contains(resName.trim())
              && resName.trim().length() > 0)) {
        if (greatCircleInKilometers(gpsTracker.getLatitude(), gpsTracker.getLongitude(),
            Double.parseDouble(homeResponse.getLatitude()), Double.parseDouble(homeResponse.getLongitude())) <= 5) {

          HomeResponse homeResponse1 = new HomeResponse();
          homeResponse1.setImage_url(homeResponse.getImage_url());
          homeResponse1.setDistance(homeResponse.getDistance());
          homeResponse1.setLongitude(homeResponse.getLongitude());
          homeResponse1.setLatitude(homeResponse.getLatitude());
          homeResponse1.setCreated_on(homeResponse.getCreated_on());
          homeResponse1.setUpdated_on(homeResponse.getUpdated_on());
          homeResponse1.setId(homeResponse.getId());
          homeResponse1.setOwner_id(homeResponse.getOwner_id());
          homeResponse1.setRest_description(homeResponse.getRest_description());
          homeResponse1.setRest_image(homeResponse.getRest_image());
          homeResponse1.setStatus(homeResponse.getStatus());
          homeResponse1.setRest_name(homeResponse.getRest_name());
          homeResponse1.setRest_location(homeResponse.getRest_location());
          homeResponse1.setRest_type(homeResponse.getRest_type());
          homeResponse1.setZipcode(homeResponse.getZipcode());

          homeList.add(homeResponse1);
        }
      }
    }
      homeResponseArrayList.clear();
      homeResponseArrayList.addAll(homeList);
      homeAdapter.notifyDataSetChanged();
      //homeResponseArrayList.addAll(homeResponseArrayListClone);
    }

    private class MyPagerAdapter extends PagerAdapter {

      @Override public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.viewpager_homelayout, null);
        PorterShapeImageView imageView = (PorterShapeImageView) view.findViewById(R.id.iv_home_pager);
        imageView.setImageDrawable(getResources().getDrawable(viewpagerimage.get(position)));
        // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(view);
        //            imageView.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View v) {
        //                    startActivity(new Intent(getActivity(), Description.class));
        //                }
        //            });

        return view;
      }

      @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
      }

      @Override public int getCount() {
        return viewpagerimage.size();
      }

      @Override public boolean isViewFromObject(View view, Object object) {
        return (view == object);
      }
    }

    private void callHomeApi () {
      MyDialog.getInstance(getActivity()).showDialog();
      Retrofit retrofit = ApiClient.getClient();
      ApiInterface apiInterface = retrofit.create(ApiInterface.class);
      Call<CommonResponse> call = apiInterface.getHomeResult(
          SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.token),
          "" + gpsTracker.getLatitude(), "" + gpsTracker.getLongitude());

      call.enqueue(new Callback<CommonResponse>() {
        @Override public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
          MyDialog.getInstance(getActivity()).hideDialog();
          if (response.isSuccessful()) {
            if (response.body().getStatus().equals("SUCCESS")) {
              //here,we get all the response of Home api.using getter method,like getHome(),we got the response.here response is comeing in array form,so to store this we take arraylist type variable which is globally declared above
              homeResponseArrayList = (ArrayList<HomeResponse>) response.body().getHome();

              homeResponseArrayListClone = new ArrayList<HomeResponse>();
              arrayList = new ArrayList<>();
              homeResponseArrayListClone.addAll(homeResponseArrayList);
              arrayList.addAll(homeResponseArrayListClone);

              //
              homeInterface.getHomeList(arrayList);

              //here response is comeing in List form,so to store homeResponseArrayList,we declare homeResponseArrayList as blank list like  ArrayList<HomeResponse> homeResponseArrayList;
              //to call HomeAdapter on Home Fragment using constructor made in HomeAdapter class
              homeAdapter = new HomeAdapter(getActivity(), homeResponseArrayList, Home.this);
              // Attach the adapter to a recyclerview
              rv_home.setAdapter(homeAdapter);//on recyclerview we set the adapter.
            } else {
              Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
          }
        }

        @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
          MyDialog.getInstance(getActivity()).hideDialog();
        }
      });
    }

    public void callFavouriteApi (String restaurant_id, String rest_type){
      //MyDialog.getInstance(getActivity()).showDialog();
      Retrofit retrofit = ApiClient.getClient();
      ApiInterface apiInterface = retrofit.create(ApiInterface.class);
      Call<CommonResponse> call = apiInterface.getFavouriteResult(
          SharedPreferenceWriter.getInstance(getActivity()).getString(SharedPreferenceKey.token),
          restaurant_id, rest_type);

      call.enqueue(new Callback<CommonResponse>() {
        @Override public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
          //MyDialog.getInstance(getActivity()).hideDialog();
          if (response.isSuccessful()) {
            if (response.body().getStatus().equals("SUCCESS")) {
              //myFavAdap.notifyDataSetChanged();
              //Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();

            } else {
              Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
          }
        }

        @Override public void onFailure(Call<CommonResponse> call, Throwable t) {
          //MyDialog.getInstance(getActivity()).hideDialog();
        }
      });
    }

    public interface HomeInterface {
      public void getHomeList(ArrayList<HomeResponse> homeResponseArrayListsss);
    }

    public double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {

      double phi1 = lat1 * PI_RAD;

      double phi2 = lat2 * PI_RAD;

      double lam1 = long1 * PI_RAD;

      double lam2 = long2 * PI_RAD;


      return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));

    }

  }
