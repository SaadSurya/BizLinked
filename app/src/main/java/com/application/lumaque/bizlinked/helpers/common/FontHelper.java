package com.application.lumaque.bizlinked.helpers.common;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontHelper {
    private static final String ROOT = "fonts/";
    public static final String FONT_AWESOME_BRANDS = ROOT + "fa_brands_400.ttf";
    public static final String FONT_AWESOME_REGULAR = ROOT + "fa_regular_400.ttf";
    public static final String FONT_AWESOME_SOLID = ROOT + "fa_solid_900.ttf";
    private static FontHelper helper;

    public static FontHelper getHelper() {
        if (helper == null)
            helper = new FontHelper();

        return helper;
    }

    public Typeface getTypeface(String font, Context context) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public void setFontStyle(String fontStyle, TextView textView, Context context) {
        textView.setTypeface(getTypeface(fontStyle, context));
    }


}
