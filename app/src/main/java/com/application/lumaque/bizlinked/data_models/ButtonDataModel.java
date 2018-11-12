package com.application.lumaque.bizlinked.data_models;

import android.view.View;

import java.io.Serializable;

public class ButtonDataModel implements Serializable {


    String text;
    View.OnClickListener onClickListener;
    int drawableEnd;
    int drawableStart;

    public ButtonDataModel(String text, View.OnClickListener onClickListener, int drawableStart, int drawableEnd) {
        this.text = text;
        this.onClickListener = onClickListener;
        this.drawableEnd = drawableEnd;
        this.drawableStart = drawableStart;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getDrawableEnd() {
        return drawableEnd;
    }

    public void setDrawableEnd(int drawableEnd) {
        this.drawableEnd = drawableEnd;
    }

    public int getDrawableStart() {
        return drawableStart;
    }

    public void setDrawableStart(int drawableStart) {
        this.drawableStart = drawableStart;
    }
}
