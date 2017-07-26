package com.workshop.service;

import com.workshop.model.User;

public interface SearchResultsService {
    void saveResults(int resultsId, String resultsJson);
    int getNextResultsId(User user);
}
