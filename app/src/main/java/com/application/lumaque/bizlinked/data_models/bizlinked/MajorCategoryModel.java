package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class MajorCategoryModel implements Serializable {

    String MajorCategoryID;

    public String getMajorCategoryID() {
        return MajorCategoryID;
    }

    public void setMajorCategoryID(String majorCategoryID) {
        MajorCategoryID = majorCategoryID;
    }

    public String getMajorCategoryName() {
        return MajorCategoryName;
    }

    public void setMajorCategoryName(String majorCategoryName) {
        MajorCategoryName = majorCategoryName;
    }

    String MajorCategoryName;

}
