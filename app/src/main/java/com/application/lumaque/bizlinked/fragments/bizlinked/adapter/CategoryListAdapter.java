package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.baseClass.BaseActivity;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.fragments.bizlinked.CategoryListFragment;
import com.application.lumaque.bizlinked.fragments.bizlinked.NewCategoryFragment;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.network.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ProductCategory> productCategoryList;
    private Activity activityReference;

    public CategoryListAdapter(Context context, Activity activityReference, List<ProductCategory> categoryRecord) {
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        //  this.headerData = new ArrayList<>();
        this.productCategoryList = new ArrayList<>();
        this.productCategoryList.addAll(categoryRecord);
        this.activityReference = activityReference;

    }


    public void clearAllList() {
        //  headerData.clear();
        productCategoryList.clear();
    }

    public void addAllList(ArrayList<ProductCategory> data) {
        this.productCategoryList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup view = null;
        int columnCount = 0;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_div_layout, parent, false);

        return new ItemViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final ProductCategory currentObject = productCategoryList.get(position);
        ((ItemViewHolder) holder).productName.setText(currentObject.getProductCategoryName());

        if (currentObject.getSubProductCategories() == null || currentObject.getSubProductCategories().isEmpty()) {
            ((ItemViewHolder) holder).editCat.setVisibility(View.GONE);
        } else {
            ((ItemViewHolder) holder).editCat.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(Utils.getProdImgURL(String.valueOf(currentObject.getCompanyID()), currentObject.getImageID()))
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())).centerCrop())
                .into(((ItemViewHolder) holder).categoryImg);

        ((ItemViewHolder) holder).editCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isNetworkAvailable(activityReference)) {
                    try {
                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("CAT_OBJ", currentObject.getSubProductCategories().get(position));
                        bundle.putSerializable("CAT_OBJ", currentObject);
                        NewCategoryFragment newCategoryFragment = new NewCategoryFragment();
                        newCategoryFragment.setArguments(bundle);
                        ((BaseActivity) activityReference).addSupportFragment(newCategoryFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);

                    } catch (Exception e) {
                        Utils.showToast(activityReference, activityReference.getString(R.string.will_be_implemented), AppConstant.TOAST_TYPES.INFO);
                        e.printStackTrace();
                    }

                } else {
          /*          Utils.showSnackBar(activityReference, getContainerLayout(),
                            activityReference.getResources().getString(R.string.no_network_available),
                            ContextCompat.getColor(activityReference, R.color.grayColor));*/
                }
            }
        });
        ((ItemViewHolder) holder).viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentObject.getSubProductCategories() != null && !currentObject.getSubProductCategories().isEmpty()) {
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("CAT_OBJ", currentObject);
                        bundle.putBoolean("FROMCATLIST", true);
                        CategoryListFragment categoryListFragment = new CategoryListFragment();
                        categoryListFragment.setArguments(bundle);
                        ((BaseActivity) activityReference).addSupportFragment(categoryListFragment, AppConstant.TRANSITION_TYPES.SLIDE, true);
                    } catch (Exception e) {
                        Utils.showToast(activityReference, activityReference.getString(R.string.will_be_implemented), AppConstant.TOAST_TYPES.INFO);
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView categoryImg;
        private TextView productName;
        private TextView editCat;
        private LinearLayout viewLayout;


        ItemViewHolder(View view) {
            super(view);
            //mainView = (ViewGroup) view;


            categoryImg = view.findViewById(R.id.category_img);

            productName = view.findViewById(R.id.product_name);
            editCat = view.findViewById(R.id.tv_edit_cat);
            viewLayout = view.findViewById(R.id.view_layout);


            //     FontHelper.getHelper().setFontStyle(FontHelper.FONT_AWESOME_REGULAR, trashTv, context);
        }


    }


}
