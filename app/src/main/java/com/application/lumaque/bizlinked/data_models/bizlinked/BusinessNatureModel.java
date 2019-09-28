package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class BusinessNatureModel implements Serializable {


    String BusinessNatureID;
    String BusinessNatureName;

    public String getBusinessNatureID() {
        return BusinessNatureID;
    }

    public void setBusinessNatureID(String businessNatureID) {
        BusinessNatureID = businessNatureID;
    }

    public String getBusinessNatureName() {
        return BusinessNatureName;
    }

    public void setBusinessNatureName(String businessNatureName) {
        BusinessNatureName = businessNatureName;
    }


}
