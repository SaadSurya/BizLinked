package com.application.lumaque.bizlinked.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.adapters.HomeItemAdapter;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.customViews.BottomTabLayout;
import com.application.lumaque.bizlinked.data_models.HomeItemDataModel;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.NetworkUtils;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements BottomTabLayout.BottomOptionSelectedInterface {

    ArrayList<HomeItemDataModel> itemArrayList = new ArrayList<>();
    HomeItemAdapter itemAdapter;
    @BindView(R.id.rvHome)
    RecyclerView rvHome;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;


    BasePreferenceHelper preferenceHelper;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        initializeAdapter();
        setHasOptionsMenu(false);
        getAllItems();
        getBaseActivity().toolbar.setTitle("BizLinked");
        swipeRefresh.setColorSchemeResources(R.color.appThemeColor);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllItems();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
*/


    public void initializeAdapter() {
        itemAdapter = new HomeItemAdapter(activityReference, itemArrayList);
        //rvHome.setItemAnimator(new ScaleInLeftAnimator());

        //rvHome.getItemAnimator().setAddDuration(2000);
//        rvHome.getItemAnimator().setRemoveDuration(2000);
//        rvHome.getItemAnimator().setMoveDuration(2000);
//        rvHome.getItemAnimator().setChangeDuration(2000);

        rvHome.setLayoutManager(new GridLayoutManager(activityReference, AppConstant.HOME_ITEM_COUNT));
        rvHome.setAdapter(itemAdapter);
        rvHome.setNestedScrollingEnabled(false);

        rvHome.addOnItemTouchListener(new RecyclerTouchListener(activityReference, rvHome,
                new ClickListenerRecycler() {
                    @Override
                    public void onClick(View view, int position) {

                        if (NetworkUtils.isNetworkAvailable(activityReference)) {
                            try {
                                BaseFragment className = Utils.getFragmentByName(activityReference, itemArrayList.get(position).getClassName());
                                activityReference.addSupportFragment(className, AppConstant.TRANSITION_TYPES.SLIDE, true);
                            } catch (Exception e) {
                                Utils.showToast(activityReference, activityReference.getString(R.string.will_be_implemented), AppConstant.TOAST_TYPES.INFO);
                                e.printStackTrace();
                            }

                        } else {
                            Utils.showSnackBar(activityReference, getContainerLayout(),
                                    activityReference.getResources().getString(R.string.no_network_available),
                                    ContextCompat.getColor(activityReference, R.color.grayColor));
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }
        ));


    }


    public void getAllItems() {

        itemArrayList = new ArrayList<>();

        HomeItemDataModel model;

      /*  model = new HomeItemDataModel();
        model.setId(1);
        model.setName("Profile");
        model.setImage("ic_customer_support");
        model.setClassName("bizlinked.ProfileTabsFragment");
        itemArrayList.add(model);


        model = new HomeItemDataModel();
        model.setId(2);
        model.setName("My BizLinks");
        model.setImage("ic_quotation_policy");
        model.setClassName("bizlinked.LinksTabsFragment");
        itemArrayList.add(model);*/


/*

        model = new HomeItemDataModel();
        model.setId(2);
        model.setName("Quotation/Policy");
        model.setImage("ic_quotation_policy");
        model.setClassName("QuotationFragment");
        itemArrayList.add(model);

        model = new HomeItemDataModel();
        model.setId(3);
        model.setName("Endorsement");
        model.setImage("ic_endorsement");
        model.setClassName("EndorsementFragment");
        itemArrayList.add(model);

        model = new HomeItemDataModel();
        model.setId(4);
        model.setName("Endorsement Status");
        model.setImage("ic_endorsement_status");
        model.setClassName("VoiceRecordingFrag");
        //model.setClassName("EndorsementStatusFragment");

        itemArrayList.add(model);


        model = new HomeItemDataModel();
        model.setId(5);
        model.setName("Claim Intimation");
        model.setImage("ic_claim_intimation");
        model.setClassName("ClaimIntimationListFragment");
        itemArrayList.add(model);

        model = new HomeItemDataModel();
        model.setId(5);
        model.setName("Claim Status");
        model.setImage("ic_claim_status");
        model.setClassName("ClaimStatusListFragment");
        itemArrayList.add(model);
*/


        itemAdapter.clearAllList();
        itemAdapter.addAllList(itemArrayList);

    }


    @Override
    public void onCustomBackPressed() {
        System.exit(0);
        getBaseActivity().finish();
    }

    @Override
    public void onOptionSelected(String className) {

        if (NetworkUtils.isNetworkAvailable(activityReference)) {
            try {
                BaseFragment fragmentName = Utils.getFragmentByName(activityReference, className);
                activityReference.addSupportFragment(fragmentName, AppConstant.TRANSITION_TYPES.SLIDE, true);
            } catch (Exception e) {
                Utils.showToast(activityReference, activityReference.getString(R.string.no_class_available), AppConstant.TOAST_TYPES.INFO);
                e.printStackTrace();
            }

        } else {
            Utils.showSnackBar(activityReference, getContainerLayout(),
                    activityReference.getResources().getString(R.string.no_network_available),
                    ContextCompat.getColor(activityReference, R.color.grayColor));
        }
    }
}

