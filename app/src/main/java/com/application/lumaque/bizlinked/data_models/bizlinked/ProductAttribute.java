package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class ProductAttribute implements Serializable {

    public long AttributeID ;
    public String AttributeName ;
    public String [] ProductAttributeValueName ;

    public long getAttributeID() {
        return AttributeID;
    }

    public void setAttributeID(long attributeID) {
        AttributeID = attributeID;
    }

    public String getAttributeName() {
        return AttributeName;
    }

    public void setAttributeName(String attributeName) {
        AttributeName = attributeName;
    }

    public String[] getProductAttributeValueName() {
        return ProductAttributeValueName;
    }

    public void setProductAttributeValueName(String[] productAttributeValueName) {
        ProductAttributeValueName = productAttributeValueName;
    }
}
