package com.application.lumaque.bizlinked.fragments.bizlinked;

import com.application.lumaque.bizlinked.data_models.bizlinked.CompanyHeadModel;

public interface CustomRecyclerCallBacks {


    void onImageClick(CompanyHeadModel companyHeadModel);

    void onListClick(CompanyHeadModel companyHeadModel);

    void onActionClick(CompanyHeadModel companyHeadModel, String tag);

}
