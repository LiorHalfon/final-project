package search;

import com.aylien.textapi.responses.TaxonomyCategory;
import com.aylien.textapi.responses.TaxonomyClassifications;

import java.net.URL;
import java.util.ArrayList;

public class ResultsFilter {

    private final ArrayList<String> haveToAppearCategories;
    private final ArrayList<String> cantAppearCategories;
    private final ArrayList<String> blacklistDomains;

    // haveToAppearCategories and cantAppearCategories are lists of IAB Categories
    public ResultsFilter(ArrayList<String> haveToAppearCategories,
                         ArrayList<String> cantAppearCategories,
                         ArrayList<String> blacklistDomains) {
        this.haveToAppearCategories = haveToAppearCategories == null ? new ArrayList<>() :haveToAppearCategories;
        this.cantAppearCategories = cantAppearCategories == null ? new ArrayList<>() :cantAppearCategories;
        this.blacklistDomains = blacklistDomains == null ? new ArrayList<>() :blacklistDomains;
    }

    public boolean isUrlRelevant(URL url, TaxonomyClassifications taxonomyClassifications)
    {
        for (String blackDomain : blacklistDomains)
            if(url.toString().toLowerCase().contains(blackDomain.toLowerCase()))
                return false;

        ArrayList categoriesLeftToAppear = (ArrayList)haveToAppearCategories.clone();

        for (TaxonomyCategory c: taxonomyClassifications.getCategories()) {
            categoriesLeftToAppear.remove(c.getId());
            if (c.isConfident() && cantAppearCategories.contains(c.getId())) {
                return false;
            }
        }

        if (categoriesLeftToAppear.size() == 0)
            return true;

        return false;
    }
}
