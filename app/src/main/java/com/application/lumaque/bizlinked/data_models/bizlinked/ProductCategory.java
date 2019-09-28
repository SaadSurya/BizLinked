package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductCategory implements Serializable {

    public int CompanyID;
    public int ProductCategoryID;
    public String ProductCategoryName;
    public ArrayList<ProductCategory> SubProductCategories;
    public String ImageID;
    public int ParentProductCategoryID;

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public int getProductCategoryID() {
        return ProductCategoryID;
    }

    public void setProductCategoryID(int productCategoryID) {
        ProductCategoryID = productCategoryID;
    }

    public String getProductCategoryName() {
        return ProductCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        ProductCategoryName = productCategoryName;
    }


    public int getParentProductCategoryID() {
        return ParentProductCategoryID;
    }

    public void setParentProductCategoryID(int parentProductCategoryID) {
        ParentProductCategoryID = parentProductCategoryID;
    }

    public ArrayList<ProductCategory> getSubProductCategories() {
        return SubProductCategories;
    }

    public void setSubProductCategories(ArrayList<ProductCategory> SubProductCategories) {
        this.SubProductCategories = SubProductCategories;
    }

}
