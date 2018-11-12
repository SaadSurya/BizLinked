package com.application.lumaque.bizlinked.customViews;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.data_models.ButtonDataModel;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.daimajia.androidanimations.library.Techniques;

import java.util.ArrayList;


public class BottomBarNavigationLayout extends LinearLayout {

    private LinearLayout llBottomNavigationLayout;

    private Context context;
    LayoutInflater inflater;


    public BottomBarNavigationLayout(Context context) {
        super(context);
        this.context = context;
        initLayout(context);
    }


    public BottomBarNavigationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public BottomBarNavigationLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void initLayout(Context context) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_bottom_navigation_layout, this);

        bindViews();
        resetViews();

    }


    public void resetViews() {
        llBottomNavigationLayout.setVisibility(GONE);
    }

    public void showView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationHelpers.animation(Techniques.BounceInUp,  400, llBottomNavigationLayout);
            }
        },500);
        //llBottomNavigationLayout.setVisibility(VISIBLE);
    }

    private void bindViews() {
        llBottomNavigationLayout = (LinearLayout) this.findViewById(R.id.llBottomNavigationLayout);
        llBottomNavigationLayout.removeAllViews();
    }

    public void addButton(ArrayList<ButtonDataModel> buttons) {
        llBottomNavigationLayout.removeAllViews();

        for (int index = 0; index < buttons.size(); index++) {
            Button btn = (Button) inflater.inflate(R.layout.bottom_bar_navigation_button_layout, null);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            if(buttons.size() > 1)
                param.setMarginEnd((int)context.getResources().getDimension(R.dimen.x1));

            btn.setLayoutParams(param);
            btn.setText(buttons.get(index).getText());
            btn.setOnClickListener(buttons.get(index).getOnClickListener());


            if (buttons.get(index).getDrawableEnd() != -1)
                btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, buttons.get(index).getDrawableEnd(), 0);

            if (buttons.get(index).getDrawableStart() != -1)
                btn.setCompoundDrawablesWithIntrinsicBounds(buttons.get(index).getDrawableStart(), 0, 0, 0);


            llBottomNavigationLayout.addView(btn, index);
        }
    }

}


