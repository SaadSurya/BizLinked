package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.MyAdapter;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;

import butterknife.BindView;

public class SettingFragment extends BaseFragment {


    @BindView(R.id.setting_list)
    RecyclerView settingList;
    // Unbinder unbinder;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCustomBackPressed() {
        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {

        return R.layout.setting_layout;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {


        settingList.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(activityReference);
        settingList.setLayoutManager(layoutManager);
        String[] myDataset = {"Notification setting"};
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        settingList.setAdapter(mAdapter);


        settingList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), settingList,
                new ClickListenerRecycler() {
                    @Override
                    public void onClick(View view, final int position) {

                        NotificationSetting categoryListFragment = new NotificationSetting();
                        activityReference.addSupportFragment(categoryListFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);


                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }
        ));

    }


}
