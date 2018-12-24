package com.application.lumaque.bizlinked.customViews;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.TagsHorizontalAdapter;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;


public class AttributesDialog extends DialogFragment {

    private BasePreferenceHelper prefHelper;
private ArrayList<ProductAttribute> productAttributes ;

    //   protected BaseActivity activityReference;
    @BindView(R.id.att_type)
    AutoCompleteTextView attType;
    @BindView(R.id.att_name)
    AutoCompleteTextView attName;
    @BindView(R.id.rv_attributes)
    RecyclerView rvAttributes;
    Unbinder unbinder;
    @BindView(R.id.attribute_save)
    Button attributeSave;
    private ProductAttribute ProductAttribute;
    private TagsHorizontalAdapter AttributeItemAdapter;

    List itemAttributes;


    public AttributesDialog() {

    }

    public void setAttribute(ProductAttribute attribute) {
        this.ProductAttribute = attribute;
    }

    public static AttributesDialog newInstance(ProductAttribute attribute) {
        AttributesDialog frag = new AttributesDialog();
        frag.setAttribute(attribute);
        // this.activityReference = activityReference;
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
        unbinder = ButterKnife.bind(this, view);
        prefHelper = new BasePreferenceHelper(getActivity());
        productAttributes = prefHelper.getAttributesList();




        attType.setText(ProductAttribute.getAttributeName());

        getDialog().setTitle("Add Attributes");

        itemAttributes = new ArrayList();
        Collections.addAll(itemAttributes, ProductAttribute.getProductAttributeValueName());


        AttributeItemAdapter = new TagsHorizontalAdapter(getActivity(), itemAttributes);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvAttributes.setLayoutManager(gridLayoutManager);
        // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
        rvAttributes.setAdapter(AttributeItemAdapter);
        rvAttributes.setNestedScrollingEnabled(false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view


        rvAttributes.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvAttributes,
                new ClickListenerRecycler() {
                    @Override
                    public void onClick(View view, final int position) {


                        new AlertDialog.Builder(getActivity())
                                .setTitle("Your Alert")
                                .setMessage("This Tag Will Be Deleted")
                                .setCancelable(true)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        AttributeItemAdapter.removeItem(position);
                                    }
                                }).show();


                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }
        ));


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
