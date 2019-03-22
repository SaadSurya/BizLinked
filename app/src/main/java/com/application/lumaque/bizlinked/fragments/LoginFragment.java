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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.fragments.baseClass.BaseFragment;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.daimajia.androidanimations.library.Techniques;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Order;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {


    @Order(1)
    @Length(min = AppConstant.VALIDATION_RULES.USER_NAME_MIN_LENGTH, messageResId = R.string.error_user_name)
    @BindView(R.id.et_user_name)
    EditText etUserName;

    @Order(2)
    @Length(min = AppConstant.VALIDATION_RULES.PASSWORD_MIN_LENGTH, messageResId = R.string.error_password)
    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.tvCaptchaText)
    TextView tvCaptchaText;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.ivbRefreshIcon)
    ImageView ivbRefreshIcon;

    @BindView(R.id.rgCaptcha)
    RadioGroup rgCaptcha;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getMainLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void onFragmentViewReady(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, View rootView) {

        setupAnimation(rootView);
    //    getCaptcha();

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

    @Override
    public void onCustomBackPressed() {
        activityReference.onPageBack();
    }


    @Override
    public void onValidationSuccess() {


        int selectedId = rgCaptcha.getCheckedRadioButtonId();

        if (selectedId != -1) {
            try {
                RadioButton radioButton = (RadioButton) rgCaptcha.findViewById(selectedId);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utils.showToast(activityReference, activityReference.getString(R.string.please_select_captcha), AppConstant.TOAST_TYPES.ERROR);
        }



    }


    @Override
    public void onValidationFail() {
        //Utils.showToast(activityReference,"QR Code Screen Option", AppConstant.TOAST_TYPES.INFO);
    }


    @OnClick({R.id.ivbRefreshIcon, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivbRefreshIcon:
           //     getCaptcha();
                break;
            case R.id.btn_login:
                validateFields();
                break;
        }
    }



}



