package com.liveStockify.LiveStockifyTask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RateLimitingServiceTest {
    
    private RateLimitingService rateLimitingService;
    
    @BeforeEach
    void setUp() {
        rateLimitingService = new RateLimitingService();
    }
    
    @Test
    void testRateLimitAllowsFirstFiveRequests() {
        String clientIp = "192.168.1.1";
        
        // First 5 requests should be allowed
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitingService.isAllowed(clientIp), 
                "Request " + (i + 1) + " should be allowed");
        }
    }
    
    @Test
    void testRateLimitBlocksSixthRequest() {
        String clientIp = "192.168.1.2";
        
        // First 5 requests should be allowed
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitingService.isAllowed(clientIp));
        }
        
        // 6th request should be blocked
        assertFalse(rateLimitingService.isAllowed(clientIp), 
            "6th request should be blocked");
    }
    
    @Test
    void testDifferentClientsHaveSeparateLimits() {
        String clientIp1 = "192.168.1.3";
        String clientIp2 = "192.168.1.4";
        
        // Both clients should be able to make 5 requests each
        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimitingService.isAllowed(clientIp1));
            assertTrue(rateLimitingService.isAllowed(clientIp2));
        }
        
        // Both should be blocked on 6th request
        assertFalse(rateLimitingService.isAllowed(clientIp1));
        assertFalse(rateLimitingService.isAllowed(clientIp2));
    }
    
    @Test
    void testTimeUntilResetReturnsValidValue() {
        String clientIp = "192.168.1.5";
        
        // Make some requests
        rateLimitingService.isAllowed(clientIp);
        rateLimitingService.isAllowed(clientIp);
        
        long timeUntilReset = rateLimitingService.getTimeUntilReset(clientIp);
        assertTrue(timeUntilReset >= 0 && timeUntilReset <= 60, 
            "Time until reset should be between 0 and 60 seconds");
    }
}
