package com.application.lumaque.bizlinked.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.HomeActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.data_models.bizlinked.MajorCategoryModel;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.daimajia.androidanimations.library.Techniques;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


public class SignupFragment extends BaseFragment {

    @Order(1)
    @Length(min = AppConstant.VALIDATION_RULES.NAME_MIN_LENGTH, messageResId = R.string.error_fname)
    @BindView(R.id.et_user_cname)
    EditText etCompanyName;

    @Order(2)
    @Email( messageResId = R.string.error_email)
    @Length(min = AppConstant.VALIDATION_RULES.NAME_MIN_LENGTH, messageResId = R.string.error_lname)
    @BindView(R.id.et_user_lname)
    EditText etUserName;

/*

    @BindView(R.id.et_user_category)
    Spinner etProductCategory;
*/

    @Order(3)
    @Password
    @Length(min = AppConstant.VALIDATION_RULES.PASSWORD_MIN_LENGTH, messageResId = R.string.error_password)
    @BindView(R.id.et_user_password)
    EditText etUserPassword;


   @Order(4)
    @ConfirmPassword
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;

    @BindView(R.id.btn_create_account)
    Button btnCreateAccount;

    ArrayList<MajorCategoryModel>   majorCategories;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_signup;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        //initializeViews();

        setupAnimation(rootView);
    }


    private void setupAnimation(final View rootView) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int duration = 300;
                for (int index = 0; index < ((ViewGroup)rootView).getChildCount(); index++) {
                    AnimationHelpers.animation(Techniques.SlideInDown, duration, ((ViewGroup) rootView).getChildAt(index));
                    duration+=50;
                }
            }
        },500);
    }
  /*  private void initializeViews() {

        WebAppManager.getInstance(activityReference,preferenceHelper).getAllGridDetails(null, AppConstant.ServerAPICalls.PRODUCT_CATEGORIES_URL,true, new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {


                majorCategories = GsonHelper.GsonToMajorCategory(activityReference, response);



                String[] majorCat = new String[majorCategories.size() <= 0 ? 0 : majorCategories.size()];

                    for (int i = 0; i < majorCat.length; i++) {
                        majorCat[i] = majorCategories.get(i).getMajorCategoryName();

                    }



                ArrayAdapter arrayAdapter = new ArrayAdapter(activityReference, android.R.layout.simple_spinner_dropdown_item, majorCat);
                arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                etProductCategory.setAdapter(arrayAdapter);


            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });



    }
*/


    @Override
    public void onCustomBackPressed() {
        activityReference.addSupportFragment(new SelectSigningFragment(), AppConstant.TRANSITION_TYPES.FADE,false);
       // activityReference.onPageBack();
    }



    @OnClick(R.id.btn_create_account)
    public void onViewClicked() {
        //activityReference.changeActivity(HomeActivity.class,true);
        validateFields();
    }



    @Override
    public void onValidationSuccess() {

        SignUp();

    }

    private void SignUp() {



        final HashMap<String, String> params = new HashMap<>();
        params.put("CompanyName", etCompanyName.getText().toString());
        params.put("Username",  etUserName.getText().toString());
        params.put("Password",  etUserPassword.getText().toString());
     //   params.put("ProductMajorCategoryID", majorCategories.get(etProductCategory.getSelectedItemPosition()).getMajorCategoryID());

        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetails(
                Request.Method.POST,
                params, AppConstant.ServerAPICalls.REGISTER_URL, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {

                        CompanyProfileModel companyprofile = GsonHelper.GsonToCompanyProfile( response);

                        preferenceHelper.putCompany(companyprofile);

                        Utils.showToast(activityReference, "Account Created...", AppConstant.TOAST_TYPES.SUCCESS);
                        activityReference.changeActivity(HomeActivity.class, true);

                    }

                    @Override
                    public void onError(String response) {
                          //  Utils.showToast(activityReference, "error", AppConstant.TOAST_TYPES.SUCCESS);

                    }

                    @Override
                    public void onNoNetwork() {

                    }
                });

        /*
        WebAppManager.getInstance(activityReference, preferenceHelper).userLogin(etUserName.getText().toString(), etPassword.getText().toString(), new WebAppManager.APIStringRequestDataCallBack() {
            @Override
            public void onSuccess(String response) {
                Utils.showToast(activityReference, "Logged in successfully...", AppConstant.TOAST_TYPES.SUCCESS);
                activityReference.changeActivity(HomeActivity.class, true);

            }

            @Override
            public void onError(String response) {

            }

            @Override
            public void onNoNetwork() {

            }
        });
*/
    }

}


