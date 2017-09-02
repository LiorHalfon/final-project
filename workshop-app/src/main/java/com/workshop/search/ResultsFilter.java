package com.workshop.search;

import com.aylien.textapi.responses.TaxonomyCategory;
import com.aylien.textapi.responses.TaxonomyClassifications;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResultsFilter {

    private final List<String> whiteListCategories;
    private final List<String> blackListCategories;
    private final List<String> blackListDomains;

    // whiteListCategories and blackListCategories are lists of IAB Categories
    public ResultsFilter(List<String> whiteListCategories,
                         List<String> blackListCategories,
                         List<String> blackListDomains) {
        this.whiteListCategories = whiteListCategories == null ? new ArrayList<>() : whiteListCategories;
        this.blackListCategories = blackListCategories == null ? new ArrayList<>() : blackListCategories;
        this.blackListDomains = blackListDomains == null ? new ArrayList<>() : blackListDomains;
    }

    public boolean isUrlRelevant(URL url, TaxonomyClassifications taxonomyClassifications) {
        for (String blackDomain : blackListDomains)
            if(url.toString().toLowerCase().contains(blackDomain.toLowerCase()))
                return false;

        boolean isAnyWLCategoryAppeared = false;
        for (TaxonomyCategory c: taxonomyClassifications.getCategories()) {
            if(whiteListCategories.contains(c.getId()))
                isAnyWLCategoryAppeared = true;

            if (c.isConfident() && blackListCategories.contains(c.getId())) {
                return false;
            }
        }

        return isAnyWLCategoryAppeared;
    }
}
