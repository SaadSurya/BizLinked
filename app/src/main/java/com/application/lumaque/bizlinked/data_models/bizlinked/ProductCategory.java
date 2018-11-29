package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class ProductCategory implements Serializable {

    public long CompanyID;
    public long ProductCategoryID ;
    public String ProductCategoryName ;

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String ImageID ;
    public long  ParentProductCategoryID ;

    public long getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(long companyID) {
        CompanyID = companyID;
    }

    public long getProductCategoryID() {
        return ProductCategoryID;
    }

    public void setProductCategoryID(long productCategoryID) {
        ProductCategoryID = productCategoryID;
    }

    public String getProductCategoryName() {
        return ProductCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        ProductCategoryName = productCategoryName;
    }



    public long getParentProductCategoryID() {
        return ParentProductCategoryID;
    }

    public void setParentProductCategoryID(long parentProductCategoryID) {
        ParentProductCategoryID = parentProductCategoryID;
    }
}
