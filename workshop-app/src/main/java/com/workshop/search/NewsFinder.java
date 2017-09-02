package com.workshop.search;

import com.workshop.model.User;

import java.util.List;

public interface NewsFinder {

    void init(List<String> haveToAppearCategories,
              List<String> cantAppearCategories,
              List<String> blacklistDomains,
              List<String> queries,
              User user);
    void start() throws Exception;

    List<RelevantNews> getResults();
    Boolean isSearchFinished();
    String printResults();
}
