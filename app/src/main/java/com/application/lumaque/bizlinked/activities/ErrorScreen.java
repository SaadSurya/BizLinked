package com.application.lumaque.bizlinked.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.helpers.common.FontHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class ErrorScreen extends BaseActivity {

    public static final String STACK_TRACE = "STACK";

    @BindView(R.id.sad_face)
    TextView sadText;
    @BindView(R.id.error_text)
    TextView errorText;

    private Throwable throwable;

    @Override
    public int getMainLayoutId() {
        return R.layout.activity_error_screen;
    }

    @Override
    public int getFragmentFrameLayoutId() {
        return 0;
    }

    @Override
    protected void onViewReady() {
        initializeViews();
    }

    private void initializeViews() {
        FontHelper.getHelper().setFontStyle(
                FontHelper.FONT_AWESOME_REGULAR, sadText, this
        );
        errorText.setText(getErrorTrace());
    }

    private String getErrorTrace() {
        Bundle bundle = getIntent().getExtras();
        return bundle != null ? bundle.getString(STACK_TRACE) : getString(R.string.error_text);
    }




    @OnClick(R.id.btn_retry)
    public void onViewClicked() {
        startSplashScreen();

    }

    private void startSplashScreen(){
        Intent intent=new Intent(this,RegistrationActivity.class);
        startActivity(intent);
        finish();
    }
}
