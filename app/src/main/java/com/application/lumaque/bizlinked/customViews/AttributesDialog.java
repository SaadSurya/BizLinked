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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.AttributesAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.AttributesTagAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.adapter.TagsHorizontalAdapter;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.helpers.recycler_touchHelper.RecyclerTouchListener;
import com.application.lumaque.bizlinked.listener.ClickListenerRecycler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;


public class AttributesDialog extends DialogFragment {

    @BindView(R.id.btn_add)
    ImageButton attBtnAdd;

    @BindView(R.id.btn_add_tag)
    ImageButton tagBtnAdd;
    private BasePreferenceHelper prefHelper;
    private ArrayList<ProductAttribute> companyAttributesList;

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
    private ProductAttribute SelectedProductAttribute;
    private TagsHorizontalAdapter AttributeItemAdapter;

    List itemAttributes;
    String [] tags = new String[0];

    AttributesAdapter attributeNameAdapter;
    AttributesTagAdapter attributeTagNameAdapter;
    public AttributesDialog() {

    }

    public void setAttribute(ProductAttribute attribute) {
        this.SelectedProductAttribute = attribute;
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
        companyAttributesList = prefHelper.getAttributesList();


        attType.setText(SelectedProductAttribute.getAttributeName());

        getDialog().setTitle("Add Attributes");

        itemAttributes = new ArrayList();
        Collections.addAll(itemAttributes, SelectedProductAttribute.getProductAttributeValueName());


        AttributeItemAdapter = new TagsHorizontalAdapter(getActivity(), itemAttributes);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvAttributes.setLayoutManager(gridLayoutManager);
        // linkRecycler.setLayoutManager(new LinearLayoutManager(activityReference));
        rvAttributes.setAdapter(AttributeItemAdapter);
        rvAttributes.setNestedScrollingEnabled(false);

        setTextAdapter();

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


    private void setTextAdapter() {

        attributeNameAdapter = new AttributesAdapter(getContext(), companyAttributesList, attBtnAdd);
        attType.setAdapter(attributeNameAdapter);


        attType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > arg0, View arg1, int position, long arg3) {
                ProductAttribute selectedItem = (ProductAttribute) arg0.getItemAtPosition(position);


                Toast.makeText(getContext(), selectedItem.getAttributeName(), Toast.LENGTH_SHORT).show();
            }

        });

        attBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getContext(), "new attribute added :" + attType.getText(), Toast.LENGTH_SHORT).show();


            }
        });




       // ArrayList<String> tags = new ArrayList<>();

        for(int a = 0; a < companyAttributesList.size();a++){


            if(companyAttributesList.get(a).AttributeID == SelectedProductAttribute.AttributeID){
                tags = companyAttributesList.get(a).getProductAttributeValueName();

            }


        }
      //  List<String> newList=new ArrayList<>(list);
        ArrayList<String> list =  new ArrayList<>(Arrays.asList(tags));
        attributeTagNameAdapter = new AttributesTagAdapter(getContext(),list, tagBtnAdd);
        attName.setAdapter(attributeTagNameAdapter);


        attName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > arg0, View arg1, int position, long arg3) {
                ProductAttribute selectedItem = (ProductAttribute) arg0.getItemAtPosition(position);


                Toast.makeText(getContext(), selectedItem.getAttributeName(), Toast.LENGTH_SHORT).show();
            }

        });

        tagBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getContext(), "new tag added :" + attType.getText(), Toast.LENGTH_SHORT).show();


            }
        });

    }




  /*  public void setAttAddVisibility(boolean isVisible) {


        if (isVisible)
            attBtnAdd.setVisibility(View.VISIBLE);
        else
            attBtnAdd.setVisibility(View.GONE);

    }*/


 /*   public void setTagAddVisibility(boolean isVisible) {


        if (isVisible)
            tagAddButton.setVisibility(View.VISIBLE);
        else
            tagAddButton.setVisibility(View.GONE);

    }
*/

}
