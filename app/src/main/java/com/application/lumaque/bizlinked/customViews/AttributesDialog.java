package com.application.lumaque.bizlinked.customViews;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.AttributesAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.AttributesTagAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.TagsHorizontalAdapter;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;


public class AttributesDialog extends DialogFragment {

    @BindView(R.id.btn_add)
    ImageButton attBtnAdd;

    @BindView(R.id.btn_add_tag)
    ImageButton tagBtnAdd;
    @BindView(R.id.attribute_cancel)
    Button attributeCancel;
    private BasePreferenceHelper prefHelper;
    private ArrayList<ProductAttribute> companyAttributesList;

    //   protected BaseActivity activityReference;
    @BindView(R.id.att_type)
    AutoCompleteTextView attType;
    @BindView(R.id.att_name)
    AutoCompleteTextView attTag;
    @BindView(R.id.rv_attributes)
    RecyclerView rvAttributes;
    Unbinder unbinder;
    @BindView(R.id.attribute_save)
    Button attributeSave;
    private ProductAttribute SelectedProductAttribute;
    private TagsHorizontalAdapter AttributeItemAdapter;
    boolean isNew;
    List itemAttributes = new ArrayList();
    String[] tags = new String[0];

    AttributesAdapter attributeNameAdapter;
    AttributesTagAdapter attributeTagNameAdapter;

    public AttributesDialog() {

    }

    public void setAttribute(ProductAttribute attribute) {
        this.SelectedProductAttribute = attribute;
    }

    public static AttributesDialog newInstance(ProductAttribute attribute,boolean isNew) {
        AttributesDialog frag = new AttributesDialog();
        frag.setAttribute(attribute);
        frag.isNew = isNew;

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_layout, container);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
        unbinder = ButterKnife.bind(this, view);
        prefHelper = new BasePreferenceHelper(getActivity());
        companyAttributesList = prefHelper.getAttributesList();
        if(!isNew)
        attType.setText(SelectedProductAttribute.getAttributeName());
        getDialog().setTitle("Add Attributes");
        setCancelable(false);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        setAutoTextAdapter();
        if(!isNew)
        setSavedAttributes();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setAutoTextAdapter() {

        attributeNameAdapter = new AttributesAdapter(getContext(), companyAttributesList, attBtnAdd);
        attType.setAdapter(attributeNameAdapter);
        attType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                ProductAttribute selectedItem = (ProductAttribute) arg0.getItemAtPosition(position);
                Toast.makeText(getContext(), selectedItem.getAttributeName(), Toast.LENGTH_SHORT).show();
                for (int a = 0; a < companyAttributesList.size(); a++) {

                    if (companyAttributesList.get(a).AttributeID == SelectedProductAttribute.AttributeID) {
                        tags = companyAttributesList.get(a).getProductAttributeValueName();
                    }


                }


                //  List<String> newList=new ArrayList<>(list);
                ArrayList<String> list = new ArrayList<>(Arrays.asList(tags));
                attributeTagNameAdapter = new AttributesTagAdapter(getContext(), list, tagBtnAdd);
                attTag.setAdapter(attributeTagNameAdapter);

            }

        });

        attBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "new attribute added :" + attType.getText(), Toast.LENGTH_SHORT).show();
                SelectedProductAttribute = new ProductAttribute();
                SelectedProductAttribute.setAttributeName(attType.getText().toString());
                attBtnAdd.setVisibility(View.GONE);
             //   SelectedProductAttribute = abc;

            }
        });







/**
 * get attributes tag for selected attributes
 */
if(!isNew)
        for (int a = 0; a < companyAttributesList.size(); a++) {

            if (companyAttributesList.get(a).AttributeID == SelectedProductAttribute.AttributeID) {
                tags = companyAttributesList.get(a).getProductAttributeValueName();
            }


        }


        //  List<String> newList=new ArrayList<>(list);
        ArrayList<String> list = new ArrayList<>(Arrays.asList(tags));
        attributeTagNameAdapter = new AttributesTagAdapter(getContext(), list, tagBtnAdd);
        attTag.setAdapter(attributeTagNameAdapter);


        attTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
          //      ProductAttribute selectedItem = (ProductAttribute) arg0.getItemAtPosition(position);

                AttributeItemAdapter.addItem((String) arg0.getItemAtPosition(position));
                attTag.setText("");
                tagBtnAdd.setVisibility(View.GONE);
               // Toast.makeText(getContext(), selectedItem.getAttributeName(), Toast.LENGTH_SHORT).show();
            }

        });

        tagBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   Toast.makeText(getContext(), "new tag added :" + attTag.getText(), Toast.LENGTH_SHORT).show();
                AttributeItemAdapter.addItem(attTag.getText().toString());
                attTag.setText("");
                tagBtnAdd.setVisibility(View.GONE);

            }
        });

        AttributeItemAdapter = new TagsHorizontalAdapter(getActivity(), itemAttributes);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        rvAttributes.setLayoutManager(gridLayoutManager);
        // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
        rvAttributes.setAdapter(AttributeItemAdapter);
        rvAttributes.setNestedScrollingEnabled(false);
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

    private void setSavedAttributes() {


      //  itemAttributes = new ArrayList();
        Collections.addAll(itemAttributes, SelectedProductAttribute.getProductAttributeValueName());
        AttributeItemAdapter.addAllList(itemAttributes);


    }

    @OnClick({R.id.attribute_cancel, R.id.attribute_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.attribute_cancel:
               dismiss();
                break;
            case R.id.attribute_save:

if(SelectedProductAttribute == null){

    Utils.showToast(getContext(),"select attribute name", AppConstant.TOAST_TYPES.ERROR);
}else {
    String[] array = new String[AttributeItemAdapter.productCategoryList.size()];
    AttributeItemAdapter.productCategoryList.toArray(array);
    //String[] abc = (String[]) AttributeItemAdapter.productCategoryList.toArray();
    SelectedProductAttribute.setProductAttributeValueName(array);

    Intent i = new Intent()
            .putExtra("attr", SelectedProductAttribute)
            .putExtra("isNew", isNew);
    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
    dismiss();

}
//                dismiss();
                break;
        }
    }
}
