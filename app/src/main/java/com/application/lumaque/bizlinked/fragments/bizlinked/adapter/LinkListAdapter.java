package com.application.lumaque.bizlinked.fragments.bizlinked.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;
import com.application.lumaque.bizlinked.fragments.bizlinked.CustomRecyclerCallBacks;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;


import java.util.ArrayList;

public class LinkListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final LayoutInflater inflater;
    Context context;
    ArrayList<CompanyHeadModel> linkRecord;
 //   private ArrayList<ArrayList<Record>> headerData;

    private CustomRecyclerCallBacks customrecyclercallbacks;



    public LinkListAdapter(Context context, ArrayList<CompanyHeadModel> linkRecord, CustomRecyclerCallBacks customrecyclercallbacks) {
        this.context = context;
        inflater = LayoutInflater.from(context);
      //  this.headerData = new ArrayList<>();
        this.linkRecord = new ArrayList<>();
        this.linkRecord.addAll(linkRecord);
        this.customrecyclercallbacks = customrecyclercallbacks;
    }



    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    public void clearAllList() {
      //  headerData.clear();
        linkRecord.clear();
    }

    public void addAllList(ArrayList<CompanyHeadModel> data) {
        this.linkRecord.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup view = null;
        int columnCount = 0;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.section_link_list_item, parent, false);

        return new ItemViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        CompanyHeadModel currentObject =linkRecord.get(position);



        ((ItemViewHolder) holder).tvName.setText(currentObject.getCompanyName());
        Glide.with(context).load(AppConstant.ServerAPICalls.GET_MEDIA_FILE+currentObject.getCompanyID())
                .apply(new RequestOptions().signature(new ObjectKey(System.currentTimeMillis())))
                .into(((ItemViewHolder) holder).profilePicView);



setAddDeleteViews(currentObject,((ItemViewHolder) holder).imSub,((ItemViewHolder) holder).imAdd);



    }




    @Override
    public int getItemCount() {
        return linkRecord.size();
    }




    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView tvName;
        private ImageButton imSub,imAdd;

        private de.hdodenhof.circleimageview.CircleImageView profilePicView;


        ItemViewHolder(View view) {
            super(view);
            //mainView = (ViewGroup) view;

            tvName = view.findViewById(R.id.tvItem);
            imSub = view.findViewById(R.id.link_sub);
            imAdd = view.findViewById(R.id.link_add);
            profilePicView = view.findViewById(R.id.profile_image);


            profilePicView.setOnClickListener(this);
            imSub.setOnClickListener(this);
            imAdd.setOnClickListener(this);
            tvName.setOnClickListener(this);


       //     FontHelper.getHelper().setFontStyle(FontHelper.FONT_AWESOME_REGULAR, trashTv, context);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();


            switch (view.getId()) {
                case R.id.profile_image: {

                 customrecyclercallbacks.onImageClick(linkRecord.get(position));
                }
                break;
                case R.id.tvItem: {

                 customrecyclercallbacks.onListClick(linkRecord.get(position));
                }
                break;
                default:
                    customrecyclercallbacks.onActionClick(linkRecord.get(position),view.getTag().toString());
                    break;
            }




        }
    }


    private  void setAddDeleteViews(CompanyHeadModel currentObject, ImageButton subBtn, ImageButton addBtn){

        if(currentObject.getLinkStatus().equalsIgnoreCase("N"))
        {
            addBtn.setVisibility(View.VISIBLE);
            subBtn.setVisibility(View.GONE);

        }
        else if(currentObject.getLinkStatus().equalsIgnoreCase("L"))
        {
            addBtn.setVisibility(View.GONE);
            subBtn.setVisibility(View.VISIBLE);

        }
        else if(currentObject.getLinkStatus().equalsIgnoreCase("S"))
        {
            addBtn.setVisibility(View.GONE);
            subBtn.setVisibility(View.VISIBLE);

        }
        else if(currentObject.getLinkStatus().equalsIgnoreCase("R"))
        {
            addBtn.setVisibility(View.VISIBLE);
            subBtn.setVisibility(View.VISIBLE);

        }

    }

}
