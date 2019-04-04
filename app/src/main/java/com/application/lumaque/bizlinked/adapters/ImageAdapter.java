package com.application.lumaque.bizlinked.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.activities.ImageCroppingActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    private ArrayList<Uri> imageUriList;
    private Context ctx;

    public ImageAdapter(Context context, ArrayList<Uri> imageUriList){
        this.ctx = context;
        this.imageUriList = imageUriList;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_images, parent, false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageHolder holder, int position) {
        holder.imageItem.setImageURI(imageUriList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUriList.size();
    }


    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView imageItem;

        ImageHolder(View itemView) {
            super(itemView);
            imageItem = (ImageView) itemView.findViewById(R.id.iv_imgItem);
            imageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ImageCroppingActivity)ctx).setPosition(getAdapterPosition());
                }
            });
        }


    }

}
