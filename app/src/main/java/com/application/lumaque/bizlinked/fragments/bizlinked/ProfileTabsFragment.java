package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.Unbinder;

public class ProfileTabsFragment extends BaseFragment {


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.tabView)
    LinearLayout tabViewLayout;

    @BindView(R.id.ProfileTabs)
    TabLayout ProfileTabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    Unbinder unbinder;
    CompanyProfileModel updatedProfile = new CompanyProfileModel();


    public ProfileTabsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCustomBackPressed() {


        if (adapter.getCurrentFragment() instanceof ProfileAddressTabFragment) {
            ((ProfileAddressTabFragment) adapter.getCurrentFragment()).doBack();

        } else if (adapter.getCurrentFragment() instanceof ProfileDetailTabFragment) {
            ((ProfileDetailTabFragment) adapter.getCurrentFragment()).doBack();

        }
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_profile_tabs;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        getBaseActivity().toolbar.setTitle("Edit Profile");
        setHasOptionsMenu(false);
        tabViewLayout.setVisibility(View.VISIBLE);
        mShimmerViewContainer.setVisibility(View.GONE);
        activityReference.getCurrentLocation();
        setupTabLayout();
    }


    public void selectFirstFrag() {

        viewPager.setCurrentItem(0, true);

    }

    private void setupTabLayout() {

        adapter = new ProfileTabsFragment.ViewPagerAdapter(getChildFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(ProfileDetailTabFragment.getInstance(activityReference, preferenceHelper), activityReference.getString(R.string.detail));
        adapter.addFragment(ProfileAddressTabFragment.getInstance(activityReference, preferenceHelper), activityReference.getString(R.string.address));
        viewPager.setAdapter(adapter);

        ProfileTabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position == 1) {


                }
                String currentTab = "abc";
                //(viewPager.getCurrentItem() instanceof ProfileAddressTabFragment)
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void saveDetailAndNext() {

        viewPager.setCurrentItem(1, true);

    }

    // Adapter for the viewpager using FragmentStatePagerAdapter
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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


     /*   @Override
        public void finishUpdate(ViewGroup container) {
            try{
                super.finishUpdate(container);
            } catch (NullPointerException nullPointerException){
                System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
            }
        }*/

    }


}
