package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class NotificationSetting extends BaseFragment {
    @BindView(R.id.text)
    TextView text;


    @Override
    public void onCustomBackPressed() {
        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.my_text_view;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        text.setText("remove remember notify customer on publish");


    }


    @OnClick(R.id.text)
    public void onViewClicked() {


        preferenceHelper.putsaveNotify(false);

    }
}
