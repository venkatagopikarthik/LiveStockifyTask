package com.liveStockify.LiveStockifyTask.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote {
    @JsonProperty("quote")
    private String quote;
    
    public Quote() {}
    
    public Quote(String quote) {
        this.quote = quote;
    }
    
    public String getQuote() {
        return quote;
    }
    
    public void setQuote(String quote) {
        this.quote = quote;
    }
}
