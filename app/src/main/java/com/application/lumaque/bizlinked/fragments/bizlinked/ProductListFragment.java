package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductList;
import com.application.lumaque.bizlinked.fragments.HomeFragment;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.CategoryHorizontalAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.ProductAdapter;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.HashMap;

import butterknife.BindView;

public class ProductListFragment extends BaseFragment {

    CategoryHorizontalAdapter categoryItemAdapter;
    ProductAdapter productItemAdapter;



    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer ;


 @BindView(R.id.mainlayout)
 ConstraintLayout mainlayout ;



    @BindView(R.id.category_rv)
    RecyclerView rvCategory;


    @BindView(R.id.product_rv)
    RecyclerView rvProduct;

    @Override
    public void onCustomBackPressed() {
        activityReference.addSupportFragment(new HomeFragment(), AppConstant.TRANSITION_TYPES.FADE,false);
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        getBaseActivity().toolbar.setTitle("Product");
        initializeViews();


    }

    private void initializeViews() {
        mShimmerViewContainer.startShimmerAnimation();

        mainlayout.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);



        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));


        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_LISTER,false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                mShimmerViewContainer.stopShimmerAnimation();
                mainlayout.setVisibility(View.VISIBLE);
                mShimmerViewContainer.setVisibility(View.GONE);

                ProductList ProductList =  GsonHelper.GsonToProductList(activityReference, response);



                if(ProductList.getProductCategory().size()==0){

                    rvCategory.setVisibility(View.GONE);
                }else {
                    categoryItemAdapter = new CategoryHorizontalAdapter(activityReference, ProductList.getProductCategory());
                    rvCategory.setHasFixedSize(true);
                    rvCategory.setLayoutManager(new LinearLayoutManager(activityReference, LinearLayoutManager.HORIZONTAL, false));
                    // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
                    rvCategory.setAdapter(categoryItemAdapter);
                    rvCategory.setNestedScrollingEnabled(false);
                }



                productItemAdapter= new ProductAdapter(activityReference, ProductList.getProduct());
                StaggeredGridLayoutManager gridLayoutManager =
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvProduct.setLayoutManager(gridLayoutManager);
                // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
                rvProduct.setAdapter(productItemAdapter);
                rvProduct.setNestedScrollingEnabled(false);









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
