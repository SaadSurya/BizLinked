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
import com.application.lumaque.bizlinked.data_models.bizlinked.Product;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final LayoutInflater inflater;
    Context context;
    ArrayList<Product> productsList;




    public ProductAdapter(Context context, List<Product> categoryRecord) {
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.headerData = new ArrayList<>();
        this.productsList = new ArrayList<>();
        this.productsList.addAll(categoryRecord);

    }





    public void clearAllList() {
      //  headerData.clear();
        productsList.clear();
    }

    public void addAllList(ArrayList<Product> data) {
        this.productsList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup view = null;
        int columnCount = 0;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_div_layout, parent, false);

        return new ItemViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        Product currentObject = productsList.get(position);

                ((ItemViewHolder) holder).productName.setText(currentObject.getProductName());
                ((ItemViewHolder) holder).productPrice.setText(String.valueOf(currentObject.getPrice()));
                //((ItemViewHolder) holder).productDesc.setText(currentObject.getProductDescription());

        //String abc = String.valueOf(currentObject.getCompanyID());
        String abc = "0";
        if(currentObject.getImages().size()>0){

            abc = currentObject.getImages().get(0);
        }


        Glide.with(context).load(Utils.getProdImgURL(String.valueOf(currentObject.getCompanyID()),abc))
                .apply(new RequestOptions().centerCrop())
                .into(((ItemViewHolder) holder).categoryImg);




    }




    @Override
    public int getItemCount() {
        return productsList.size();
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView categoryImg;
private TextView productName,productDesc,productPrice;



        ItemViewHolder(View view) {
            super(view);
            //mainView = (ViewGroup) view;


            categoryImg = view.findViewById(R.id.category_img);

            productName  = view.findViewById(R.id.product_name);
           // productDesc  = view.findViewById(R.id.product_desc);
            productPrice  = view.findViewById(R.id.product_price);


       //     FontHelper.getHelper().setFontStyle(FontHelper.FONT_AWESOME_REGULAR, trashTv, context);
        }


    }


}
