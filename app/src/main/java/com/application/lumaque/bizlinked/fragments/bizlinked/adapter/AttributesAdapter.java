package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;

import java.util.ArrayList;

public class AttributesAdapter extends ArrayAdapter<ProductAttribute> {

    ArrayList<ProductAttribute> customers, tempCustomer, suggestions;

    Context context;
    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ProductAttribute customer = (ProductAttribute) resultValue;
            return customer.getAttributeName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //  setAttAddVisibility(true);
            if (constraint != null) {
                suggestions.clear();
                //   setAttAddVisibility(true);
                for (ProductAttribute people : tempCustomer) {
                    if (people.getAttributeName().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
            ArrayList<ProductAttribute> c = (ArrayList<ProductAttribute>) results.values;
            if (results != null && results.count > 0) {
                clear();
                // setAttAddVisibility(false);
                for (ProductAttribute cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                //   setAttAddVisibility(true);
                notifyDataSetChanged();

            }
        }
    };

    //   ImageButton imageButton;
    public AttributesAdapter(Context context, ArrayList<ProductAttribute> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.customers = objects;
        //   this.imageButton = imageButton;
        this.tempCustomer = new ArrayList<ProductAttribute>(objects);
        this.suggestions = new ArrayList<ProductAttribute>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductAttribute productAttribute = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView txtCustomer = (TextView) convertView.findViewById(android.R.id.text1);
        //ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.ivCustomerImage);

        if (txtCustomer != null)
            txtCustomer.setText(productAttribute.AttributeName);

        //   convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));


        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

}
