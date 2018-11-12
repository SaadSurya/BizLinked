package com.application.lumaque.bizlinked.activities;

import android.os.Handler;
import android.widget.ScrollView;
import android.widget.TextView;

import com.application.lumaque.bizlinked.BizLinkApplication;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.fireBase.BackgroundService;
import com.application.lumaque.bizlinked.fragments.SelectSigningFragment;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.daimajia.androidanimations.library.Techniques;

import butterknife.BindView;

public class RegistrationActivity extends BaseActivity {

    @BindView(R.id.ivLogo)
    TextView ivLogo;
    @BindView(R.id.fragmentContainer)
    ScrollView fragmentContainer;



    @Override
    public int getMainLayoutId() {
        return R.layout.activity_registration;
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected void onViewReady() {
        showHomeActivityIfRequired();
    }

    private void showHomeActivityIfRequired() {
        if (prefHelper.getLoginStatus()) {

           /* BizLinkApplication.setFireBaseReffID( "com_"+ prefHelper.getCompanyProfile().getCompanyID());
            BizLinkApplication.setLoginStatus(true);*/
            StartBackgroundService(BackgroundService.class);
            changeActivity(HomeActivity.class, true);
        } else {
            addFragment();
        }
    }

    private void addFragment() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationHelpers.animation(Techniques.BounceInUp, 600, ivLogo);
                AnimationHelpers.animation(Techniques.BounceInUp, 600, fragmentContainer);
               // AnimationHelpers.animation(Techniques.BounceInUp, 600, llFooterView);
                addSupportFragment(new SelectSigningFragment(), AppConstant.TRANSITION_TYPES.FADE,true);
            }
        }, 500);

    }
}
