package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {



    public int CompanyID ;
    public long ProductID ;
    public String ProductName ;
    public String ProductDescription ;
    public double Price ;
    public long ProductCategoryID ;
    public String ProductCategoryName ;
    public List<String> Images ;
    public List<ProductAttribute> ProductAttributes;

    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
        CompanyID = companyID;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
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

    public List<String> getImages() {
        return Images;
    }

    public void setImages(List<String> images) {
        Images = images;
    }

    public List<ProductAttribute> getProductAttributes() {
        return ProductAttributes;
    }

    public void setProductAttributes(List<ProductAttribute> productAttributes) {
        ProductAttributes = productAttributes;
    }
}
