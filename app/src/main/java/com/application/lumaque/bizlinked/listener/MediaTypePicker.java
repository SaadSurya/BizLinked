package com.application.lumaque.bizlinked.listener;

import java.io.File;
import java.util.ArrayList;


public interface MediaTypePicker {
    void onPhotoClicked(ArrayList<File> file);

    void onDocClicked(ArrayList<File> files);
}
