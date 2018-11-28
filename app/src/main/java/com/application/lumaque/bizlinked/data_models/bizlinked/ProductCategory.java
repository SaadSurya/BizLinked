package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class ProductCategory implements Serializable {

    public long CompanyID;
    public long ProductCategoryID ;
    public String ProductCategoryName ;
    public String Images ;
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

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public long getParentProductCategoryID() {
        return ParentProductCategoryID;
    }

    public void setParentProductCategoryID(long parentProductCategoryID) {
        ParentProductCategoryID = parentProductCategoryID;
    }
}
