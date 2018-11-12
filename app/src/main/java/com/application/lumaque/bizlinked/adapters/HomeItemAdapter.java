package com.application.lumaque.bizlinked.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.HomeItemDataModel;
import com.application.lumaque.bizlinked.helpers.animation.AnimationHelpers;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.daimajia.androidanimations.library.Techniques;

import java.util.ArrayList;


public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {


    final LayoutInflater inflater;
    Context context;
    ArrayList<HomeItemDataModel> data;


    public HomeItemAdapter(Context context, ArrayList<HomeItemDataModel> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = new ArrayList<>();
        this.data.addAll(data);

    }

    public void clearAllList() {
        data.clear();
    }

    public void addAllList(ArrayList<HomeItemDataModel> data) {
        this.data.addAll(data);
        //notifyItemRangeChanged(0, this.data.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.item_normal_home_circle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        HomeItemDataModel object = data.get(position);

        String mDrawableName = object.getImage();
        int resID = Utils.getResourceId(context, mDrawableName, AppConstant.RESOURCE_FOLDER.DRAWABLES);
        if(resID != -1)
            holder.homeItemImage.setImageResource(resID);

//        holder.homeItemImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // PRESSED
//                        holder.homeItemImage.setBackgroundResource(R.drawable.normal_home_circle_select);
//                        return true; // if you want to handle the touch event
//                    case MotionEvent.ACTION_UP:
//                        // RELEASED
//                        holder.homeItemImage.setBackgroundResource(R.drawable.normal_home_circle_unselect);
//                        return true; // if you want to handle the touch event
//                }
//                return false;
//            }
//        });

        holder.homeItemImagetext.setText(object.getName());


        //For Animation Work
        holder.llContainer.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimationHelpers.animation(Techniques.FlipInY,  600, holder.llContainer);
            }
        },500);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llContainer;
        ImageView homeItemImage;
        TextView homeItemImagetext;

        ViewHolder(View itemView) {
            super(itemView);
            llContainer = (LinearLayout) itemView;
            homeItemImage = (ImageView) itemView.findViewById(R.id.imgCircleOptions);
            homeItemImagetext = (TextView) itemView.findViewById(R.id.imgCircleOptionsText);

        }


    }
}