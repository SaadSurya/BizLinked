package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.LinkListAdapter;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LinkedListFragment extends Fragment implements CustomRecyclerCallBacks  {



    private static LinkedListFragment linkedlistfragment;

    ArrayList<CompanyHeadModel> LinkList;
    LinkListAdapter itemAdapter;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;



    @BindView(R.id.recyclerview)
    RecyclerView linkRecycler;
    Unbinder unbinder;
    BaseActivity activityReference;
    BasePreferenceHelper preferenceHelper;
    View rootView;
    public static LinkedListFragment getInstance(BaseActivity activityReference, BasePreferenceHelper preferenceHelper,ArrayList<CompanyHeadModel> LinkList ) {
        if (linkedlistfragment == null) {
            linkedlistfragment = new LinkedListFragment();
        }
            linkedlistfragment.activityReference = activityReference;
            linkedlistfragment.preferenceHelper = preferenceHelper;
            linkedlistfragment.LinkList = LinkList;

        return linkedlistfragment;
    }






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_linked_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initializeAdapter();
        setHasOptionsMenu(true);
        return rootView;

    }

    @Override
    public void onImageClick(CompanyHeadModel companyHeadModel) {



    }

    @Override
    public void onListClick(CompanyHeadModel companyHeadModel) {


        ViewProfileFragment viewprofilefragment = new ViewProfileFragment();
        viewprofilefragment.setViewID(companyHeadModel.getCompanyID(),"customer");
        activityReference.addSupportFragment(viewprofilefragment, AppConstant.TRANSITION_TYPES.SLIDE,true);
    }

    @Override
    public void onActionClick(CompanyHeadModel companyHeadModel, String tag) {
        if(getParentFragment() instanceof SupplierFragmentTabs){

            ((SupplierFragmentTabs)getParentFragment()).onActionBtnClick(companyHeadModel,tag);
        }
        else if(getParentFragment() instanceof CustomerFragmentTabs){
            ((CustomerFragmentTabs)getParentFragment()).onActionBtnClick(companyHeadModel,tag);
        }
    }


    public void initializeAdapter() {
        itemAdapter = new LinkListAdapter(activityReference, LinkList, this);
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
                            viewprofilefragment.setViewID(LinkList.get(position).getCompanyID());
                            activityReference.addSupportFragment(viewprofilefragment, AppConstant.TRANSITION_TYPES.SLIDE,true);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }
        ));
*/












        swipeRefresh.setColorSchemeResources(R.color.appThemeColor);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(getParentFragment() instanceof SupplierFragmentTabs){

                    ((SupplierFragmentTabs)getParentFragment()).refresAllLists();
                }
                else if(getParentFragment() instanceof CustomerFragmentTabs){
                    ((CustomerFragmentTabs)getParentFragment()).refresAllLists();
                }



                swipeRefresh.setRefreshing(false);
            }
        });



    }




    public void refreshList( ArrayList<CompanyHeadModel> List){

        LinkList=List;
        itemAdapter.clearAllList();
        itemAdapter.addAllList(LinkList);


    }
}
