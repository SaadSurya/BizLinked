package com.application.lumaque.bizlinked.activities;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;

public class StartUpActivity extends BaseActivity {


    @Override
    public int getMainLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return R.id.fragmentContainer;
    }

    @Override
    protected void onViewReady() {

    }
}
