package com.liveStockify.LiveStockifyTask.service;

import com.liveStockify.LiveStockifyTask.model.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QuoteServiceTest {
    
    private QuoteService quoteService;
    
    @BeforeEach
    void setUp() {
        quoteService = new QuoteService();
    }
    
    @Test
    void testGetRandomQuoteReturnsValidQuote() {
        Quote quote = quoteService.getRandomQuote();
        
        assertNotNull(quote, "Quote should not be null");
        assertNotNull(quote.getQuote(), "Quote text should not be null");
        assertFalse(quote.getQuote().trim().isEmpty(), "Quote text should not be empty");
    }
    
    @Test
    void testGetRandomQuoteReturnsDifferentQuotes() {
        Quote quote1 = quoteService.getRandomQuote();
        Quote quote2 = quoteService.getRandomQuote();
        Quote quote3 = quoteService.getRandomQuote();
        
        // With 20 quotes, it's very likely to get different quotes
        // This test might occasionally fail due to randomness, but it's acceptable
        boolean hasDifferentQuotes = !quote1.getQuote().equals(quote2.getQuote()) ||
                                   !quote2.getQuote().equals(quote3.getQuote()) ||
                                   !quote1.getQuote().equals(quote3.getQuote());
        
        assertTrue(hasDifferentQuotes, "Should return different quotes (with high probability)");
    }
    
    @Test
    void testQuoteContainsExpectedFormat() {
        Quote quote = quoteService.getRandomQuote();
        String quoteText = quote.getQuote();
        
        // Check if quote contains a dash (author separator)
        assertTrue(quoteText.contains(" - "), "Quote should contain author separator ' - '");
        
        // Check if quote has reasonable length
        assertTrue(quoteText.length() > 10, "Quote should have reasonable length");
        assertTrue(quoteText.length() < 500, "Quote should not be excessively long");
    }
}
