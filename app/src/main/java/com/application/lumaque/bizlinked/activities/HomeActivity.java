package com.application.lumaque.bizlinked.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.fragments.HomeFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.ViewProfileFragment;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {


    @BindView(R.id.fragmentContainer)
    FrameLayout fragmentContainer;


    @Override
    public int getMainLayoutId() {
        return R.layout.activity_home_drawer;
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected void onViewReady() {

        setFireBase();

        //addSupportFragment(new LinksTabsFragment(), AppConstant.TRANSITION_TYPES.FADE);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            addSupportFragment(new HomeFragment(), AppConstant.TRANSITION_TYPES.FADE, true);
        } else {

            if (prefHelper.getLoginStatus()) {
                String VIEWID = extras.getString("VIEWID");
                ViewProfileFragment viewprofilefragment = new ViewProfileFragment();
                viewprofilefragment.setViewID(VIEWID, "customer");
                addSupportFragment(viewprofilefragment, AppConstant.TRANSITION_TYPES.SLIDE, false);
            } else {
                changeActivity(RegistrationActivity.class, true);

            }

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
