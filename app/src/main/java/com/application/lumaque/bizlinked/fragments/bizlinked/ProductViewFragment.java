package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.Product;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.TagViewAdapter;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import me.relex.circleindicator.CircleIndicator;

public class ProductViewFragment extends BaseFragment implements TagCloseCallBack {


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout mShimmerViewContainer;

    @BindView(R.id.mainlayout)
    ConstraintLayout mainlayout;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    @BindView(R.id.pro_category)
    AutoCompleteTextView proCate;


    @BindView(R.id.pro_name)
    EditText proName;

    @BindView(R.id.pro_desc)
    EditText proDesc;

    @BindView(R.id.pro_price)
    EditText proPrice;


    @BindView(R.id.fab_add_image)
    ImageButton fabAddImage;



    @BindView(R.id.product_desc_view)
    ScrollView productDescView;

    @BindView(R.id.attribute_layout)
    RecyclerView attributeLayout;
    TextView pubUnPub;

    private Bundle bundle;
    private Product product;
    int paramCompanyId;
    String paramProductId = "";
    String paramCategoryId = "";
    List<String> ImageList = new ArrayList<>();
    private CustomPagerAdapter viewpagerAdapter;
    public static final String companyId = "companyId";
    public static final String productId = "productId";
    public static final String categoryId = "categoryId";
    ArrayList<File> imageFile;
    TagViewAdapter tagItemAdapter;
    @Override
    public void onCustomBackPressed() {
        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_view_product;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {


        startShimerAnimation();
        getBaseActivity().toolbar.setTitle("Product");

        setArguments();


        if(paramCompanyId==((preferenceHelper.getCompanyProfile().getCompanyID())))
            setHasOptionsMenu(true);
        else
            setHasOptionsMenu(false);
        //cacheCat();
        viewpagerAdapter = new CustomPagerAdapter(activityReference);
        viewpager.setAdapter(viewpagerAdapter);
        indicator.setViewPager(viewpager);
        imageFile = new ArrayList<>();

        initializeViews();

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_product_menu, menu);
       // super.onCreateOptionsMenu(menu);
        //getMenuInflater().inflate(R.menu.activity_main, menu);
      //  return true;

        final MenuItem item2 = menu.findItem(R.id.action_view_publish);
        pubUnPub = (TextView) MenuItemCompat.getActionView(item2);

        pubUnPub.setTextSize(22);
        pubUnPub.setTypeface(null, Typeface.BOLD);



        pubUnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.IsPublished)
                    publishUnpublishProduct(AppConstant.ServerAPICalls.PRODUCT_UNPUBLISH);
                else
                    publishUnpublishProduct(AppConstant.ServerAPICalls.PRODUCT_PUBLISH);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_profile:
                Bundle bundle = new Bundle();
                bundle.putInt(companyId, paramCompanyId);
                bundle.putString(productId,paramProductId);

                ProductFragment ProductFragment = new ProductFragment();
                ProductFragment.setArguments(bundle);
                activityReference.addSupportFragment(ProductFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);

                return true;
        case R.id.action_view_publish:
            if(product.IsPublished)
                publishUnpublishProduct(AppConstant.ServerAPICalls.PRODUCT_UNPUBLISH);
            else
                publishUnpublishProduct(AppConstant.ServerAPICalls.PRODUCT_PUBLISH);

            return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initializeViews() {
        startShimerAnimation();
        HashMap<String, String> params = new HashMap<>();
        params.put("companyId", String.valueOf(paramCompanyId));
        params.put("productId", paramProductId);
        ImageList = new ArrayList<>();
        WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(params, AppConstant.ServerAPICalls.PRODUCT_DETAIL, false, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {

                stopShimerAnimation();
                /**
                 * product detail
                 */
                product = GsonHelper.GsonToProduct(response);


                /**
                 * produt images
                 */

                for (int a = 0; a < product.getImages().size(); a++) {
                    ImageList.add("http://api.bizlinked.lumaque.pk/rest/Product/Image?companyId=" + product.CompanyID + "&imageId=" + product.getImages().get(a));
                }
                viewpagerAdapter.notifyDataSetChanged();


                //product.getProductAttributes();

              /*  if (product.getProductCategoryID() > 1)
                    for (ProductCategory temp : companyCategoryList) {
                        if (temp.getProductCategoryID() == product.getProductCategoryID()) {
                            proCate.setText(temp.ProductCategoryName);
                        }
                    }*/
                setTagAdapter();
                proCate.setText(product.getProductCategoryName());
                proName.setText(product.getProductName());
                proDesc.setText(product.getProductDescription());
                proPrice.setText(String.valueOf(product.getPrice()));

                getBaseActivity().toolbar.setTitle(product.getProductName());


                if(paramCompanyId==((preferenceHelper.getCompanyProfile().getCompanyID()))){


                if(product.IsPublished){
                    pubUnPub.setText("unpublish");
                }else {
                    pubUnPub.setText("Publish");
                }

                }

            }

            @Override
            public void onError(String response) {
                stopShimerAnimation();
                onCustomBackPressed();

            }

            @Override
            public void onNoNetwork() {
                stopShimerAnimation();
            }
        });


    }



    private void setArguments() {
        bundle = getArguments();
        if (bundle != null) {
            paramCompanyId = bundle.getInt(companyId);
            paramProductId = bundle.getString(productId);
            paramCategoryId = bundle.getString(categoryId);
        }
    }


    private void startShimerAnimation() {
        isLoading = true;
        mShimmerViewContainer.startShimmerAnimation();
        mainlayout.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);

    }

    private void stopShimerAnimation() {
        isLoading = false;
        mShimmerViewContainer.stopShimmerAnimation();
        mainlayout.setVisibility(View.VISIBLE);
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void onImageClick(ProductAttribute productAttribute) {

    }

    @Override
    public void onRowClick(ProductAttribute productAttribute) {

    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        public CustomPagerAdapter(Context context) {
            mContext = context;
        }

        public void addItem() {

        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            // ModelObject modelObject = ModelObject.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_product_header, collection, false);
            ImageView headerView = layout.findViewById(R.id.imageView);

            Glide.with(activityReference).load(ImageList.get(position))
                    .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())).centerCrop())
                    .into(headerView);
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageSliderFragment imageSliderFragment = new ImageSliderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("IMAGELINKS", new ArrayList<String>(ImageList));
                    imageSliderFragment.setArguments(bundle);
                    activityReference.addSupportFragment(imageSliderFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);

                }
            });

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {

            return ImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // ModelObject customPagerEnum = ModelObject.values()[position];
            return ImageList.get(position);
        }

    }

    private void setTagAdapter() {
        tagItemAdapter = new TagViewAdapter(activityReference, product.getProductAttributes(), this);
        attributeLayout.setLayoutManager(new LinearLayoutManager(activityReference));
        attributeLayout.setAdapter(tagItemAdapter);
        attributeLayout.setNestedScrollingEnabled(false);
        }

    private void publishUnpublishProduct(String URL){
        final HashMap<String, String> params = new HashMap<>();

        //params.put("productId", String.valueOf(product.getProductID()));
        URL= URL + "?productId="+product.getProductID();

        WebAppManager.getInstance(activityReference, preferenceHelper).putDetails(
                Request.Method.PUT,
                params,URL, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {

                        Utils.showToast(activityReference, "Status Updated", AppConstant.TOAST_TYPES.SUCCESS);


                       /* if (activityReference.isFragmentPresent(ProductViewFragment.class.getName())) {
                            activityReference.clearStackTillFragment(
                                    ProductViewFragment.class.getName()
                            );
                        } else {
                            onCustomBackPressed();
                        }
*/



                    }

                    @Override
                    public void onError(String response) {
                        //     Utils.showToast(activityReference, "error", AppConstant.TOAST_TYPES.SUCCESS);

                    }

                    @Override
                    public void onNoNetwork() {

                    }
                });

    }


}
