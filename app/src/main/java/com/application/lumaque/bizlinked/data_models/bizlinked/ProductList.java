package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;
import java.util.List;

public class ProductList implements Serializable {


    List<ProductCategory> ProductCategory;
    List<Product> Product;

    public List<com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory> getProductCategory() {
        return ProductCategory;
    }

    public void setProductCategory(List<com.application.lumaque.bizlinked.data_models.bizlinked.ProductCategory> productCategory) {
        ProductCategory = productCategory;
    }

    public List<com.application.lumaque.bizlinked.data_models.bizlinked.Product> getProduct() {
        return Product;
    }

    public void setProduct(List<com.application.lumaque.bizlinked.data_models.bizlinked.Product> product) {
        Product = product;
    }
}
