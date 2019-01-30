package com.application.lumaque.bizlinked.data_models.bizlinked;

import java.io.Serializable;

public class CompanyProfileModel implements Serializable {










    String LinkType;

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

    String      LinkStatus;
    int CompanyID;
    String CompanyName;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    String CityName;
    String CompanyLogo;
    //int ProductMajorCategoryID;
    String ContactNo;
    String PhoneNo;


    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo2) {
        PhoneNo = phoneNo2;
    }

   /* String PhoneNo2;*/

    public int[] getBusinessNature() {
        return BusinessNature;
    }

    public void setBusinessNature(int[] businessNature) {
        BusinessNature = businessNature;
    }

    int[] BusinessNature;

    String EmailAddress;
    String Website;
    String ShopNo;
    String Market;
    String Area;
    String CityID;
    double Latitude;
    double Longitude;






    public int getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(int companyID) {
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
/*

    public int getProductMajorCategoryID() {
        return ProductMajorCategoryID;
    }

    public void setProductMajorCategoryID(int productMajorCategoryID) {
        ProductMajorCategoryID = productMajorCategoryID;
    }
*/



    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }





    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getShopNo() {
        return ShopNo;
    }

    public void setShopNo(String shopNo) {
        ShopNo = shopNo;
    }

    public String getMarket() {
        return Market;
    }

    public void setMarket(String market) {
        Market = market;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getCityID() {
        return CityID;
    }

    public void setCityID(String cityID) {
        CityID = cityID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }












}
