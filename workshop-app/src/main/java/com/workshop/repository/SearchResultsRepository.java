package com.workshop.repository;

import com.workshop.model.SearchResults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("searchResultsRepository")
public interface SearchResultsRepository extends JpaRepository<SearchResults, Long> {

}
