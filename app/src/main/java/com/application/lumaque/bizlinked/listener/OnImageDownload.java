package com.application.lumaque.bizlinked.listener;

import android.view.View;

public interface OnImageDownload {
    void onImageDownload(View currentView,String filePath);
    void onImageError(View currentView);
}
