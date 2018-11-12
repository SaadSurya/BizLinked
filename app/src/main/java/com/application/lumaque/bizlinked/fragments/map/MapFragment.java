package com.application.lumaque.bizlinked.fragments.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.application.lumaque.bizlinked.data_models.PlacesModel;
import com.application.lumaque.bizlinked.listener.MapReadyListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    public GoogleMap getmMap() {
        return mMap;
    }

public void setmMap(GoogleMap Map){

    mMap = Map;
}
    private GoogleMap mMap;
    private Marker marker;
    Context context;
    MapReadyListener mapListener;

    @SuppressLint("ValidFragment")
    public MapFragment(Context context, MapReadyListener listener) {
        this.context = context;
        mapListener = listener;
    }


    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void setUpMapIfNeeded() {

        if (mMap == null) {
            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapListener.onMapReady(googleMap);
        setUpMap();
    }

    private void setUpMap() {

        //mMap.setMyLocationEnabled(true);
        //mMap.setMapType(GoogleMap.MAP_TYPE_);
        mMap.getUiSettings().setMapToolbarEnabled(false);
//        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(30.3753,69.3451), 5);  // Open map on Pakistan Lat Long
//        mMap.animateCamera(location);
        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        //mMap.setMinZoomPreference(17f);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(false);
            return;
        }

//        mMap.getUiSettings().setRotateGesturesEnabled(true);
//        mMap.getUiSettings().setScrollGesturesEnabled(true);
//        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //or myMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);


//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng point) {
//
//                //remove previously placed Marker
//                if (marker != null) {
//                    marker.remove();
//                }
//
//                //place marker where user just clicked
//                marker = mMap.addMarker(new MarkerOptions().position(point).title("Marker")
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
//
//            }
//        });


    }



    public void addMarkers(ArrayList<PlacesModel> dataList, int zoomLevel) {
        if (mMap != null) {
            ArrayList<PlacesModel> data = new ArrayList<>();
            data.addAll(dataList);
            if (data.size() > 0) {
                mMap.clear();
                for (int index = 0; index < dataList.size(); index++) {
                    Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(dataList.get(index).getLat(), dataList.get(index).getLng())).title(dataList.get(index).getName()));
                    m.setTag(dataList.get(index));
                }

                LatLng coordinate = new LatLng(dataList.get(0).getLat(), dataList.get(0).getLng()); //Store these lat lng values somewhere. These should be constant.
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, zoomLevel);
                mMap.animateCamera(location);
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLat()), 14f));
            }
            //mMap.setMaxZoomPreference(14);

//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//
//                PlacesModel obj = (PlacesModel) marker.getTag();
//
//                // Getting view from the layout file info_window_layout
//                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
//
//                // Getting reference to the TextView to set title
//                ImageView tvPlaceImage = (ImageView) v.findViewById(R.id.tvPlaceImage);
//                TextView tvPlaceTitle = (TextView) v.findViewById(R.id.tvPlaceTitle);
//                TextView tvPlaceSubTitle = (TextView) v.findViewById(R.id.tvPlaceSubTitle);
//                TextView tvPlaceDescription = (TextView) v.findViewById(R.id.tvPlaceDescription);
//
//                tvPlaceTitle.setText(obj.getName());
//                tvPlaceSubTitle.setText(obj.getName());
//                //tvPlaceDescription.setText(obj.getName());
//                //tvPlaceImage.setImage()
//
//
//                // Returning the view containing InfoWindow contents
//                return v;
//            }
//        });
//
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
//                return false;
//            }
//        });
        }
    }

    public void addMarkerWithLatLong(double lat, double lon){
        if(mMap != null){

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));

            LatLng cameraPosition = new LatLng(lat, lon);
            CameraUpdate loc = CameraUpdateFactory.newLatLngZoom(cameraPosition,15);
            mMap.animateCamera(loc);
        }
    }



    public void addMarker(PlacesModel data) {

        if (mMap != null) {

            mMap.clear();

            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(data.getLat(), data.getLng())).title(data.getName()));
            m.setTag(data);


            LatLng coordinate = new LatLng(data.getLat(), data.getLng()); //Store these lat lng values somewhere. These should be constant.
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 19);
            mMap.animateCamera(location);
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dataList.get(0).getLat(), dataList.get(0).getLat()), 14f));

            //mMap.setMaxZoomPreference(14);

//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//
//                PlacesModel obj = (PlacesModel) marker.getTag();
//
//                // Getting view from the layout file info_window_layout
//                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
//
//                // Getting reference to the TextView to set title
//                ImageView tvPlaceImage = (ImageView) v.findViewById(R.id.tvPlaceImage);
//                TextView tvPlaceTitle = (TextView) v.findViewById(R.id.tvPlaceTitle);
//                TextView tvPlaceSubTitle = (TextView) v.findViewById(R.id.tvPlaceSubTitle);
//                TextView tvPlaceDescription = (TextView) v.findViewById(R.id.tvPlaceDescription);
//
//                tvPlaceTitle.setText(obj.getName());
//                tvPlaceSubTitle.setText(obj.getName());
//                //tvPlaceDescription.setText(obj.getName());
//                //tvPlaceImage.setImage()
//
//
//                // Returning the view containing InfoWindow contents
//                return v;
//            }
//        });
//
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                marker.showInfoWindow();
//                return false;
//            }
//        });
        }
    }

    public void clearMap(){
        if(mMap != null)
            mMap.clear();
    }
}