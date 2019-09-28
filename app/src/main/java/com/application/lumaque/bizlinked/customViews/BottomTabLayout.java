package com.application.lumaque.bizlinked.customViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.daimajia.androidanimations.library.Techniques;

import java.io.Serializable;
import java.util.ArrayList;


public class BottomTabLayout extends LinearLayout {

    ArrayList<BottomBarModal> arrayList = new ArrayList<>();
    BottomOptionSelectedInterface bottomOptionSelectedInterface;
    private LinearLayout llBottomBarLayout;
    private TextView tvAlert;
    private TextView tvDashboard;
    private TextView tvLiveChat;
    private TextView tvFavourites;
    private Context context;


    public BottomTabLayout(Context context) {
        super(context);
        this.context = context;
        initLayout(context);
    }

    public BottomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public BottomTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayout(context);
        if (attrs != null)
            initAttrs(context, attrs);
    }

    public static boolean areDrawablesIdentical(Drawable drawableA, Drawable drawableB) {
        Drawable.ConstantState stateA = drawableA.getConstantState();
        Drawable.ConstantState stateB = drawableB.getConstantState();
        // If the constant state is identical, they are using the same drawable resource.
        // However, the opposite is not necessarily true.
        return (stateA != null && stateB != null && stateA.equals(stateB))
                || getBitmap(drawableA).sameAs(getBitmap(drawableB));
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap result;
        if (drawable instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable).getBitmap();
        } else {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            // Some drawables have no intrinsic width - e.g. solid colours.
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }

            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return result;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    private void initLayout(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.home_bottom_tab_layout, this);

        bindViews();
        resetViews();

    }

    public void resetViews() {
        llBottomBarLayout.setVisibility(GONE);
    }

    public void showView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationHelpers.animation(Techniques.BounceInUp, 400, llBottomBarLayout);
            }
        }, 250);
        //llBottomBarLayout.setVisibility(VISIBLE);
    }

    private void bindViews() {

        llBottomBarLayout = (LinearLayout) this.findViewById(R.id.llBottomBarLayout);
        tvAlert = (TextView) this.findViewById(R.id.tvAlert);
        tvDashboard = (TextView) this.findViewById(R.id.tvDashboard);
        tvLiveChat = (TextView) this.findViewById(R.id.tvLiveChat);
        tvFavourites = (TextView) this.findViewById(R.id.tvFavourites);


        setBottomLayout();

        setTouchListener(tvAlert);
        setTouchListener(tvDashboard);
        setTouchListener(tvLiveChat);
        setTouchListener(tvFavourites);
    }

    private void setBottomLayout() {
        arrayList.add(new BottomBarModal(R.drawable.ic_alert, R.drawable.ic_alert_active, R.color.appThemeColor, R.color.home_normal_bottom_tab_text_color, "AlertFragment"));
        arrayList.add(new BottomBarModal(R.drawable.ic_dashboard, R.drawable.ic_dashboard_active, R.color.appThemeColor, R.color.home_normal_bottom_tab_text_color, "AlertFragmen"));
        arrayList.add(new BottomBarModal(R.drawable.ic_live_chat, R.drawable.ic_live_chat_active, R.color.appThemeColor, R.color.home_normal_bottom_tab_text_color, "AlertFragme"));
        arrayList.add(new BottomBarModal(R.drawable.ic_favourite, R.drawable.ic_favourite_active, R.color.appThemeColor, R.color.home_normal_bottom_tab_text_color, "AlertFragm"));
    }

    private void setTouchListener(View v) {
        final Drawable[] topCompoundDrawable = {null};
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        if (v instanceof TextView) {
                            // Left, top, right, bottom drawables.
                            Drawable[] compundDrawables = ((TextView) v).getCompoundDrawables();
                            // This is the top drawable.
                            topCompoundDrawable[0] = compundDrawables[1];
                            for (int tabIndex = 0; tabIndex < arrayList.size(); tabIndex++) {
                                if (areDrawablesIdentical(topCompoundDrawable[0], getResources().getDrawable(arrayList.get(tabIndex).getUnselected_image()))) {
                                    ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(arrayList.get(tabIndex).getSelected_image()), null, null);
                                    ((TextView) v).setTextColor(getResources().getColor(arrayList.get(tabIndex).getSelected_color()));
                                    bottomOptionSelectedInterface.onOptionSelected(arrayList.get(tabIndex).getClassName());
                                    break;
                                }


                            }
                        }
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null, topCompoundDrawable[0], null, null);
                        ((TextView) v).setTextColor(getResources().getColor(arrayList.get(0).getUnselected_color()));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    public void setBottomBarListener(BottomOptionSelectedInterface listener) {
        bottomOptionSelectedInterface = listener;
    }


//    public void setTitle(String title,int color) {
//        txtTitle.setVisibility(View.VISIBLE);
//        txtTitle.setText(title);
//        txtTitle.setTextColor(color);
//    }
//    public void showSearchButtonAndBindClickListener(OnClickListener onClickListener) {
//        searchBtn.setVisibility(View.VISIBLE);
//        searchBtn.setOnClickListener(onClickListener);
//    }


    public interface BottomOptionSelectedInterface {
        void onOptionSelected(String className);
    }

    public class BottomBarModal implements Serializable {

        private int selected_image;
        private int unselected_image;
        private int unselected_color;
        private int selected_color;
        private String className;

        BottomBarModal(int unselected_image, int selected_image, int selected_color, int unselected_color, String className) {
            this.unselected_image = unselected_image;
            this.selected_image = selected_image;
            this.selected_color = selected_color;
            this.unselected_color = unselected_color;
            this.className = className;
        }

        int getSelected_image() {
            return selected_image;
        }

        void setSelected_image(int selected_image) {
            this.selected_image = selected_image;
        }

        int getUnselected_image() {
            return unselected_image;
        }

        void setUnselected_image(int unselected_image) {
            this.unselected_image = unselected_image;
        }

        int getUnselected_color() {
            return unselected_color;
        }

        void setUnselected_color(int unselected_color) {
            this.unselected_color = unselected_color;
        }

        int getSelected_color() {
            return selected_color;
        }

        void setSelected_color(int selected_color) {
            this.selected_color = selected_color;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }

}
