package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.application.lumaque.bizlinked.data_models.bizlinked.ProductAttribute;

import java.util.ArrayList;

public class AttributesTagAdapter extends ArrayAdapter<String> {

    ArrayList<String> customers, tempCustomer, suggestions;

    Context context;
    ImageButton imageButton;
    public AttributesTagAdapter(Context context, ArrayList<String> objects, ImageButton imageButton) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.customers = objects;
        this.imageButton = imageButton;
        this.tempCustomer = new ArrayList<String>(objects);
        this.suggestions = new ArrayList<String>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String tags = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView txtCustomer = (TextView) convertView.findViewById(android.R.id.text1);
        //ImageView ivCustomerImage = (ImageView) convertView.findViewById(R.id.ivCustomerImage);

        if (txtCustomer != null)
            txtCustomer.setText(tags);

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
            String customer = (String) resultValue;
            return customer;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            setAttAddVisibility(true);
            if (constraint != null) {
                suggestions.clear();
                setAttAddVisibility(true);
                for (String people : tempCustomer) {
                    if (people.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                        setAttAddVisibility(false);


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
            ArrayList<String> c = (ArrayList<String>) results.values;
            if (results != null && results.count > 0) {
                clear();
                setAttAddVisibility(false);
                for (String cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                setAttAddVisibility(true);
                notifyDataSetChanged();
            }
        }
    };
    public void setAttAddVisibility(boolean isVisible) {


        if (isVisible)
            imageButton.setVisibility(View.VISIBLE);
        else
            imageButton.setVisibility(View.GONE);

    }
}
