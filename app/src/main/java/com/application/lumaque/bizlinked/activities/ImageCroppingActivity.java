package com.application.lumaque.bizlinked.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.lumaque.bizlinked.R;
import com.application.lumaque.bizlinked.adapters.ImageAdapter;
import com.application.lumaque.bizlinked.fragments.bizlinked.ProductFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;

public class ImageCroppingActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView iv_imgToView;
    RecyclerView rv_images;
    TextView tv_crop;
    FloatingActionButton fab_done;

    ArrayList<Uri> imageUriList;
    ImageAdapter imageAdapter;

    ArrayList<File> imageFile;

    int clickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropping);

        initViews();
        initRecyclerView();

    }

    public void initViews() {
        rv_images = (RecyclerView) findViewById(R.id.rv_images);
        iv_imgToView = (ImageView) findViewById(R.id.iv_imgToView);
        tv_crop = (TextView) findViewById(R.id.tv_crop);
        fab_done = (FloatingActionButton) findViewById(R.id.fab_done);

        tv_crop.setOnClickListener(this);
        fab_done.setOnClickListener(this);
    }

    public void initRecyclerView() {

        Intent intent = getIntent();
        imageUriList = intent.getParcelableArrayListExtra("imageList");
        imageFile = new ArrayList<>();

        imageAdapter = new ImageAdapter(this, imageUriList);

        rv_images.setAdapter(imageAdapter);
        rv_images.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rv_images.setNestedScrollingEnabled(false);
        rv_images.setBackgroundColor(getResources().getColor(R.color.rv_backGround));
    }

    public void setPosition(int position) {
        this.clickedPosition = position;
        tv_crop.setVisibility(View.VISIBLE);
        iv_imgToView.setImageURI(imageUriList.get(position));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_crop:
            if (clickedPosition != -1) {
                CropImage.activity(imageUriList.get(clickedPosition))
                        .start(this);

            }
                break;

            case R.id.fab_done:
                proceedWithCroppedImages();
                break;
        }
    }

    private void proceedWithCroppedImages() {

        for (int i=0 ; i<imageUriList.size() ; i++){
            imageFile.add(i, new File(imageUriList.get(i).getPath()));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && null != data) {
//            imageFile.add(new File(CropImage.getActivityResult(data).getUri().getPath()));
            imageUriList.set(clickedPosition, CropImage.getActivityResult(data).getUri());
            imageAdapter.notifyItemChanged(clickedPosition);
            iv_imgToView.setImageURI(imageUriList.get(clickedPosition));
        }

    }
}
