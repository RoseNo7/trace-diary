package com.graduationwork.tracediary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.graduationwork.tracediary.Data.SharedPreference;

import java.util.List;

public class LocationInfo implements LocationListener {
    /* GPS 이용 변수 */
    public LocationManager locationManager = null;  /* LocationManager와 LocationListener 선언 */
    LocationListener locationListener = null;

    /* Reverse Geocoder 변수 */
    Geocoder geocoder;
    List<Address> list;                                 /* 위도, 경도에 대한 List */

    Location location = null;                         /* Loccation 값을 받아올 변수 */

    boolean isGPSEnabled = false;                   /* GPS와 Network로 정보를 받을 수 있는지 */
    boolean isNetworkEnabled = false;

    Context context;

    /* 생성자 */
    public LocationInfo(Context context) {
        this.context = context;
    }

    /* LocationListiener Override */
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


    /* GPS 위치 값 얻기 */
    public Location getLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

         /* 권한 확인 */
        boolean fine_Location = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        boolean coarse_Location = (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);

        if (fine_Location && coarse_Location) {

            /* GPS정보를 가져올 수 있는 경우 */
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /* LocationListener에 대한 객체 생성 */
            locationListener = new LocationInfo(context);

            try {
                    /* GPS가 꺼져있는 경우 */
                if (!isGPSEnabled && !isNetworkEnabled) {

                    /* GPS가 설정되어 있는 경우 */
                } else {
                    if (isGPSEnabled) {

                            /* GPS_Provider를 이용하여 위치정보 업데이트 */
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        if (locationManager != null) {
                                /* GPS_PROVIDER를 이용하여 마지막으로 가져온 위치 값 */
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                /* 위치값을 가져 온 경우 */
                            if (location != null) {
                                     /* 위도와 경도 값 저장 */
                            }
                        }
                    }

                    if (isNetworkEnabled) {

                            /* GPS로 인해 값을 가져오지 못 했을 경우 */
                        if (location == null) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                            if (locationManager != null) {

                                    /* NETWORK_PROVIDER를 이용하여 마지막으로 가져온 위치 값 */
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                                    /* 위치 값을 가져 온 경우 */
                                if (location != null) {
                                        /* 위도와 경도 값 저장 */
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /* locationListener 자원 해제 */
            locationManager.removeUpdates(locationListener);

        }

        return location;
    }

    /* 역 지오코딩 (reverse geocoding) */
    public String reverseGeocoding() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        String netType = net.getTypeName();

        /* Network가 연결되있는 경우 */
        if ((netType.equals("MOBILE")) || (netType.equals("WIFI"))) {
            geocoder = new Geocoder(context);

            try {
                list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list.get(0).getAddressLine(0);
    }

    /* 거리 차이 */
    public Float getDistance() {
        /* 반환 타입 : Long으로 Doublie로 바꿔줘야함  */
        Location mean_lastLocation = new Location("");

        mean_lastLocation.setLatitude(Double.longBitsToDouble(SharedPreference.getSharedLatitude(context)));
        mean_lastLocation.setLongitude(Double.longBitsToDouble(SharedPreference.getSharedLongitude(context)));

        return mean_lastLocation.distanceTo(location);
    }
}