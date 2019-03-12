package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.customViews.AttributesDialog;
import com.application.lumaque.bizlinked.data_models.bizlinked.Product;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductList;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.TagViewAdapter;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.listener.MediaTypePicker;
import com.application.lumaque.bizlinked.webhelpers.CompanyHelper;
import com.application.lumaque.bizlinked.webhelpers.ProductHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAPIRequestHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class ProductFragment extends BaseFragment implements TagCloseCallBack, ResponceCallBack, MediaTypePicker {

    private Bundle bundle;
    private ArrayList<ProductCategory> companyCategoryList;
    private ArrayList<ProductAttribute> productAttributes;
    public static final int DATEPICKER_FRAGMENT = 1;
    private Product product;
    public static final String companyId = "companyId";
    public static final String productId = "productId";
    public static final String categoryId = "categoryId";
    Gson g = new Gson();
    int paramCompanyId;
    String paramProductId = "";
    String paramCategoryId = "";
    //    private String newItemFilePath;
    ArrayList<File> imageFile;
    private CustomPagerAdapter viewpagerAdapter;
    List<String> ImageList = new ArrayList<>();
    TagViewAdapter tagItemAdapter;
    private boolean isInEditMode = false;
    //    HashMap<String, Integer> hMap = new HashMap<>();
//    private CatArrayAdapter catArrayAdapter;
    //  int ImageObjectSize;
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

    @BindView(R.id.add_att)
    ImageButton addAtt;

    @BindView(R.id.fab_add_image)
    ImageButton fabAddImage;

    @BindView(R.id.btn_save)
    Button btnSave;

 @BindView(R.id.btn_publish)
    Button btnPublish;

    @BindView(R.id.product_desc_view)
    ScrollView productDescView;

    @BindView(R.id.attribute_layout)
    RecyclerView attributeLayout;


    @Override
    public void onCustomBackPressed() {
        activityReference.onPageBack();
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_product;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        startShimerAnimation();
        getBaseActivity().toolbar.setTitle("Product");
        setArguments();
        cacheCat();
        viewpagerAdapter = new CustomPagerAdapter(activityReference);
        viewpager.setAdapter(viewpagerAdapter);
        indicator.setViewPager(viewpager);
        imageFile = new ArrayList<>();
       /* ArrayList<String> newList = new ArrayList<>();
        catArrayAdapter = new CatArrayAdapter(activityReference, newList);
        catArrayAdapter.setNotifyOnChange(true);
        proCate.setAdapter(catArrayAdapter);*/

        /**
         * update product
         */
        if (paramProductId != null && paramProductId.length() > 0) {
            startShimerAnimation();
            initializeViews();
            isInEditMode = true;
        }
        /**
         * new product
         */
        else {
            stopShimerAnimation();
            product = new Product();
            isInEditMode = false;
            product.setProductAttributes(new ArrayList<ProductAttribute>());
            product.setCompanyID(preferenceHelper.getCompanyProfile().getCompanyID());
            if (!paramCategoryId.equals(""))
                product.setProductCategoryID(Long.parseLong(paramCategoryId));

            setTagAdapter();


        }


    }

    private void setArguments() {
        bundle = getArguments();
        if (bundle != null) {
            paramCompanyId = bundle.getInt(companyId);
            paramProductId = bundle.getString(productId);
            paramCategoryId = bundle.getString(categoryId);
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

                if(product.IsPublished){
                    btnPublish.setVisibility(View.GONE);
                   // btnPublish.setText("UNPUBLISH");
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

    @Override
    public void onImageClick(ProductAttribute productAttribute) {
        Utils.showToast(activityReference, "doesnot delete", AppConstant.TOAST_TYPES.ERROR);
        // TODO: 12/14/2018 perfome cancel button
    }

    @Override
    public void onRowClick(ProductAttribute productAttribute) {
        showAttributeDialog(productAttribute, false,tagItemAdapter.getAttributeLIst());


    }

    @Override
    public void onCategoryResponce(ArrayList<ProductCategory> categoryList) {
        if (categoryList != null) {
            setCatAdapter(categoryList);
        }

    }

    private void takePicture(View view) {
        activityReference.openMediaPicker(ProductFragment.this, 10);

        //  this.currentImageContainerView = view.findViewById(R.id.flImageDocumnetContainer);
    }

    @Override
    public void onPhotoClicked(ArrayList<File> file) {
        if (file.get(0) != null) {
//            categoryImageView.setAdjustViewBounds(true);
//            categoryImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageFile.addAll(file);
//            newItemFilePath = file.getPath();
            for (int i = 0; i < file.size(); i++) {
                ImageList.add(file.get(i).getPath());
            }
            viewpagerAdapter.notifyDataSetChanged();
            Log.d("FileTag", "File is not null");
            if (isInEditMode) {
                uploadImages(file);
            }
        }

//        if(productCategory.getProductCategoryID() != 0)

    }

    private void uploadImages(ArrayList<File> files) {
        for (int i = 0; i < files.size(); i++) {
            uploadMedia(files.get(i), "1.jpg");
        }
        imageFile.clear();


    }

    private void uploadMedia(final File file, final String fileName) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("id", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));

        String catImageURL = AppConstant.ServerAPICalls.UPLOAD_PRODUCT_IMAGE + "?" + "companyId=" + paramCompanyId + "&productId=" + product.getProductID();

        //upload image to server
        WebAppManager.getInstance(activityReference, preferenceHelper).uploadImage(fileName, parameters, catImageURL, false, file
                , new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        //setImageFromPath(true, currentImageContainerView,file.getAbsolutePath());
                        //    getImages();
//                        activityReference.updateDrawer();


                        Utils.showToast(activityReference, "Successfull", AppConstant.TOAST_TYPES.SUCCESS);
                    }

                    @Override
                    public void onError(String response) {


                    }

                    @Override
                    public void onNoNetwork() {

                    }
                });
    }

    @Override
    public void onDocClicked(ArrayList<File> files) {

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


    private void cacheCat() {


        CompanyHelper companyHelper = new CompanyHelper(activityReference, preferenceHelper, this);
        companyHelper.getCompanyCategoty(paramCompanyId);
        companyHelper.getCompanyAttributes(paramCompanyId);


    }


    private void setTagAdapter() {


        tagItemAdapter = new TagViewAdapter(activityReference, product.getProductAttributes(), this);
        attributeLayout.setLayoutManager(new LinearLayoutManager(activityReference));
        attributeLayout.setAdapter(tagItemAdapter);
        attributeLayout.setNestedScrollingEnabled(false);


    }


    private void showAttributeDialog(ProductAttribute attribute, boolean isNew,ArrayList<ProductAttribute> productAttributeArrayList) {
        FragmentManager fm = activityReference.getSupportFragmentManager();
        AttributesDialog editNameDialogFragment = AttributesDialog.newInstance(attribute, isNew,productAttributeArrayList);
        if (editNameDialogFragment.getDialog() != null)
            editNameDialogFragment.getDialog().setCanceledOnTouchOutside(false);
        //  editNameDialogFragment.show(fm, "fragment_attribute_dialog");


        editNameDialogFragment.setTargetFragment(this, DATEPICKER_FRAGMENT);
        editNameDialogFragment.show(fm, "fragment_attribute_dialog");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DATEPICKER_FRAGMENT:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    ProductAttribute returnedAtt = (ProductAttribute) data.getExtras().getSerializable("attr");

                    if (data.getBooleanExtra("isNew", false))
                        tagItemAdapter.addItem(returnedAtt);

                    tagItemAdapter.notifyChangeData();
                 //   Toast.makeText(activityReference, "show", Toast.LENGTH_SHORT).show();
                } else if (resultCode == Activity.RESULT_CANCELED) {

                }
                break;
        }
    }

    private void setCatAdapter(ArrayList<ProductCategory> categoryList) {

        companyCategoryList = categoryList;

        List categoryName = new ArrayList();

        for (ProductCategory temp : companyCategoryList) {

            if (paramCategoryId != null && paramCategoryId.equals(String.valueOf(temp.getProductCategoryID())) && proCate != null) {
                proCate.setText(temp.getProductCategoryName());
                product.setProductCategoryName(temp.getProductCategoryName());
            }
            categoryName.add(temp.getProductCategoryName());
//            hMap.put(temp.getProductCategoryName(), temp.getProductCategoryID());
            //System.out.println(temp);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(activityReference, android.R.layout.simple_list_item_1, categoryName);
        proCate.setAdapter(adapter);
        proCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                product.setProductCategoryID(companyCategoryList.get(position).getProductCategoryID());
            }
        });
//      catArrayAdapter.updateAdapter(new ArrayList<String>(categoryName));
//        proCate.setAdapter(catArrayAdapter);

    }


    @OnClick({R.id.add_att, R.id.btn_save, R.id.fab_add_image,R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_publish:
               if(isInEditMode) {
                   //todo api call of publish

                   if(((Button)view).getText().toString().equalsIgnoreCase("Publish"))
                   publishUnpublishProduct(AppConstant.ServerAPICalls.PRODUCT_PUBLISH);
                   else
                       publishUnpublishProduct(AppConstant.ServerAPICalls.PRODUCT_UNPUBLISH);
               }else
                saveClick(true);

                break;
                case R.id.btn_save:
                saveClick(false);

                break;
            case R.id.add_att:
                //onSave();
                showAttributeDialog(null, true,tagItemAdapter.getAttributeLIst());

                break;

            case R.id.fab_add_image:
                takePicture(view);


        }
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


                        if (activityReference.isFragmentPresent(ProductViewFragment.class.getName())) {
                            activityReference.clearStackTillFragment(
                                    ProductViewFragment.class.getName()
                            );
                        } else {
                            onCustomBackPressed();
                        }




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
 private void saveClick(boolean publish){
     String catName = proCate.getText().toString();
     product.setProductCategoryName(proCate.getText().toString());
     product.setProductName(proName.getText().toString());
     product.setProductDescription(proDesc.getText().toString());
     product.setPrice(Double.parseDouble(proPrice.getText().toString()));
//                if (hMap.get(catName) != null) {
//                    product.setProductCategoryID(hMap.get(catName));
//                }
     product.setProductAttributes(tagItemAdapter.getAttributeLIst());
     product.setPublished(publish);

     String jsonString = g.toJson(product);

     prodSaveReq(jsonString);
 }
    private void prodSaveReq(String jsonString) {

        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetailsJson(
                Request.Method.POST,
                jsonString, AppConstant.ServerAPICalls.PRODUCT_SAVE, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        String anc = response;

                        product = GsonHelper.GsonToProduct(response);

                        Utils.showToast(activityReference, "save Successfully", AppConstant.TOAST_TYPES.SUCCESS);
                        if (!isInEditMode) {
                            uploadImages(imageFile);
                        } else {

                        }
                        onCustomBackPressed();


                    /*    CompanyProfileModel companyprofile = GsonHelper.GsonToCompanyProfile(activityReference, response);

                        preferenceHelper.putCompany(companyprofile);
                        Utils.showToast(activityReference, "Profile Updater", AppConstant.TOAST_TYPES.SUCCESS);
                        activityReference.updateDrawer();*/

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

}
