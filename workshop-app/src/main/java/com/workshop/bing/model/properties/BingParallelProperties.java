package com.workshop.bing.model.properties;


import lombok.Data;

@Data
public class BingParallelProperties {
    int threads;
    String threadPrefix;
    int timeoutAmountInMilliseconds;
}
