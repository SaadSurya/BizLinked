package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.List;

public class CategoryHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final LayoutInflater inflater;
    Context context;
    ArrayList<ProductCategory> productCategoryList;




    public CategoryHorizontalAdapter(Context context, List<ProductCategory> categoryRecord) {
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.headerData = new ArrayList<>();
        this.productCategoryList = new ArrayList<>();
        this.productCategoryList.addAll(categoryRecord);

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

        ProductCategory currentObject = productCategoryList.get(position);


                ((ItemViewHolder) holder).productName.setText(currentObject.getProductCategoryName());
        Glide.with(context).load(Utils.getProdImgURL(String.valueOf(currentObject.getCompanyID()),currentObject.getImageID()))
                .apply(new RequestOptions().centerCrop())
                .into(((ItemViewHolder) holder).categoryImg);


    }




    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView categoryImg;
private TextView productName;



        ItemViewHolder(View view) {
            super(view);
            //mainView = (ViewGroup) view;


            categoryImg = view.findViewById(R.id.category_img);

            productName  = view.findViewById(R.id.product_name);


       //     FontHelper.getHelper().setFontStyle(FontHelper.FONT_AWESOME_REGULAR, trashTv, context);
        }


    }


}
