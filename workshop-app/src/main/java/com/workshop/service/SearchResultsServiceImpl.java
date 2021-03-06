package com.workshop.service;

import com.workshop.model.SearchResults;
import com.workshop.model.User;
import com.workshop.repository.SearchResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service("searchResultsService")
public class SearchResultsServiceImpl implements SearchResultsService {

    @Autowired
    private SearchResultsRepository searchResultsRepository;

    @Override
    public void saveResults(int resultsId, String resultsJson) {
        SearchResults searchResults = getResultsByResultsId(resultsId);
        searchResults.setResults(resultsJson);
        searchResults.setSearchEndTime(new Timestamp(new Date().getTime()));
        searchResultsRepository.save(searchResults);
    }

    @Override
    public int getNextResultsId(User user) {
        SearchResults searchResults = new SearchResults();
        searchResults.setUser(user);
        searchResults.setSearchStartTime(new Timestamp(new Date().getTime()));

        SearchResults saved = searchResultsRepository.save(searchResults);
        return saved.getResultsId();
    }

    @Override
    public SearchResults getResultsByResultsId(int resultsId) {
        return searchResultsRepository.findByResultsId(resultsId);
    }
}
