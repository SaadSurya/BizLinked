package com.application.lumaque.bizlinked.constant;


public class AppConstant {

    public static final int HOME_ITEM_COUNT = 3;

    public static final int SELECT_IMAGE_COUNT = 1;
    public static final int SELECT_MAX_FILE_COUNT = 10;
    public static final int SELECT_DOC_FILE_COUNT = 1;
    public static final int SELECT_MAX_DOC_FILE_COUNT = 10;
    public static final int GOOGLE_PLACE_CODE = 501;


    public class RESOURCE_FOLDER {
        public static final String DRAWABLES = "drawable";
        public static final String RAW = "raw";
    }

    public class TRANSITION_TYPES {
        public static final int FADE = 20001;
        public static final int SLIDE = 20002;
    }

    public class VALIDATION_RULES {
        public static final int PASSWORD_MIN_LENGTH = 3;
        public static final int USER_NAME_MIN_LENGTH = 3;
        public static final int NAME_MIN_LENGTH = 3;
    }

    public class TOAST_TYPES {
        public static final int INFO = 1101;
        public static final int SUCCESS = 1102;
        public static final int ERROR = 1103;

    }

    public class SCAN_DOCUMENT_TYPES {
        public static final String IMAGE_0 = "image_0";

    }


    public class ServerAPICalls {


        public class HTTP_VERBS {
            public static final int GET = 5400;
            public static final int POST = 5401;
        }

        public class IMAGE_UPLOAD_STATUS {
            public static final String FROM_SERVER = "server";
            public static final String FROM_GALLERY = "server";
        }

        private static final String BASE_URL = "http://api.bizlinked.lumaque.pk/rest/";


        private static final String COMPANY = "Company/";
        private static final String PARAMETER = "Parameter/";
        private static final String LINK = "Link/";
//For API Calls

        public static final String LOGIN_URL = BASE_URL + "Account/Login";
        public static final String REGISTER_URL = BASE_URL + "Account/Register";


        public static final String SEND_CUSTOMER_REQ_URL = BASE_URL + LINK +"SendCustomerRequest";
        public static final String SEND_SUPPLIER_REQ_URL = BASE_URL + LINK +"SendSupplierRequest";

        public static final String Accept_REQ_URL = BASE_URL + LINK +"AcceptRequest";
        public static final String CANCEL_REQ_URL = BASE_URL + LINK +"CancelRequest";
        public static final String UNLINKED_REQ_URL = BASE_URL + LINK +"Unlink";
        public static final String IGNORE_REQ_URL = BASE_URL + LINK +"IgnoreRequest";


        public static final String LINKED_CUSTOMER_URL = BASE_URL + LINK +"GetCustomerLinks";
        public static final String LINKED_SUPPLIER_URL = BASE_URL + LINK +"GetSupplierLinks";
        public static final String PRODUCT_CATEGORIES_URL = BASE_URL + PARAMETER +"GetProductMajorCategories";
        public static final String BUSINESS_NATURE_URL = BASE_URL + PARAMETER +"GetBusinessNatures";
        public static final String CITIES_URL = BASE_URL + PARAMETER +"GetCities";


        public static final String SAVE_COMPANY_PROFILE = BASE_URL + COMPANY +"SaveCompanyProfile";
        public static final String DELETE_COMPANY_PROFILE_PIC = BASE_URL + COMPANY +"RemoveCompanyLogo";
        public static final String GET_COMPANYPROFILE = BASE_URL + COMPANY +"GetCompanyProfile";

        public static final String UPLOAD_FILE_IMAGE = BASE_URL + COMPANY +"SaveCompanyLogo";

        public static final String SEARCH_COMPANY = BASE_URL + COMPANY +"SearchCompany";

        public static final String GET_MEDIA_FILE = BASE_URL + COMPANY +"Logo?companyId=";



        public static final String DELETE_MEDIA = BASE_URL + "files";

    }

}
