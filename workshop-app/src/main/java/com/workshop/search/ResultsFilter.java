package com.workshop.search;

import com.aylien.textapi.responses.TaxonomyCategory;
import com.aylien.textapi.responses.TaxonomyClassifications;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ResultsFilter {

    private final List<String> haveToAppearCategories;
    private final List<String> cantAppearCategories;
    private final List<String> blacklistDomains;

    // haveToAppearCategories and cantAppearCategories are lists of IAB Categories
    public ResultsFilter(List<String> haveToAppearCategories,
                         List<String> cantAppearCategories,
                         List<String> blacklistDomains) {
        this.haveToAppearCategories = haveToAppearCategories == null ? new ArrayList<>() : haveToAppearCategories;
        this.cantAppearCategories = cantAppearCategories == null ? new ArrayList<>() :cantAppearCategories;
        this.blacklistDomains = blacklistDomains == null ? new ArrayList<>() :blacklistDomains;
    }

    public boolean isUrlRelevant(URL url, TaxonomyClassifications taxonomyClassifications) {
        for (String blackDomain : blacklistDomains)
            if(url.toString().toLowerCase().contains(blackDomain.toLowerCase()))
                return false;

        List<String> categoriesLeftToAppear = new ArrayList<>(haveToAppearCategories);

        for (TaxonomyCategory c: taxonomyClassifications.getCategories()) {
            categoriesLeftToAppear.remove(c.getId());
            if (c.isConfident() && cantAppearCategories.contains(c.getId())) {
                return false;
            }
        }

        return categoriesLeftToAppear.size() == 0;
    }
}
