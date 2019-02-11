package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.PlacesModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.map.MapFragment;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.listener.MapReadyListener;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class ViewProfileFragment extends BaseFragment {


private String profileID;
private String strReqType;
    MapFragment mapFragment;



    CompanyProfileModel companyProfileModel;
    @BindView(R.id.flCaptureImage)
    ImageView flCaptureImage1;



    @BindView(R.id.products)
    TextView tvproducts;


    @BindView(R.id.et_user_link)
    TextView tvLink;


    @BindView(R.id.et_user_un_link)
    TextView tvUnLink;

    @BindView(R.id.et_user_name)
    TextView tvName;


    @BindView(R.id.textView)
    TextView tvPhone;

    @BindView(R.id.textView2)
    TextView tvPhone2;

    @BindView(R.id.textView3)
    TextView tvEmail;
    @BindView(R.id.textView4)
    TextView tvWebsite;
    @BindView(R.id.textView5)
    TextView tvAddress;

@BindView(R.id.mainlayout)
ConstraintLayout mainLayout;


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer ;



    @Override
    public void onCustomBackPressed() {
        getBaseActivity().toolbar.setTitle("BizLinked");
        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_profile_view;
    }




    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        getBaseActivity().toolbar.setTitle("Profile");
        if(profileID.equalsIgnoreCase(String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID())))
        setHasOptionsMenu(true);
        else
            setHasOptionsMenu(false);
        getImages(AppConstant.ServerAPICalls.GET_MEDIA_FILE+profileID);
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
        getProfile();


    }

    private void getImages(String URL) {




//        ImageView ivDocumentImage = flCaptureImage1.findViewById(R.id.ivDocumentImage);
//        setVisibilityOfImageView(true,ivDocumentImage);
        Glide.with(this).load(URL)
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())).placeholder(R.drawable.profile))
                .into(flCaptureImage1);




    }




    private void setVisibilityOfImageView(boolean isVisible, View view) {
        //ImageView imageView = view.findViewById(R.id.ivDocumentImage);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void getProfile(){

        HashMap<String, String> params = new HashMap<>();
        params.put("companyId",profileID);
        params.put("currentCompanyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));

        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.GET_COMPANYPROFILE,false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                // Utils.showToast(activityReference, "Logged in successfully...", AppConstant.TOAST_TYPES.SUCCESS);
                companyProfileModel =  GsonHelper.GsonToCompanyProfile(response);
                //JSONArray recordsArray = responseObj.getJSONArray("records");
                // String recordsArrayString = recordsArray.toString();



                mapFragment = new MapFragment(activityReference, new MapReadyListener() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if(isAdded()&&isVisible()) {
                            setSelectedData();
                        }

                    }
                });
                activityReference.addSupportFragmentWithContainerView(mapFragment, R.id.mapContainer);


              //  setSelectedData();

            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });


























    }


public void setViewID(String id,String reqType){


    profileID = id;
    strReqType = reqType;

};



    private void setSelectedData() {

        tvName.setText(companyProfileModel.getCompanyName());
                tvPhone.setText(companyProfileModel.getContactNo());
        tvPhone2.setText(companyProfileModel.getPhoneNo());
        tvEmail.setText(companyProfileModel.getEmailAddress());
                tvWebsite.setText(companyProfileModel.getWebsite());


switch (String.valueOf(companyProfileModel.getLinkStatus())){


    case "L":
        tvLink.setText("Unlink");
        tvLink.setVisibility(View.VISIBLE);
        tvUnLink.setVisibility(View.GONE);
        break;
    case "N":
        tvLink.setText("Link");
        tvLink.setVisibility(View.VISIBLE);
        tvUnLink.setVisibility(View.GONE);
        break;
        case "S":
        tvLink.setText("Delete Request");
            tvLink.setVisibility(View.VISIBLE);
            tvUnLink.setVisibility(View.GONE);
        break;
        case "R":
        tvLink.setText("accept");
            tvUnLink.setText("Reject");
            tvLink.setVisibility(View.VISIBLE);
            tvUnLink.setVisibility(View.VISIBLE);
        break;
        default:
            tvLink.setText("Link");
            tvLink.setVisibility(View.VISIBLE);
            tvUnLink.setVisibility(View.GONE);
            break;




}


        if(profileID.equalsIgnoreCase(String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID())))
        {  tvLink.setVisibility(View.GONE);
            tvUnLink.setVisibility(View.GONE);
        }



           if(!(companyProfileModel.getShopNo()== null && companyProfileModel.getMarket()== null && companyProfileModel.getArea()== null &&companyProfileModel.getCityID()== null ))
        tvAddress.setText("Shop # "+ companyProfileModel.getShopNo()==null?"":companyProfileModel.getShopNo()+ " "+companyProfileModel.getMarket()==null?"":companyProfileModel.getMarket()
                + " "+companyProfileModel.getArea()==null?"":companyProfileModel.getArea()+ " "+companyProfileModel.getCityName()==null?"":companyProfileModel.getCityName());





        PlacesModel placesModel = new PlacesModel();

        placesModel.setLat(companyProfileModel.getLatitude());
        placesModel.setLng(companyProfileModel.getLongitude());


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (mapFragment != null &&( placesModel.getLat()!= 0 && placesModel.getLng() != 0))
            mapFragment.addMarker(placesModel);
//            }
//        }, 2000);
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_profile_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_view_profile);
        final TextView viewProfile = (TextView) MenuItemCompat.getActionView(item);
        viewProfile.setText("Edit Profile");
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ProfileTabsFragment profileTabsFragment = new ProfileTabsFragment();

                activityReference.addSupportFragment(profileTabsFragment, AppConstant.TRANSITION_TYPES.SLIDE,true);

            }
        });

    }
    @OnClick({R.id.et_user_link,R.id.et_user_un_link,R.id.products})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_user_link:
                onActionBtnClick(companyProfileModel,view.getTag().toString());
                break;
            case R.id.et_user_un_link:
                onActionBtnClick(companyProfileModel,view.getTag().toString());

                break; case R.id.products:

                Bundle bundle = new Bundle();
                bundle.putString(ProductListFragment.companyId, profileID);
                bundle.putString(ProductListFragment.productCategoryId, "");
                ProductListFragment ProductListFragment = new ProductListFragment();
                ProductListFragment.setArguments(bundle);
                activityReference.addSupportFragment(ProductListFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);


                break;

        }
    }
    protected void onActionBtnClick(CompanyProfileModel companyHeadModel, String tag){
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);

        String msgString = null;
        String URL = null;
        switch (String.valueOf(companyHeadModel.getLinkStatus())){
/*
            if(strReqType.equalsIgnoreCase("supplier"))
                URL =  AppConstant.ServerAPICalls.SEND_SUPPLIER_REQ_URL;
            else if(strReqType.equalsIgnoreCase("customer"))
                URL =  AppConstant.ServerAPICalls.SEND_CUSTOMER_REQ_URL;
*/


            case "N":
                if(strReqType.equalsIgnoreCase("supplier"))
                URL =  AppConstant.ServerAPICalls.SEND_SUPPLIER_REQ_URL;
               else
                    URL =  AppConstant.ServerAPICalls.SEND_CUSTOMER_REQ_URL;

                break;
            case "S":
                URL =  AppConstant.ServerAPICalls.CANCEL_REQ_URL;
                break;
            case "L":
                URL =  AppConstant.ServerAPICalls.UNLINKED_REQ_URL;
                break;
            case "R":
                if(tag.equalsIgnoreCase("add"))
                    URL =  AppConstant.ServerAPICalls.Accept_REQ_URL;
                else
                    URL =  AppConstant.ServerAPICalls.IGNORE_REQ_URL;
                break;
                default:
                    URL =  AppConstant.ServerAPICalls.SEND_SUPPLIER_REQ_URL;

                    break;

      /*  case "C":
            URL =  AppConstant.ServerAPICalls.CANCEL_REQ_URL;
        break;*/

        }



        final HashMap<String, String> params = new HashMap<>();

        params.put("CompanyID", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));
        params.put("LinkCompanyID", String.valueOf(companyHeadModel.getCompanyID()));
      /*  params.put("LinkType",companyHeadModel.getLinkType());
        params.put("LinkStatus",companyHeadModel.getLinkStatus());*/

        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetails(
                Request.Method.POST,
                params, URL, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        getProfile();
                        /*

                        if(strQuery.length()> 0) {
                            searchFromServer(strQuery);


                        }
                        else {
                            getLinks();

                        }*/
                        Utils.showSnackBar(activityReference,getView(),response,
                                ContextCompat.getColor(activityReference, R.color.grayColor));

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
}
