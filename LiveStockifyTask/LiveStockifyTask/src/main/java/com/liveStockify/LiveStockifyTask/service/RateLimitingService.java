package com.liveStockify.LiveStockifyTask.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RateLimitingService {
    
    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute
    
    private final ConcurrentHashMap<String, RequestCounter> requestCounters = new ConcurrentHashMap<>();
    
    public boolean isAllowed(String clientIp) {
        long currentTime = System.currentTimeMillis();
        
        requestCounters.compute(clientIp, (ip, counter) -> {
            if (counter == null || isTimeWindowExpired(counter.getFirstRequestTime(), currentTime)) {
                return new RequestCounter(currentTime);
            }
            return counter;
        });
        
        RequestCounter counter = requestCounters.get(clientIp);
        int currentCount = counter.incrementAndGet();
        
        // Clean up expired entries periodically
        if (currentCount == 1) {
            cleanupExpiredEntries(currentTime);
        }
        
        return currentCount <= MAX_REQUESTS;
    }
    
    public long getTimeUntilReset(String clientIp) {
        RequestCounter counter = requestCounters.get(clientIp);
        if (counter == null) {
            return 0;
        }
        
        long timeUntilReset = TIME_WINDOW_MS - (System.currentTimeMillis() - counter.getFirstRequestTime());
        return Math.max(0, timeUntilReset / 1000); // Return seconds
    }
    
    private boolean isTimeWindowExpired(long firstRequestTime, long currentTime) {
        return (currentTime - firstRequestTime) >= TIME_WINDOW_MS;
    }
    
    private void cleanupExpiredEntries(long currentTime) {
        requestCounters.entrySet().removeIf(entry -> 
            isTimeWindowExpired(entry.getValue().getFirstRequestTime(), currentTime));
    }
    
    private static class RequestCounter {
        private final AtomicLong firstRequestTime;
        private final AtomicInteger count;
        
        public RequestCounter(long firstRequestTime) {
            this.firstRequestTime = new AtomicLong(firstRequestTime);
            this.count = new AtomicInteger(0);
        }
        
        public int incrementAndGet() {
            return count.incrementAndGet();
        }
        
        public long getFirstRequestTime() {
            return firstRequestTime.get();
        }
    }
}
