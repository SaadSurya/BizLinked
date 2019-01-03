package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class ProductCategory implements Serializable {

    public int CompanyID;
    public int ProductCategoryID ;
    public String ProductCategoryName ;

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String ImageID ;
    public int  ParentProductCategoryID ;

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
}
