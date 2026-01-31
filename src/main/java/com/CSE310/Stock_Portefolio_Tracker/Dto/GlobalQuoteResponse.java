package com.CSE310.Stock_Portefolio_Tracker.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GlobalQuoteResponse {

    @JsonProperty("Global Quote")
    private Quote quote;

    @Data
    public static class Quote {
        @JsonProperty("01. symbol")
        private String symbol;

        @JsonProperty("05. price")
        private String price;

        @JsonProperty("10. change percent")
        private String changePercent;
    }

}
