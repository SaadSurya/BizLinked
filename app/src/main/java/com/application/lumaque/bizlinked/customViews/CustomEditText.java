package com.application.lumaque.bizlinked.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.application.lumaque.bizlinked.R;

public class CustomEditText extends LinearLayout {

    Context context;
    EditText edtiText;
    TextInputLayout textInputLayout;
    @StyleableRes
    int index0 = 0;
    @StyleableRes
    int index1 = 1;

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);

    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.floating_hint_edit_text, this, true);
        //   LayoutInflater.from(context).inflate(R.layout.labelled_text_view, this, true);

        int[] sets = {R.attr.hint, R.attr.text};

        TypedArray typedArray = context.obtainStyledAttributes(attrs, sets);
        CharSequence hint = typedArray.getText(index0);
        CharSequence text = typedArray.getText(index1);
        typedArray.recycle();

        initComponents();
        setHint(hint);
        setText(text);

    }

    private void initComponents() {
        edtiText = findViewById(R.id.floatingedittext);
        textInputLayout = findViewById(R.id.text_input_layout);
    }

    public void setHint(CharSequence text) {


        edtiText.setHint("");
        textInputLayout.setHint(text);

    }

    public CharSequence getText() {

        return edtiText.getText();
    }

    public void setText(CharSequence text) {
        edtiText.setText(text);


    }


}
