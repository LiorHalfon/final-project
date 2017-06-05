import com.aylien.textapi.responses.TaxonomyCategory;
import com.aylien.textapi.responses.TaxonomyClassifications;

import java.util.ArrayList;

public class ResultsFilter {

    private final ArrayList<String> haveToAppearCategories;
    private final ArrayList<String> cantAppearCategories;

    // haveToAppearCategories and cantAppearCategories are lists of IAB Categories
    public ResultsFilter(ArrayList<String> haveToAppearCategories,
                         ArrayList<String> cantAppearCategories) {
        this.haveToAppearCategories = haveToAppearCategories == null ? new ArrayList<>() :haveToAppearCategories;
        this.cantAppearCategories = cantAppearCategories == null ? new ArrayList<>() :cantAppearCategories;
    }

    public boolean isUrlRelevant(TaxonomyClassifications taxonomyClassifications)
    {
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