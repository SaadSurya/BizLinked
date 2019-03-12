package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.Product;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.CategoryListAdapter;
import com.application.lumaque.bizlinked.helpers.common.KeyboardHelper;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.network.NetworkUtils;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class CategoryListFragment extends BaseFragment implements SearchView.OnQueryTextListener {

    @BindView(R.id.rv_category_list)
    RecyclerView categoryListRV;
    private String strQuery = "";
    SearchView searchView;

    MenuItem searchItem;
    ImageView closeButton;

    @BindView(R.id.fab_cat_list)
    FloatingActionButton fabCatList;
    private int companyId;
    private ArrayList<ProductCategory> productCategoriesList;
    private ArrayList<ProductCategory> productCategoriesListSearched;
    private CategoryListAdapter categoryItemAdapter;
    private boolean isFromCatList = false;
    private ProductCategory productSubCategory;

    @Override
    public void onCustomBackPressed() {
        searchView.setQuery("", false);
        searchView.onActionViewCollapsed();
        strQuery = "";
        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_category_list;

    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        setHasOptionsMenu(true);
        getBaseActivity().toolbar.setTitle("Categories");


    }
    @Override
    public void onResume() {
        super.onResume();
        if (strQuery.length() > 0) {
            searchView.setQuery(strQuery, false);
            searchFromServer(strQuery);
        } else
            initializeViews();

    }
    private void initializeViews() {
        ProductCategory productCategory = new ProductCategory();

        Bundle bundle = getArguments();
        if (bundle != null) {
            isFromCatList = bundle.getBoolean("FROMCATLIST");
            productCategory = (ProductCategory) bundle.getSerializable("CAT_OBJ");
            productSubCategory = productCategory;
            getBaseActivity().toolbar.setTitle(productCategory.getProductCategoryName());

        }
        companyId = preferenceHelper.getCompanyProfile().getCompanyID();
//        CompanyHelper companyHelper = new CompanyHelper(activityReference, preferenceHelper, this);
//        companyHelper.getCompanyCategoty(companyId);
        if (isFromCatList) {
            assert productCategory != null;
            productCategoriesList = productCategory.getSubProductCategories();
            setAdapter(productCategoriesList);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("companyId", String.valueOf(companyId));
            WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_HEIRARCHY_CATEGORY, false, new WebAppManager.APIStringRequestDataCallBack() {
                @Override
                public void onSuccess(String response) {
                    ArrayList<ProductCategory> categoryList = new ArrayList<>();
                /*ArrayList<ProductCategory> categoryList2 = new ArrayList<>();
                Gson gson = new Gson();
                Type typeOfT = new TypeToken<List<SubProductCategory>>() {
                }.getType();
                JsonParser parser = new JsonParser();
                JsonArray jo = (JsonArray) parser.parse(response);
                categoryList2 = gson.fromJson(jo, typeOfT);*/
                    GsonHelper gsonHelper = new GsonHelper();
                    categoryList = gsonHelper.GsonToCategoryList(response);
                    productCategoriesList = categoryList;
                    setAdapter(productCategoriesList);
                }

                @Override
                public void onError(String response) {
//                mShimmerViewContainer.stopShimmerAnimation();
                    Log.d("CAT_SUB_LIST", response);
                    onCustomBackPressed();

                }

                @Override
                public void onNoNetwork() {

                }
            });

        }
    }

    private void setAdapter(final ArrayList<ProductCategory> productCategoriesList) {
        categoryItemAdapter = new CategoryListAdapter(activityReference, activityReference, productCategoriesList);
        categoryListRV.setHasFixedSize(true);
        categoryListRV.setLayoutManager(new GridLayoutManager(activityReference, 2, GridLayoutManager.VERTICAL, false));
        // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
        categoryListRV.setAdapter(categoryItemAdapter);
      /*  categoryListRV.addOnItemTouchListener(new RecyclerTouchListener(activityReference, categoryListRV,
                new ClickListenerRecycler() {
                    @Override
                    public void onClick(View view, int position) {
                        ProductCategory currentObject = productCategoriesList.get(position);
                        if (NetworkUtils.isNetworkAvailable(activityReference)) {

                            try {
//                                    NewCategoryFragment newCategoryFragment = new NewCategoryFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("CAT_OBJ", currentObject);
                                bundle.putBoolean("FROMCATLIST", true);
                                CategoryListFragment categoryListFragment = new CategoryListFragment();
                                categoryListFragment.setArguments(bundle);
                                activityReference.addSupportFragment(categoryListFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);


//                                    activityReference.addSupportFragment(newCategoryFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);
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
        ));*/

    }

    //    public void setArguments(Bundle bundle) {
////        bundle = getArguments();
//        if (bundle != null) {
//            productCategory = (ProductCategory) bundle.getSerializable("CAT_OBJ");
//        }
//    }
    @OnClick({R.id.fab_cat_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_cat_list:
                NewCategoryFragment newCategoryFragment = new NewCategoryFragment();

//                    ProductCategory category = new ProductCategory();
                    Bundle bundle = new Bundle();
//                        bundle.putSerializable("CAT_OBJ", currentObject.getSubProductCategories().get(position));
                    if (productSubCategory != null){
                        bundle.putSerializable("CAT_OBJ", productSubCategory);

                    }
                    bundle.putBoolean("isFromNestedCAT", true);
                    newCategoryFragment.setArguments(bundle);

                activityReference.addSupportFragment(newCategoryFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ex7, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);


        if (strQuery.length() > 0) {
            searchView.onActionViewExpanded();
            searchItem.expandActionView();
            searchView.setQuery(strQuery, false);
        }
        closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!strQuery.equalsIgnoreCase("")) {
                    searchView.setQuery("", false);
                    searchView.onActionViewCollapsed();
                    searchItem.collapseActionView();
                    initializeViews();
                    strQuery = "";
                } else {
                    strQuery = "";
                    searchView.setQuery("", false);
                    searchView.onActionViewCollapsed();
                    searchItem.collapseActionView();
                }

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        KeyboardHelper.hideSoftKeyboard(activityReference);
        searchFromServer(query);

        strQuery = query;
        return false;
    }

    private void searchFromServer(String query) {
        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(companyId));
        params.put("productcategoryname", query);
        WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_HEIRARCHY_CATEGORY, false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                ArrayList<ProductCategory> categoryList = new ArrayList<>();
                GsonHelper gsonHelper = new GsonHelper();
                categoryList = gsonHelper.GsonToCategoryList(response);
                productCategoriesListSearched = categoryList;
                categoryItemAdapter.clearAllList();
                categoryItemAdapter.addAllList(productCategoriesListSearched);
            }

            @Override
            public void onError(String response) {
//                mShimmerViewContainer.stopShimmerAnimation();
                Log.d("CAT_SUB_LIST", response);
                onCustomBackPressed();

            }

            @Override
            public void onNoNetwork() {

            }
        });
    }

    @Override
    public boolean onQueryTextChange(String query) {
        return false;
    }
}
