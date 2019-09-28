package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;
import java.util.List;

public class SubProductCategory implements Serializable {

    int companyID;

    int productCategoryID;

    String productCategoryName;

    String imageID;

    int parentProductCategoryID;

    List<ProductCategory> SubProductCategories;

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getProductCategoryID() {
        return productCategoryID;
    }

    public void setProductCategoryID(int productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public int getParentProductCategoryID() {
        return parentProductCategoryID;
    }

    public void setParentProductCategoryID(int parentProductCategoryID) {
        this.parentProductCategoryID = parentProductCategoryID;
    }

    public List<ProductCategory> getSubProductCategories() {
        return SubProductCategories;
    }

    public void setSubProductCategories(List<ProductCategory> SubProductCategories) {
        this.SubProductCategories = SubProductCategories;
    }

}