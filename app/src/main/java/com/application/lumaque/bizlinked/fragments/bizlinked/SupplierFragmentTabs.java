package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;
import com.application.lumaque.bizlinked.fragments.HomeFragment;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.LinkListAdapter;
import com.application.lumaque.bizlinked.helpers.common.KeyboardHelper;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.Unbinder;

public class SupplierFragmentTabs extends BaseFragment implements SearchView.OnQueryTextListener,CustomRecyclerCallBacks {


    @BindView(R.id.searchView)
    LinearLayout searchViewLayout;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.tabView)
    LinearLayout tabViewLayout;


  @BindView(R.id.no_record)
    LinearLayout noRecord;

    SearchView searchView;
    MenuItem searchItem;


    ImageView closeButton ;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer ;


    @BindView(R.id.recyclerview)
    RecyclerView linkRecycler;
    @BindView(R.id.ProfileTabs)
    TabLayout ProfileTabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    SupplierFragmentTabs.ViewPagerAdapter adapter;
    Unbinder unbinder;
    String strQuery = "";
    int strSelectedTab = -1;
    ArrayList<CompanyHeadModel>  searchedLinkList;


    ArrayList<CompanyHeadModel> RequestsRecievedList = new ArrayList<>();
    ArrayList<CompanyHeadModel> LinkedList = new ArrayList<>();
    ArrayList<CompanyHeadModel> RequestsSentList = new ArrayList<>();
    private LinkListAdapter itemAdapter;

    @Override
    public void onCustomBackPressed() {

        if(searchViewLayout.getVisibility()==View.VISIBLE || noRecord.getVisibility()==View.VISIBLE ){

            noRecord.setVisibility(View.GONE);
            searchViewLayout.setVisibility(View.GONE);
            tabViewLayout.setVisibility(View.VISIBLE);
            mShimmerViewContainer.setVisibility(View.GONE);
            strQuery = "";
            getLinks();
            searchView.setQuery("", false);
            searchView.onActionViewCollapsed();
            searchItem.collapseActionView();
        }else {

            activityReference.addSupportFragment(new HomeFragment(), AppConstant.TRANSITION_TYPES.FADE,false);
           // activityReference.onPageBack();
        }
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_profile_tabs;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        setHasOptionsMenu(true);
        getBaseActivity().toolbar.setTitle("Supplier");

        viewPager.setOffscreenPageLimit(1);
        if(strQuery.length()>0){


            searchView.setQuery(strQuery, false);
            searchFromServer(strQuery);
        }
      else
          getLinks();



        swipeRefresh.setColorSchemeResources(R.color.appThemeColor);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {





                    searchFromServer(strQuery);




                swipeRefresh.setRefreshing(false);
            }
        });


    }


    private void setupTabLayout() {
        adapter = new SupplierFragmentTabs.ViewPagerAdapter(getChildFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(LinkedListFragmentSuppier.getInstance(activityReference, preferenceHelper,LinkedList), activityReference.getString(R.string.my_bizlinks));
        adapter.addFragment(RequestedListFragmentSupplier.getInstance(activityReference, preferenceHelper,RequestsRecievedList), activityReference.getString(R.string.my_request));
        adapter.addFragment(RequestSentListFragmentSupplier.getInstance(activityReference, preferenceHelper,RequestsSentList), activityReference.getString(R.string.my_sent));
       // adapter.addFragment(LinkedListFragment.getInstance(activityReference, preferenceHelper,RequestsSentList), activityReference.getString(R.string.my_sent));
      //  adapter.addFragment(SupplierTabsFragment.getInstance(activityReference, preferenceHelper), activityReference.getString(R.string.supplier));
        //adapter.addFragment(SupplierTabsFragment.getInstance(activityReference, preferenceHelper), activityReference.getString(R.string.supplier));
        viewPager.setAdapter(adapter);

        ProfileTabs.setupWithViewPager(viewPager);
    }



    private void getLinks(){

        mShimmerViewContainer.startShimmerAnimation();

        noRecord.setVisibility(View.GONE);
        searchViewLayout.setVisibility(View.GONE);
        tabViewLayout.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));


        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.LINKED_SUPPLIER_URL,false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                // Utils.showToast(activityReference, "Logged in successfully...", AppConstant.TOAST_TYPES.SUCCESS);


                try {

                    JSONObject responseObj = new JSONObject(response);
                    JSONArray RequestsRecievedArray = responseObj.getJSONArray("RequestsRecieved");
                    JSONArray LinkedArray = responseObj.getJSONArray("Linked");
                    JSONArray RequestsSentArray = responseObj.getJSONArray("RequestsSent");

                    String RequestsRecievedArrayString = RequestsRecievedArray.toString();
                    String LinkedArrayString = LinkedArray.toString();
                    String RequestsSentArrayString = RequestsSentArray.toString();


                    RequestsSentList = GsonHelper.GsonToCompanyProfileList(RequestsSentArrayString);
                    LinkedList =  GsonHelper.GsonToCompanyProfileList(LinkedArrayString);
                    RequestsRecievedList =  GsonHelper.GsonToCompanyProfileList(RequestsRecievedArrayString);
                    setupTabLayout();

                    if(strSelectedTab != -1)
                        viewPager.setCurrentItem(strSelectedTab,true);
                    strSelectedTab = -1;
                    noRecord.setVisibility(View.GONE);
                    searchViewLayout.setVisibility(View.GONE);
                    tabViewLayout.setVisibility(View.VISIBLE);
                    noRecord.setVisibility(View.GONE);
                    searchViewLayout.setVisibility(View.GONE);
                    tabViewLayout.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //JSONArray recordsArray = responseObj.getJSONArray("records");
                // String recordsArrayString = recordsArray.toString();




            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });




    }
    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        KeyboardHelper.hideSoftKeyboard(activityReference);
        searchFromServer(query);

        strQuery = query;
         strSelectedTab = viewPager.getCurrentItem();
/*
        Utils.showSnackBar(activityReference,rootView,query,
                ContextCompat.getColor(activityReference, R.color.grayColor));*/

        return true;
    }
    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    @Override
    public void onImageClick(CompanyHeadModel companyHeadModel) {

    }

    @Override
    public void onActionClick(CompanyHeadModel companyHeadModel, String tag) {
        onActionBtnClick(companyHeadModel,tag);
    }

    @Override
    public void onListClick(CompanyHeadModel companyHeadModel) {


        ViewProfileFragment viewprofilefragment = new ViewProfileFragment();
        viewprofilefragment.setViewID(companyHeadModel.getCompanyID(),"supplier");
        activityReference.addSupportFragment(viewprofilefragment, AppConstant.TRANSITION_TYPES.SLIDE,true);
    }
    
    
    
    // Adapter for the viewpager using FragmentStatePagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter   {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
           // return  getChildFragmentManager().findFragmentByTag(getFragmentTag(R.id.viewPager,position));
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }


        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }
    }




    protected void onActionBtnClick(CompanyHeadModel companyHeadModel,String tag){


        String msgString = null;
        String URL = null;
        switch (companyHeadModel.getLinkStatus()){


            case "N":
                URL =  AppConstant.ServerAPICalls.SEND_SUPPLIER_REQ_URL;

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

      /*  case "C":
            URL =  AppConstant.ServerAPICalls.CANCEL_REQ_URL;
        break;*/

        }



        final HashMap<String, String> params = new HashMap<>();

        params.put("CompanyID", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));
        params.put("LinkCompanyID",companyHeadModel.getCompanyID());
        params.put("LinkType",companyHeadModel.getLinkType());
        params.put("LinkStatus",companyHeadModel.getLinkStatus());

        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetails(
                Request.Method.POST,
                params, URL, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {

                        if(strQuery.length()> 0) {
                            searchFromServer(strQuery);


                        }
                        else {
                            getLinks();

                        }
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
    private void searchFromServer(String query){

        mShimmerViewContainer.startShimmerAnimation();
        noRecord.setVisibility(View.GONE);
        searchViewLayout.setVisibility(View.GONE);
        tabViewLayout.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));
        params.put("searchTerm", query);
        params.put("forLinkType", "S");

        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.SEARCH_COMPANY,true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                noRecord.setVisibility(View.GONE);
                searchViewLayout.setVisibility(View.VISIBLE);
                tabViewLayout.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.GONE);
                searchedLinkList =  GsonHelper.GsonToCompanyProfileList(response);



                noRecord.setVisibility(View.GONE);
                searchViewLayout.setVisibility(View.VISIBLE);
                tabViewLayout.setVisibility(View.GONE);
                mShimmerViewContainer.setVisibility(View.GONE);
                initializeAdapter();

            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });






    }




    public void initializeAdapter() {

     if(searchedLinkList.size()==0){
         noRecord.setVisibility(View.VISIBLE);
         searchViewLayout.setVisibility(View.GONE);
         tabViewLayout.setVisibility(View.GONE);
         mShimmerViewContainer.setVisibility(View.GONE);


     }
        itemAdapter = new LinkListAdapter(activityReference, searchedLinkList, this);


        linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
        linkRecycler.setAdapter(itemAdapter);
        linkRecycler.setNestedScrollingEnabled(false);

/*

        linkRecycler.addOnItemTouchListener(new RecyclerTouchListener(activityReference, linkRecycler,
                new ClickListenerRecycler() {
                    @Override
                    public void onClick(View view, int position) {
                        if (NetworkUtils.isNetworkAvailable(activityReference)) {

                            ViewProfileFragment viewprofilefragment = new ViewProfileFragment();
                            viewprofilefragment.setViewID(searchedLinkList.get(position).getCompanyID());
                            activityReference.addSupportFragment(viewprofilefragment, AppConstant.TRANSITION_TYPES.SLIDE,true);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }
        ));
*/













    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ex7, menu);


        searchItem = menu.findItem(R.id.action_search);
         searchView = (SearchView) searchItem.getActionView();

      //  searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);


        if(strQuery.length()>0){
            searchView.onActionViewExpanded();
            searchItem.expandActionView();
            searchView.setQuery(strQuery, false);
        }
        closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!strQuery.equalsIgnoreCase("")){
                    searchView.setQuery("", false);
                    searchView.onActionViewCollapsed();
                    searchItem.collapseActionView();
                    getLinks();
                    strQuery = "";
                    noRecord.setVisibility(View.GONE);
                    searchViewLayout.setVisibility(View.GONE);
                    tabViewLayout.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
                else {
                    strQuery = "";
                    searchView.setQuery("", false);
                    searchView.onActionViewCollapsed();
                    searchItem.collapseActionView();
                    noRecord.setVisibility(View.GONE);
                    searchViewLayout.setVisibility(View.GONE);
                    tabViewLayout.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                }

            }
        });

    }








    public void refresAllLists(){







        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));


        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.LINKED_SUPPLIER_URL,true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                // Utils.showToast(activityReference, "Logged in successfully...", AppConstant.TOAST_TYPES.SUCCESS);


                try {

                    JSONObject responseObj = new JSONObject(response);
                    JSONArray RequestsRecievedArray = responseObj.getJSONArray("RequestsRecieved");
                    JSONArray LinkedArray = responseObj.getJSONArray("Linked");
                    JSONArray RequestsSentArray = responseObj.getJSONArray("RequestsSent");

                    String RequestsRecievedArrayString = RequestsRecievedArray.toString();
                    String LinkedArrayString = LinkedArray.toString();
                    String RequestsSentArrayString = RequestsSentArray.toString();


                    RequestsSentList = GsonHelper.GsonToCompanyProfileList(RequestsSentArrayString);
                    LinkedList =  GsonHelper.GsonToCompanyProfileList(LinkedArrayString);
                    RequestsRecievedList =  GsonHelper.GsonToCompanyProfileList(RequestsRecievedArrayString);






                    switch ( viewPager.getCurrentItem()){


                        case 0:

                            LinkedListFragmentSuppier linkedlistfragment = (LinkedListFragmentSuppier) adapter.getItem(0);
                            linkedlistfragment.refreshList(LinkedList);
                            break;
                        case 1:
                            RequestedListFragmentSupplier requestedlistfragment  = (RequestedListFragmentSupplier) adapter.getItem(1);
                            requestedlistfragment.refreshList(RequestsRecievedList);
                            break;
                        case 2:
                            RequestSentListFragmentSupplier requestsentlistfragment = (RequestSentListFragmentSupplier) adapter.getItem(2);
                            requestsentlistfragment.refreshList(RequestsSentList);
                            break;

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //JSONArray recordsArray = responseObj.getJSONArray("records");
                // String recordsArrayString = recordsArray.toString();



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
