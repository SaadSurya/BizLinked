package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductList;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.CategoryHorizontalAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.ProductAdapter;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.network.NetworkUtils;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;
import com.application.lumaque.bizlinked.webhelpers.CompanyHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.HashMap;

import butterknife.BindView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class ProductListFragment extends BaseFragment {

    CategoryHorizontalAdapter categoryItemAdapter;
    ProductAdapter productItemAdapter;

    public static final String companyId = "companyId";
    public static final String productCategoryId = "productCategoryId";

    String paramCompanyId = "";
    String paramProductCategoryId = "";

    Bundle bundle;


    ProductList ProductList;





  //  FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);


    @BindView(R.id.fab_speed_dial)
    FabSpeedDial fabSpeedDial;


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;


    @BindView(R.id.mainlayout)
    ConstraintLayout mainlayout;


    @BindView(R.id.category_rv)
    RecyclerView rvCategory;


    @BindView(R.id.product_desc_view)
    RecyclerView rvProduct;

    @Override
    public void onCustomBackPressed() {
        /*activityReference.addSupportFragment(new HomeFragment(), AppConstant.TRANSITION_TYPES.FADE,false);*/


        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        getBaseActivity().toolbar.setTitle("Product");
        setArguments();


        initializeViews();


    }


    private void setArguments() {
        bundle = getArguments();
        if (bundle != null) {
            paramCompanyId = bundle.getString(companyId);
            paramProductCategoryId = bundle.getString(productCategoryId);
        }
    }

    private void initializeViews() {
        mShimmerViewContainer.startShimmerAnimation();

        mainlayout.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);


        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", paramCompanyId);
        params.put("productCategoryId", paramProductCategoryId);

/*

        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));
        params.put("productCategoryId", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));

*/

        WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_LISTER, false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                mShimmerViewContainer.stopShimmerAnimation();
                mainlayout.setVisibility(View.VISIBLE);
                mShimmerViewContainer.setVisibility(View.GONE);

                ProductList = GsonHelper.GsonToProductList(activityReference, response);




                fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
                    @Override
                    public boolean onMenuItemSelected(MenuItem menuItem) {
                        //TODO: Start some activity

                        int id = menuItem.getItemId();
                        switch (id) {



                            case R.id.add_category:
                          Utils.showToast(activityReference,"ye hai new Style",AppConstant.TOAST_TYPES.INFO);
                            break;

                            case R.id.add_product:
                                Bundle bundleParam = new Bundle();
                                bundleParam.putString(ProductFragment.companyId, String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));
                                //bundle.putString(ProductFragment.productId, String.valueOf(ProductList.getProduct().get(position).getProductID()));
                                ProductFragment ProductFragment = new ProductFragment();
                                ProductFragment.setArguments(bundleParam);
                                activityReference.addSupportFragment(ProductFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);
                            break;

                        }

                        return false;
                    }
                });





                if (ProductList.getProductCategory().size() == 0) {

                    rvCategory.setVisibility(View.GONE);
                } else {
                    categoryItemAdapter = new CategoryHorizontalAdapter(activityReference, ProductList.getProductCategory());
                    rvCategory.setHasFixedSize(true);
                    rvCategory.setLayoutManager(new LinearLayoutManager(activityReference, LinearLayoutManager.HORIZONTAL, false));
                    // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
                    rvCategory.setAdapter(categoryItemAdapter);
                    rvCategory.setNestedScrollingEnabled(false);

                    rvCategory.addOnItemTouchListener(new RecyclerTouchListener(activityReference, rvCategory,
                            new ClickListenerRecycler() {
                                @Override
                                public void onClick(View view, int position) {

                                    if (NetworkUtils.isNetworkAvailable(activityReference)) {
                                        try {


                                            Bundle bundle = new Bundle();
                                            bundle.putString(ProductListFragment.companyId, String.valueOf(ProductList.getProductCategory().get(position).getCompanyID()));
                                            bundle.putString(ProductListFragment.productCategoryId, String.valueOf(ProductList.getProductCategory().get(position).getProductCategoryID()));
                                            ProductListFragment ProductListFragment = new ProductListFragment();
                                            ProductListFragment.setArguments(bundle);
                                            activityReference.addSupportFragment(ProductListFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);


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


                productItemAdapter = new ProductAdapter(activityReference, ProductList.getProduct());
                StaggeredGridLayoutManager gridLayoutManager =
                        new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                rvProduct.setLayoutManager(gridLayoutManager);
                // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
                rvProduct.setAdapter(productItemAdapter);
                rvProduct.setNestedScrollingEnabled(false);


                rvProduct.addOnItemTouchListener(new RecyclerTouchListener(activityReference, rvProduct,
                        new ClickListenerRecycler() {
                            @Override
                            public void onClick(View view, int position) {

                                if (NetworkUtils.isNetworkAvailable(activityReference)) {
                                    try {


                                        // CompanyHelper companyHelper = new CompanyHelper(activityReference, preferenceHelper);
                                        //companyHelper.cacheCat(String.valueOf(ProductList.getProduct().get(position).getCompanyID()));

                                        Bundle bundle = new Bundle();
                                        bundle.putString(ProductFragment.companyId, String.valueOf(ProductList.getProduct().get(position).getCompanyID()));
                                        bundle.putString(ProductFragment.productId, String.valueOf(ProductList.getProduct().get(position).getProductID()));
                                        ProductFragment ProductFragment = new ProductFragment();
                                        ProductFragment.setArguments(bundle);
                                        activityReference.addSupportFragment(ProductFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);


                                    } catch (Exception e) {
                                        Utils.showToast(activityReference, activityReference.getString(R.string.product_detail_not_found), AppConstant.TOAST_TYPES.INFO);
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

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });


    }


}
