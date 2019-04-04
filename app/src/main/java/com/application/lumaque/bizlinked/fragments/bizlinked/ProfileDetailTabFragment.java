package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.BusinessNatureModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.ui.dialogs.DialogFactory;
import com.application.lumaque.bizlinked.listener.MediaTypePicker;
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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileDetailTabFragment extends Fragment implements MediaTypePicker {
    ArrayList<BusinessNatureModel> BusinessNatureList;


    boolean isImageSet = false;
    @BindView(R.id.profile_picture)
    ImageView profilePic;

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.sp_busines_type)
    Spinner spBusinesType;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_phone2)
    EditText etPhone2;
    @BindView(R.id.et_email)
    EditText etEMail;
    @BindView(R.id.et_website)
    EditText etWebsite;
    @BindView(R.id.btn_save)
    Button btSave;


    ProfileTabsFragment parentFragment;

    private String selectedViewName;
    Unbinder unbinder;
    BaseActivity activityReference;
    BasePreferenceHelper preferenceHelper;
    private static ProfileDetailTabFragment profileDetailTabFragment;

    // private int position;
    // private View currentImageContainerView;
    @SuppressLint("ValidFragment")
    private ProfileDetailTabFragment() {
    }

    public static ProfileDetailTabFragment getInstance(BaseActivity activityReference, BasePreferenceHelper preferenceHelper) {
        if (profileDetailTabFragment == null) {
            profileDetailTabFragment = new ProfileDetailTabFragment();
            profileDetailTabFragment.activityReference = activityReference;
            profileDetailTabFragment.preferenceHelper = preferenceHelper;
            return profileDetailTabFragment;

        }
        return profileDetailTabFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_profile_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        getStoragePermissions();
        initializeViews();


        return rootView;

    }

    private void initializeViews() {

        WebAppManager.getInstance(activityReference, preferenceHelper).getAllGridDetails(null, AppConstant.ServerAPICalls.BUSINESS_NATURE_URL, true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {


                BusinessNatureList = GsonHelper.GsonToBusinessNature(response);


                String[] majorCat = new String[BusinessNatureList.size() <= 0 ? 0 : BusinessNatureList.size()];

                for (int i = 0; i < majorCat.length; i++) {
                    majorCat[i] = BusinessNatureList.get(i).getBusinessNatureName();

                }


                ArrayAdapter arrayAdapter = new ArrayAdapter(activityReference, android.R.layout.simple_spinner_dropdown_item, majorCat);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spBusinesType.setAdapter(arrayAdapter);
                getImages();

                etUserName.setText(preferenceHelper.getCompanyProfile().getCompanyName());
                etPhone.setText(preferenceHelper.getCompanyProfile().getContactNo());
                etPhone2.setText(preferenceHelper.getCompanyProfile().getPhoneNo());
                etEMail.setText(preferenceHelper.getCompanyProfile().getEmailAddress());
                etWebsite.setText(preferenceHelper.getCompanyProfile().getWebsite());
                //  preferenceHelper.getCompanyProfile();


                try {


                    int defaultPos = 0;
                    for (int i = 0; i < BusinessNatureList.size(); i++) {
                        if (BusinessNatureList.get(i).getBusinessNatureID().equalsIgnoreCase(String.valueOf(preferenceHelper.getCompanyProfile().getBusinessNature()[0])))
                            defaultPos = i;
                        spBusinesType.setSelection(defaultPos);
                    }

                } catch (Exception e) {


                }


                CompanyProfileModel companyProfileModel = new CompanyProfileModel();

                companyProfileModel.setBusinessNature(preferenceHelper.getCompanyProfile().getBusinessNature());
                companyProfileModel.setCompanyID(preferenceHelper.getCompanyProfile().getCompanyID());
            //    companyProfileModel.setProductMajorCategoryID(preferenceHelper.getCompanyProfile().getProductMajorCategoryID());
                companyProfileModel.setCompanyName(etUserName.getText().toString());
                companyProfileModel.setContactNo(etPhone.getText().toString());
                companyProfileModel.setPhoneNo(etPhone2.getText().toString());
                companyProfileModel.setEmailAddress(etEMail.getText().toString());
                companyProfileModel.setWebsite(etWebsite.getText().toString());

                //   parentFragment = ((ProfileTabsFragment)this.getParentFragment());
                parentFragment = ((ProfileTabsFragment) (ProfileDetailTabFragment.this.getParentFragment()));
                parentFragment.updatedProfile = companyProfileModel;


            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });


    }

    public void doBack() {


        activityReference.onPageBack();


    }


    @OnClick({R.id.profile_picture, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_picture:
                selectedViewName = AppConstant.SCAN_DOCUMENT_TYPES.IMAGE_0;
                openImagePicker(view);
                break;
            case R.id.btn_save:
                onSave();

                break;

        }
    }


    private void onSave() {

        CompanyProfileModel companyProfileModel = new CompanyProfileModel();


        int BN[] = {Integer.parseInt(BusinessNatureList.get(spBusinesType.getSelectedItemPosition()).getBusinessNatureID())};

        companyProfileModel.setCompanyID(preferenceHelper.getCompanyProfile().getCompanyID());
       // companyProfileModel.setProductMajorCategoryID(preferenceHelper.getCompanyProfile().getProductMajorCategoryID());
        companyProfileModel.setCompanyName(etUserName.getText().toString());
        companyProfileModel.setContactNo(etPhone.getText().toString());
        companyProfileModel.setPhoneNo(etPhone2.getText().toString());
        companyProfileModel.setEmailAddress(etEMail.getText().toString());
        companyProfileModel.setWebsite(etWebsite.getText().toString());


        companyProfileModel.setBusinessNature(BN);


        parentFragment.updatedProfile = companyProfileModel;
        parentFragment.saveDetailAndNext();


    }

    private void openImagePicker(View view) {
        if (isImageSet)
            openOptionsList(view);
        else
            takePicture(view);
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

   /* private boolean isImageSet(View imageContainerView) {
        return ((ImageView) imageContainerView.findViewById(R.id.ivDocumentImage)).getVisibility() == View.VISIBLE;
    }*/

    private void takePicture(View view) {
        activityReference.openMediaPicker(ProfileDetailTabFragment.this, 1);

        //  this.currentImageContainerView = view.findViewById(R.id.flImageDocumnetContainer);
    }

    private void getStoragePermissions() {
        if (TedPermission.isGranted(activityReference, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            getImages();
        } else {
            TedPermission.with(activityReference)
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted() {
                            getImages();
                        }

                        @Override
                        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                            //  setupAnimation(glImageContainer);
                        }
                    }).check();

        }
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
                .into(profilePic);


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


    @Override
    public void onPhotoClicked(ArrayList<File> file) {

        if (file.size() > 0) {
            CropImage.activity(Uri.fromFile(file.get(0)))
                    .start(activityReference);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            File file = new File(CropImage.getActivityResult(data).getUri().getPath());
            uploadMedia(file, String.format("%s%s", selectedViewName, ".jpg"));

        }


    }

    private void uploadMedia(final File file, final String fileName) {
        HashMap<String, String> parameters = new HashMap<>();

        parameters.put("id", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));

        String logoURL = AppConstant.ServerAPICalls.UPLOAD_FILE_IMAGE + "/" + preferenceHelper.getCompanyProfile().getCompanyID();

        //upload image to server
        WebAppManager.getInstance(activityReference, preferenceHelper).uploadImage(fileName, parameters, logoURL, true, file
                , new WebAPIRequestHelper.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        //setImageFromPath(true, currentImageContainerView,file.getAbsolutePath());
                        getImages();
                        activityReference.updateDrawer();
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

    public void setImageFromPath(final boolean uploadFromGallery, final View currentImageContainerView, final String filePath) {

        ImageView ivDocumentImage = currentImageContainerView.findViewById(R.id.ivDocumentImage);

        setVisibilityOfImageView(true, ivDocumentImage);

        ivDocumentImage.setTag(uploadFromGallery ? AppConstant.ServerAPICalls.IMAGE_UPLOAD_STATUS.FROM_GALLERY : AppConstant.ServerAPICalls.IMAGE_UPLOAD_STATUS.FROM_SERVER);
        ivDocumentImage.setImageBitmap(BitmapFactory.decodeFile(filePath));

        //    ivDocumentImage.getLayoutParams().width = currentImageContainerView.getWidth() - (currentImageContainerView.getPaddingBottom() * 2);
        //      ivDocumentImage.getLayoutParams().height = currentImageContainerView.getHeight() - (currentImageContainerView.getPaddingBottom() * 2);
        //        ivDocumentImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //  ivDocumentImage.requestLayout();

    }

    private void setVisibilityOfImageView(boolean isVisible, View view) {
        //ImageView imageView = view.findViewById(R.id.ivDocumentImage);
        if (isVisible) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void deleteCurrentProfileImage() {


        final HashMap<String, String> params = new HashMap<>();

        params.put("id", String.valueOf(preferenceHelper.getCompanyProfile().getCompanyID()));


        WebAppManager.getInstance(activityReference, preferenceHelper).deleteDetails(params, AppConstant.ServerAPICalls.DELETE_COMPANY_PROFILE_PIC, true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                // Utils.showToast(activityReference, response, AppConstant.TOAST_TYPES.SUCCESS);
                getImages();
                activityReference.updateDrawer();


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
