package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.SaveCategoryAdapter;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.ui.dialogs.DialogFactory;
import com.application.lumaque.bizlinked.listener.MediaTypePicker;
import com.application.lumaque.bizlinked.webhelpers.CompanyHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAPIRequestHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.application.lumaque.bizlinked.helpers.common.Utils.getBytes;

public class NewCategoryFragment extends BaseFragment implements ResponceCallBack, MediaTypePicker {

    @Order(1)
    @Length(min = AppConstant.VALIDATION_RULES.USER_NAME_MIN_LENGTH, messageResId = R.string.error_category_name)
    @BindView(R.id.et_category_name)
    EditText catNameEditText;

    boolean isImageSet = false;

    @BindView(R.id.att_type_cat)
    AutoCompleteTextView parentProductEditText;

    @BindView(R.id.iv_category)
    ImageView categoryImageView;
    File imageFile;
    private GsonHelper gsonHelper;
    private int companyId;
    private int postionAdapter;
    private ProductCategory productCategory;
    @Override
    public void onCustomBackPressed() {
        activityReference.onPageBack();
    }


    int selectedParentCagetory;

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_new_category;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {
        getBaseActivity().toolbar.setTitle("New Category");
//        setArguments();
//        cacheCat();
        initializeViews();
//        if()


    }

    private void initializeViews() {
        companyId = preferenceHelper.getCompanyProfile().getCompanyID();
        CompanyHelper companyHelper = new CompanyHelper(activityReference, preferenceHelper, this);
        companyHelper.getCompanyCategoty(companyId);

    }

    @Override
    public void onValidationSuccess() {
        saveCategory();
    }


    @Override
    public void onValidationFail() {
    }

    private void saveCategory() {
        gsonHelper = new GsonHelper();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCompanyID(preferenceHelper.getCompanyProfile().getCompanyID());
        productCategory.setProductCategoryName(catNameEditText.getText().toString());
        productCategory.setParentProductCategoryID(selectedParentCagetory);
        saveNewCategory(gsonHelper.getJsonString(productCategory));
    }

    private void saveNewCategory(String jsonString) {
        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetailsJson(
                Request.Method.POST,
                jsonString, AppConstant.ServerAPICalls.CATEGORY_SAVE, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        String anc = response;
                        Utils.showToast(activityReference, "save Successfully", AppConstant.TOAST_TYPES.SUCCESS);
                        onCustomBackPressed();
                        GsonHelper gsonHelper = new GsonHelper();
                        productCategory = gsonHelper.GsonToProductCategory(activityReference, response);
                        uploadMedia(imageFile,"1");
                    }

                    @Override
                    public void onError(String response) {
                    }

                    @Override
                    public void onNoNetwork() {
                    }
                });
    }

    @OnClick({R.id.btn_save, R.id.iv_category})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                validateFields();
                break;

            case R.id.iv_category:
                openImagePicker(view);
                break;
        }
    }

    @Override
    public void onCategoryResponce(ArrayList<ProductCategory> categoryList) {

        SaveCategoryAdapter saveCategoryAdapter = new SaveCategoryAdapter(activityReference, categoryList);
        parentProductEditText.setAdapter(saveCategoryAdapter);
        parentProductEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String abc = "";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(s.subSequence(s.length() - count, s.length())).equals("")) {
                    selectedParentCagetory = 0;

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        parentProductEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postionAdapter = position;

                selectedParentCagetory = ((ProductCategory) parent.getItemAtPosition(position)).ProductCategoryID;

            }
        });
    }

    private void openImagePicker(View view) {
        if (isImageSet)
            openOptionsList(view);
        else
            takePicture(view);
    }


    private void getImages() {

        String URL = AppConstant.ServerAPICalls.GET_MEDIA_FILE + preferenceHelper.getCompanyProfile().getCompanyID();
        ///  ImageView ivDocumentImage = flCaptureImage1.findViewById(R.id.ivDocumentImage);
        //  setVisibilityOfImageView(true,ivDocumentImage);
        Glide.with(activityReference).load(URL)
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())).placeholder(R.drawable.profile))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        isImageSet = false;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        isImageSet = true;
                        return false;
                    }
                })
                .into(categoryImageView);
    }

    private void openOptionsList(final View view) {
        final ArrayList<String> optionsList = new ArrayList<>();
        optionsList.add(activityReference.getString(R.string.preview));
        optionsList.add(activityReference.getString(R.string.update));
        optionsList.add(activityReference.getString(R.string.delete));
        DialogFactory.listDialog(activityReference, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichOptionPosition) {
                dialog.dismiss();

                if (optionsList.get(whichOptionPosition).equalsIgnoreCase(activityReference.getString(R.string.preview))) {
                    openImagePreview((ImageView) view);
                } else if (optionsList.get(whichOptionPosition).equalsIgnoreCase(activityReference.getString(R.string.delete))) {
                    deleteCurrentProfileImage();

                } else if (optionsList.get(whichOptionPosition).equalsIgnoreCase(activityReference.getString(R.string.update))) {
                    takePicture(view);
                }

            }
        }, activityReference.getString(R.string.select_options), optionsList);
    }

    private void takePicture(View view) {
        activityReference.openMediaPicker(NewCategoryFragment.this);

        //  this.currentImageContainerView = view.findViewById(R.id.flImageDocumnetContainer);
    }

    private void openImagePreview(ImageView imageView) {
        final ImagePopup imagePopup = new ImagePopup(activityReference);
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional
        imagePopup.initiatePopup(imageView.getDrawable());
        imagePopup.viewPopup();
    }


    private void deleteCurrentProfileImage() {


        final HashMap<String, String> params = new HashMap<>();

        params.put("id", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));


        WebAppManager.getInstance(activityReference, preferenceHelper).deleteDetails(params, AppConstant.ServerAPICalls.DELETE_COMPANY_PROFILE_PIC, true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                // Utils.showToast(activityReference, response, AppConstant.TOAST_TYPES.SUCCESS);
                getImages();
//                activityReference.updateDrawer();
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
    public void onPhotoClicked(ArrayList<File> file) {
        if (file.get(0) != null) {
            categoryImageView.setAdjustViewBounds(true);
            categoryImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageFile = file.get(0);
            Bitmap bitmap = BitmapFactory.decodeFile(file.get(0).getPath());
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(this)
                    .load(stream.toByteArray())
                    .into(categoryImageView);
            Log.d("FileTag", "File is not null");
        }
    }

    @Override
    public void onDocClicked(ArrayList<File> files) {

    }







    private void uploadMedia(final File file, final String fileName) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("id", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));

        String catImageURL = AppConstant.ServerAPICalls.UPLOAD_CATEGORY_IMAGE+"/"+preferenceHelper.getCompanyProfile().getCompanyID()+ "/"+productCategory.getProductCategoryID();

        //upload image to server
        WebAppManager.getInstance(activityReference, preferenceHelper).uploadImage(fileName, parameters, catImageURL,file
                , new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        //setImageFromPath(true, currentImageContainerView,file.getAbsolutePath());
                    //    getImages();
//                        activityReference.updateDrawer();
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
