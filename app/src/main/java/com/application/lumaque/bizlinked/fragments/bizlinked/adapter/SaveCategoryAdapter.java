package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory;

import java.util.ArrayList;

public class SaveCategoryAdapter extends ArrayAdapter<ProductCategory> {

    ArrayList<ProductCategory> customers, tempCustomer, suggestions;

    Context context;

    public SaveCategoryAdapter(Context context, ArrayList<ProductCategory> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.customers = objects;
        this.tempCustomer = new ArrayList<ProductCategory>(objects);
        this.suggestions = new ArrayList<ProductCategory>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductCategory productCategory = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView txtCustomer = (TextView) convertView.findViewById(android.R.id.text1);
        //ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.ivCustomerImage);

        if (txtCustomer != null)
            txtCustomer.setText(productCategory.getProductCategoryName());

        //   convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));


        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ProductCategory customer = (ProductCategory) resultValue;
            return customer.getProductCategoryName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //  setAttAddVisibility(true);
            if (constraint != null) {
                suggestions.clear();
                //   setAttAddVisibility(true);
                for (ProductCategory people : tempCustomer) {
                    if (people.getProductCategoryName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                        //       setAttAddVisibility(false);


                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ProductCategory> c = (ArrayList<ProductCategory>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ProductCategory cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();

            }
        }
    };
}
