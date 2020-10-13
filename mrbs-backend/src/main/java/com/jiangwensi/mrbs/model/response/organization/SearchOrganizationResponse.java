package com.jiangwensi.mrbs.model.response.organization;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 24/8/2020
 */
public class SearchOrganizationResponse extends GeneralResponse {
    List<SearchOrganizationResponseItem> organizations = new ArrayList<>();

    public List<SearchOrganizationResponseItem> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<SearchOrganizationResponseItem> organizations) {
        this.organizations = organizations;
    }
}