package bing.model.search.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class BingWebResponseData<T> {

    List<T> results;
    String next;


    @JsonProperty("results")
    public void setResults(List<T> results) {
        this.results = results;
    }

    @JsonProperty("__next")
    public void setNext(String next) {
        this.next = next;
    }
}
