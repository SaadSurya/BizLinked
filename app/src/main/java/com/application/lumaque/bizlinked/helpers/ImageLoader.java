package com.application.lumaque.bizlinked.helpers;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.application.lumaque.bizlinked.constant.AppConstant;
import com.application.lumaque.bizlinked.helpers.common.Utils;
import com.application.lumaque.bizlinked.helpers.preference.BasePreferenceHelper;
import com.application.lumaque.bizlinked.listener.OnImageDownload;
import com.application.lumaque.bizlinked.webhelpers.WebAppManager;

import java.util.HashMap;

public class ImageLoader {

    private int runnableIndex;
    private Handler handler = new Handler();
    private Runnable runnable;

    private FrameLayout imagesArray;
    private Activity activity;
    private BasePreferenceHelper preferenceHelper;


    public ImageLoader(Activity activity, FrameLayout imagesArray, BasePreferenceHelper preferenceHelper) {
        this.runnableIndex = 0;
        this.imagesArray = imagesArray;
        this.imagesArray = imagesArray;
        this.activity = activity;
        this.preferenceHelper = preferenceHelper;
    }


    public void loadScanDocumentImages(final boolean isProgressShow, String URL, final OnImageDownload onImageDownload) {
        getMediaFile(isProgressShow,
                imagesArray, null, onImageDownload, URL
        );
    }

    public void loadSingleImage(boolean isProgressShow, View view, final OnImageDownload onImageDownload, String URL) {


        getMediaFile(isProgressShow,
                view, null, onImageDownload, URL
        );
    }

    public void getMediaFile(boolean isProgressShow, final View currentView, HashMap<String, String> extraParams
            , final OnImageDownload imageDownload, String URL
    ) {


        WebAppManager.getInstance(activity, preferenceHelper).getMediaFile(isProgressShow,
                URL,
                AppConstant.ServerAPICalls.HTTP_VERBS.GET,
                extraParams,
                new WebAppManager.APIStringRequestDataCallBack() {
                    @Override
                    public void onSuccess(String filePath) {
                        imageDownload.onImageDownload(currentView, filePath);
                        Utils.deleteFileForPath(filePath);
                    }

                    @Override
                    public void onError(String response) {
                        imageDownload.onImageError(currentView);
                    }

                    @Override
                    public void onNoNetwork() {
                    }
                });
    }


}
