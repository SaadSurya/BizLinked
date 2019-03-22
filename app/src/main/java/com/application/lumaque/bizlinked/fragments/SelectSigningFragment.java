package com.application.lumaque.bizlinked.fragments;


import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.HomeActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyProfileModel;
import com.application.lumaque.bizlinked.fireBase.BackgroundService;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.application.lumaque.bizlinked.helpers.network.GsonHelper;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;
import com.daimajia.androidanimations.library.Techniques;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;



/**
 * A simple {@link Fragment} subclass.
 */

public class SelectSigningFragment extends BaseFragment {

    @Order(1)
    @Length(min = AppConstant.VALIDATION_RULES.USER_NAME_MIN_LENGTH, messageResId = R.string.error_user_name)
    @BindView(R.id.et_user_name)
    EditText etUserName;

    @Order(2)
    @Length(min = AppConstant.VALIDATION_RULES.PASSWORD_MIN_LENGTH, messageResId = R.string.error_password)
    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_signup)
    Button btnSignup;



    public SelectSigningFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_select_signing;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // AnimationHelpers.animation(Techniques.SlideInDown, 300,tvIconQr);
                AnimationHelpers.animation(Techniques.SlideInDown, 300,etUserName);
                AnimationHelpers.animation(Techniques.SlideInDown, 350,etPassword);
                AnimationHelpers.animation(Techniques.SlideInDown, 400,btnLogin);
                AnimationHelpers.animation(Techniques.SlideInDown, 450,btnSignup);
            }
        },500);

    }


    @Override
    public void onCustomBackPressed() {
        //activityReference.onPageBack();
        System.exit(0);
        activityReference.finish();
    }


    @OnClick({ R.id.btn_login, R.id.btn_signup})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_login:
                validateFields();
                break;
            case R.id.btn_signup:

                activityReference.addSupportFragment(new SignupFragment(), AppConstant.TRANSITION_TYPES.FADE,true);
                break;
        }
    }



    @Override
    public void onValidationSuccess() {
        userLogin();

    }



    private void userLogin() {


        final HashMap<String, String> params = new HashMap<>();
        params.put("Username", etUserName.getText().toString());
        params.put("Password",  etPassword.getText().toString());

        WebAppManager.getInstance(activityReference, preferenceHelper).saveDetails(
                Request.Method.POST,
                params, AppConstant.ServerAPICalls.LOGIN_URL, new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String response) {

                        CompanyProfileModel companyprofile = GsonHelper.GsonToCompanyProfile(response);

                        preferenceHelper.putCompany(companyprofile);

                        preferenceHelper.setLoginStatus(true);

                      /*  BizLinkApplication.setFireBaseReffID( "com_"+ preferenceHelper.getCompanyProfile().getCompanyID());
                        BizLinkApplication.setLoginStatus(true);
*/


                       // Toast.makeText(activityReference,"afterLogin"+preferenceHelper.getCompanyProfile().getCompanyID(),Toast.LENGTH_LONG).show();

                        activityReference.StartBackgroundService(BackgroundService.class);
                       // Utils.showToast(activityReference, "Logged in successfully...", AppConstant.TOAST_TYPES.SUCCESS);
                        activityReference.changeActivity(HomeActivity.class, true);

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
