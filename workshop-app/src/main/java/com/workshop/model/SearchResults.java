package com.workshop.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "search_results")
public class SearchResults {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "results_id")
    private int resultsId;

    @Column(name = "search_start_time")
    private Timestamp searchStartTime;

    @Column(name = "search_end_time")
    private Timestamp searchEndTime;

    @Column(name = "results")
    private String results;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER"))
    private User user;

    public int getResultsId() {
        return resultsId;
    }

    public void setResultsId(int resultsId) {
        this.resultsId = resultsId;
    }

    public Timestamp getSearchStartTime() {
        return searchStartTime;
    }

    public void setSearchStartTime(Timestamp searchStartTime) {
        this.searchStartTime = searchStartTime;
    }

    public Timestamp getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(Timestamp searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
