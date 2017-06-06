package search;

import model.User;
import java.util.ArrayList;
import java.util.List;

public interface NewsFinder {

    void Init(ArrayList<String> haveToAppearCategories,
              ArrayList<String> cantAppearCategories,
              ArrayList<String> blacklistDomains,
              ArrayList<String> queries,
              User user);
    void Start() throws Exception;

    List<RelevantNews> GetResults();
    Boolean IsSearchFinished();
    String PrintResults();
}
