package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;
import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;
import com.application.lumaque.bizlinked.fragments.bizlinked.TagCloseCallBack;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TagViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final LayoutInflater inflater;
    Context context;
    ArrayList<ProductAttribute> productAttributesList;

    private TagCloseCallBack tagcallbacks;

    public TagViewAdapter(Context context, List<ProductAttribute> categoryRecord,TagCloseCallBack tagcallbacks) {
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.headerData = new ArrayList<>();
        this.productAttributesList = new ArrayList<>();
        this.productAttributesList.addAll(categoryRecord);
        this.tagcallbacks = tagcallbacks;
    }





    public void clearAllList() {
      //  headerData.clear();
        productAttributesList.clear();
    }

    public void addAllList(ArrayList<ProductAttribute> data) {
        this.productAttributesList.addAll(data);
        notifyDataSetChanged();
    }

public void notifyChangeData(){
    notifyDataSetChanged();

}
    public void addItem(ProductAttribute item) {
        this.productAttributesList.add(item);
        notifyDataSetChanged();
    }

    public ArrayList<ProductAttribute> getAttributeLIst(){

        return productAttributesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup view = null;
        int columnCount = 0;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attribute_row, parent, false);

        return new ItemViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        ProductAttribute currentObject = productAttributesList.get(position);


                ((ItemViewHolder) holder).attributeName.setText(currentObject.getAttributeName());

                 currentObject.getProductAttributeValueName();

                 showInfo(((ItemViewHolder) holder).tagDiv,currentObject.getProductAttributeValueName());
        ((ItemViewHolder) holder).Row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagcallbacks.onRowClick(productAttributesList.get(position));
            }
        });

    }




    @Override
    public int getItemCount() {
        return productAttributesList.size();
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{


        ImageButton deleteRow;
        LinearLayout Row;

        TextView  attributeName;

        LinearLayout tagDiv;

        ItemViewHolder(View view) {
            super(view);

            Row = view.findViewById(R.id.row);
            deleteRow = view.findViewById(R.id.delete_attribute);
            attributeName = view.findViewById(R.id.pro_attribute_name);
            tagDiv  = view.findViewById(R.id.tag_div);
            deleteRow.setOnClickListener(this);
//            Row.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();


            switch (view.getId()) {
                case R.id.delete_attribute: {

                    tagcallbacks.onImageClick(productAttributesList.get(position));

                }
                break;
               /* case R.id.row: {

                    tagcallbacks.onRowClick(productAttributesList.get(position));

                }
                break;*/

            }
        }

    }

    private void showInfo(LinearLayout linearLayout, String[] names){
        LayoutInflater layoutInfralte=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

linearLayout.removeAllViews();
        for (int a=0;a<names.length;a++){
            View view=layoutInfralte.inflate(R.layout.tag_layout, null);
            TextView tv=view.findViewById(R.id.att_name);
            tv.setText(names[a]);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(view);

        }



    }
}
