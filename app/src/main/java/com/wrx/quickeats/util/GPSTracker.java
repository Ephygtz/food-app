package com.wrx.quickeats.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by mobulous51 on 17/3/17.
 */

public class GPSTracker  implements LocationListener {
    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location = null;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 10 seconds

    protected LocationManager locationManager;
    private Location m_Location;
    Activity mActivity;

    public GPSTracker(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        m_Location = getLocation();
//        System.out.println("location Latitude:" + m_Location.getLatitude());
//        System.out.println("location Longitude:" + m_Location.getLongitude());
//        System.out.println("getLocation():" + getLocation());
    }

    public Location getLocation() {


        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //          }

            try {
                locationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);

                isGPSEnabled = locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                isNetworkEnabled = locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                            else {
                               getLocation();
                                return null;
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                } else {
                                    getLocation();
                                    return null;
                                }
                            }
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        return location;
    }





    public void stopUsingGPS() {
//        if (locationManager != null) {
//            locationManager.removeUpdates(GPSTracker.this);
//        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if(location!= null){
          longitude = location.getLongitude();
        latitude = location.getLatitude();
}
}

//private final LocationListener locationListener = new LocationListener() {
//    public void onLocationChanged(Location location) {
//        longitude = location.getLongitude();
//        latitude = location.getLatitude();
//    }
//};
//
//
//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }


}


