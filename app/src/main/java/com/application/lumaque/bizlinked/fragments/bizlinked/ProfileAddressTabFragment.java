package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.CitiesModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.fragments.map.MapFragment;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.ui.dialogs.DialogFactory;
import com.application.lumaque.bizlinked.listener.MapReadyListener;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.daimajia.androidanimations.library.Techniques;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileAddressTabFragment extends Fragment {

    private static ProfileAddressTabFragment profileAddrssFragment;
    MapFragment mapFragment;
    ArrayList<CitiesModel> citiesList;
    Gson g = new Gson();
    @BindView(R.id.mapContainer)
    FrameLayout mpFrameLayout;
    //     @BindView(R.id.et_shop_num)
//     CustomEditText etShopNum;
    @BindView(R.id.et_shop_num)
    EditText etShopNum;
    @BindView(R.id.et_market)
    EditText etMarket;
    @BindView(R.id.et_area)
    EditText etArea;
    @BindView(R.id.sp_cities)
    Spinner spCities;
    @BindView(R.id.btn_current_location)
    ImageButton btnCurrentLocation;
    @BindView(R.id.btn_save)
    Button btSave;
    Marker marker;
    View rootView;
    MarkerOptions opt;
    LatLng latLng2;
    Unbinder unbinder;
    BaseActivity activityReference;
    BasePreferenceHelper preferenceHelper;

    @SuppressLint("ValidFragment")
    private ProfileAddressTabFragment() {
    }

    public static ProfileAddressTabFragment getInstance(BaseActivity activityReference, BasePreferenceHelper preferenceHelper) {
        if (profileAddrssFragment == null) {
            profileAddrssFragment = new ProfileAddressTabFragment();
        }
        profileAddrssFragment.activityReference = activityReference;
        profileAddrssFragment.preferenceHelper = preferenceHelper;

        return profileAddrssFragment;
    }

  /*  @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }*/

    @OnClick({R.id.btn_current_location, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_current_location:
                getCurrentLocation();
                break;
            case R.id.btn_save:
                onSave();

                break;

        }
    }

    private void setCustomLocation(LatLng latLng) {
        ProfileTabsFragment frag = ((ProfileTabsFragment) ProfileAddressTabFragment.this.getParentFragment());
        frag.updatedProfile.setLatitude(latLng.latitude);
        frag.updatedProfile.setLongitude(latLng.longitude);
    }

    private void getCurrentLocation() {

        DialogFactory.createMessageDialog(getActivity(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (activityReference.getLastLocation() != null) {
                    ProfileTabsFragment frag = ((ProfileTabsFragment) ProfileAddressTabFragment.this.getParentFragment());
                    frag.updatedProfile.setLatitude(activityReference.getLastLocation().getLatitude());
                    frag.updatedProfile.setLongitude(activityReference.getLastLocation().getLongitude());
                    //frag.saveDetailAndNext();
                    mapFragment.addMarkerWithLatLong(activityReference.getLastLocation().getLatitude(), activityReference.getLastLocation().getLongitude());
                }

            }
        }, getString(R.string.get_gps)).show();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_profile_addres, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        onMapReady();
        activityReference.addSupportFragmentWithContainerView(mapFragment, R.id.mapContainer);
        initializeViews();
        // setupAnimation(rootView);
        return rootView;
    }


    public void onMapReady() {
//        final BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_icon);
        mapFragment = new MapFragment(activityReference, new MapReadyListener() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                /*settings.setAllGesturesEnabled(false);
                settings.setMyLocationButtonEnabled(false);
                settings.setZoomControlsEnabled(false);
                */
                opt = new MarkerOptions();
                latLng2 = new LatLng(preferenceHelper.getCompanyProfile().getLatitude(), preferenceHelper.getCompanyProfile().getLongitude());
                opt.position(latLng2);
//                marker = googleMap.addMarker(opt);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                if (isAdded() && isVisible()) {
                    if (preferenceHelper.getCompanyProfile().getLatitude() != 0 && preferenceHelper.getCompanyProfile().getLongitude() != 0)
                        mapFragment.addMarkerWithLatLong(preferenceHelper.getCompanyProfile().getLatitude(), preferenceHelper.getCompanyProfile().getLongitude());
                }

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        // Get the center of the Map.
                        LatLng centerOfMap = googleMap.getCameraPosition().target;
                        // Update your Marker's position to the center of the Map.
                        googleMap.clear();
                        opt.position(centerOfMap);
//                        opt.icon(icon);
                        marker = googleMap.addMarker(opt);
                        marker.setPosition(centerOfMap);
                        setCustomLocation(centerOfMap);

                    }
                });


            }
        });
    }

    private void setupAnimation(final View rootView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int duration = 300;
                for (int index = 0; index < ((ViewGroup) rootView).getChildCount(); index++) {
                    AnimationHelpers.animation(Techniques.SlideInDown, duration, ((ViewGroup) rootView).getChildAt(index));
                    duration += 50;
                }
            }
        }, 500);
    }

    private void onSave() {
        ProfileTabsFragment frag = ((ProfileTabsFragment) this.getParentFragment());
        frag.updatedProfile.setShopNo(etShopNum.getText().toString());
        frag.updatedProfile.setMarket(etMarket.getText().toString());
        frag.updatedProfile.setArea(etArea.getText().toString());
        frag.updatedProfile.setCityID(citiesList.get(spCities.getSelectedItemPosition()).getCityID());
        // frag.updatedProfile.setBusinessNature(BN);
        String jsonString = g.toJson(frag.updatedProfile);
        //    Type type = new TypeToken<Map<String,String>>() {}.getType();
/*        Map<String,String> result =  new Gson().fromJson(jsonString, type);
        HashMap<String,String> params = new HashMap<>(result);*/
        profileSaveReq(jsonString);
    }

    public void doBack() {
        ProfileTabsFragment frag = ((ProfileTabsFragment) this.getParentFragment());
        frag.selectFirstFrag();
    }

    private void profileSaveReq(String jsonString) {

        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetailsJson(
                Request.Method.POST,
                jsonString, AppConstant.ServerAPICalls.SAVE_COMPANY_PROFILE, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        CompanyProfileModel companyprofile = GsonHelper.GsonToCompanyProfile(response);
                        preferenceHelper.putCompany(companyprofile);
                        Utils.showToast(activityReference, "Profile Updater", AppConstant.TOAST_TYPES.SUCCESS);
                        activityReference.updateDrawer();
                    }

                    @Override
                    public void onError(String response) {
                        //     Utils.showToast(activityReference, "error", AppConstant.TOAST_TYPES.SUCCESS);
                    }

                    @Override
                    public void onNoNetwork() {
                    }
                });
    }


    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private void initializeViews() {

        WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(null, AppConstant.ServerAPICalls.CITIES_URL, true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                citiesList = GsonHelper.GsonToCities(response);
                String[] majorCat = new String[citiesList.size() <= 0 ? 0 : citiesList.size()];

                for (int i = 0; i < majorCat.length; i++) {
                    majorCat[i] = citiesList.get(i).getCityName();
                }

                ArrayAdapter arrayAdapter = new ArrayAdapter(activityReference, android.R.layout.simple_spinner_dropdown_item, majorCat);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spCities.setAdapter(arrayAdapter);

                int defaultPos = 0;
                for (int i = 0; i < citiesList.size(); i++) {
                    if (citiesList.get(i).getCityID().equalsIgnoreCase(preferenceHelper.getCompanyProfile().getCityID()))
                        defaultPos = i;
                    spCities.setSelection(defaultPos);
                }
                etShopNum.setText(preferenceHelper.getCompanyProfile().getShopNo());
                etMarket.setText(preferenceHelper.getCompanyProfile().getMarket());
                etArea.setText(preferenceHelper.getCompanyProfile().getArea());
                //   spCities.setText(preferenceHelper.getCompanyProfile().getWebsite());
                ProfileTabsFragment frag = ((ProfileTabsFragment) (ProfileAddressTabFragment.this.getParentFragment()));
                frag.updatedProfile.setShopNo(etShopNum.getText().toString());
                frag.updatedProfile.setMarket(etMarket.getText().toString());
                frag.updatedProfile.setArea(etArea.getText().toString());
                frag.updatedProfile.setCityID(citiesList.get(spCities.getSelectedItemPosition()).getCityID());
                frag.updatedProfile.setLatitude(preferenceHelper.getCompanyProfile().getLatitude());
                frag.updatedProfile.setLongitude(preferenceHelper.getCompanyProfile().getLongitude());

            }

            @Override
            public void onError(String response) {
            }

            @Override
            public void onNoNetwork() {

            }
        });
    }


}
