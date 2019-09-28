package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.Unbinder;

public class ProfileFragmentTabs extends BaseFragment {


    @BindView(R.id.searchView)
    LinearLayout searchViewLayout;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    @BindView(R.id.no_record)
    LinearLayout noRecord;

    @BindView(R.id.tabView)
    LinearLayout tabViewLayout;

    SearchView searchView;
    MenuItem searchItem;


    ImageView closeButton;

    @BindView(R.id.recyclerview)
    RecyclerView linkRecycler;
    @BindView(R.id.ProfileTabs)
    TabLayout ProfileTabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    ProfileFragmentTabs.ViewPagerAdapter adapter;
    Unbinder unbinder;
    String strQuery = "";
    int strSelectedTab = -1;
    ArrayList<CompanyHeadModel> searchedLinkList;

/*
    ArrayList<CompanyHeadModel> RequestsRecievedList = new ArrayList<>();
    ArrayList<CompanyHeadModel> LinkedList = new ArrayList<>();
    ArrayList<CompanyHeadModel> RequestsSentList = new ArrayList<>();
    private LinkListAdapter categoryItemAdapter;*/

    @Override
    public void onCustomBackPressed() {

    }

    /* @Override
        public void onCustomBackPressed() {

            if(searchViewLayout.getVisibility()==View.VISIBLE ||   noRecord.getVisibility()==View.VISIBLE ){
                noRecord.setVisibility(View.GONE);
                searchViewLayout.setVisibility(View.GONE);
                tabViewLayout.setVisibility(View.VISIBLE);

                strQuery = "";
                getLinks();
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();
                searchItem.collapseActionView();
            }else {
                activityReference.addSupportFragment(new HomeFragment(), AppConstant.TRANSITION_TYPES.FADE,false);
              //  activityReference.onPageBack();
            }
        }
    */
    @Override
    protected int getMainLayout() {
        return R.layout.fragment_profile_tabs;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        setHasOptionsMenu(true);
        getBaseActivity().toolbar.setTitle("Profile Edit");
        viewPager.setOffscreenPageLimit(1);
        setHasOptionsMenu(false);
        activityReference.getCurrentLocation();
        setupTabLayout();
    }


    private void setupTabLayout() {
        adapter = new ProfileFragmentTabs.ViewPagerAdapter(getChildFragmentManager());


        adapter.addFragment(ProfileDetailTabFragment.getInstance(activityReference, preferenceHelper), activityReference.getString(R.string.detail));
        adapter.addFragment(ProfileAddressTabFragment.getInstance(activityReference, preferenceHelper), activityReference.getString(R.string.address));


        viewPager.setAdapter(adapter);

        ProfileTabs.setupWithViewPager(viewPager);
    }


    // Adapter for the viewpager using FragmentStatePagerAdapter
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
        private Fragment mCurrentFragment;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
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


}
