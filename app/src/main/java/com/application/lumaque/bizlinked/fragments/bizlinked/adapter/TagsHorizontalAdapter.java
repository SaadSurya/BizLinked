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
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagsHorizontalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final LayoutInflater inflater;
    Context context;

    List productCategoryList;




    public TagsHorizontalAdapter(Context context, List attValues) {
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.headerData = new ArrayList<>();
        this.productCategoryList = new ArrayList<>();
        productCategoryList.addAll(attValues);

    }





    public void clearAllList() {
      //  headerData.clear();
        productCategoryList.clear();
    }
    public void removeItem(int position) {


        productCategoryList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String item) {


        productCategoryList.add(item);
        notifyDataSetChanged();
    }

    public void addAllList(String[] attValues) {
        Collections.addAll(productCategoryList, attValues);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup view = null;
        int columnCount = 0;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_layout, parent, false);

        return new ItemViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

       // ProductCategory currentObject = productCategoryList.get(position);


                ((ItemViewHolder) holder).att_name.setText(productCategoryList.get(position).toString());


    }




    @Override
    public int getItemCount() {
        return productCategoryList.size();
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder {


       // private ImageView categoryImg;
private TextView att_name;



        ItemViewHolder(View view) {
            super(view);
            //mainView = (ViewGroup) view;


            att_name = view.findViewById(R.id.att_name);

          //  productName  = view.findViewById(R.id.product_name);


       //     FontHelper.getHelper().setFontStyle(FontHelper.FONT_AWESOME_REGULAR, trashTv, context);
        }


    }


}
