package com.application.lumaque.bizlinked.fragments.bizlinked;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.application.lumaque.bizlinked.webhelpers.CompanyHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class NewCategoryFragment extends BaseFragment implements ResponceCallBack {

    @Order(1)
    @Length(min = AppConstant.VALIDATION_RULES.USER_NAME_MIN_LENGTH, messageResId = R.string.error_category_name)
    @BindView(R.id.et_category_name)
    EditText catNameEditText;


    @BindView(R.id.att_type_cat)
    AutoCompleteTextView parentProductEditText;

    @BindView(R.id.iv_category)
    ImageView categoryImageView;

    private GsonHelper gsonHelper;
    private int companyId;
    private int postionAdapter;

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
                    }

                    @Override
                    public void onError(String response) {
                    }

                    @Override
                    public void onNoNetwork() {
                    }
                });
    }

    @OnClick({R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                validateFields();
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
}
