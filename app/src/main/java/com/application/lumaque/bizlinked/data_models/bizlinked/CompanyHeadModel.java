package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class CompanyHeadModel implements Serializable {


    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyLogo() {
        return CompanyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        CompanyLogo = companyLogo;
    }

    public String getLinkType() {
        return LinkType;
    }

    public void setLinkType(String linkType) {
        LinkType = linkType;
    }

    public String getLinkStatus() {
        return LinkStatus;
    }

    public void setLinkStatus(String linkStatus) {
        LinkStatus = linkStatus;
    }

    String CompanyID;
            String CompanyName;
            String CompanyLogo;
            String LinkType;
            String LinkStatus;

}
