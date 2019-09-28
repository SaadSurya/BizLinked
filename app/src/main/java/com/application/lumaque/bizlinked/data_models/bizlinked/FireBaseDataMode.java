package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.util.Map;

public class FireBaseDataMode {


    String screen;
    String text;
    Map<String, Object> data;

    public FireBaseDataMode() {
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

   /* public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
*/
}
