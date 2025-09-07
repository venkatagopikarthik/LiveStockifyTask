package com.liveStockify.LiveStockifyTask.controller;

import com.liveStockify.LiveStockifyTask.model.ErrorResponse;
import com.liveStockify.LiveStockifyTask.model.Quote;
import com.liveStockify.LiveStockifyTask.service.QuoteService;
import com.liveStockify.LiveStockifyTask.service.RateLimitingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class QuoteController {
    
    @Autowired
    private QuoteService quoteService;
    
    @Autowired
    private RateLimitingService rateLimitingService;
    
    @GetMapping("/quote")
    public ResponseEntity<?> getQuote(HttpServletRequest request) {
        String clientIp = getClientIpAddress(request);
        
        if (!rateLimitingService.isAllowed(clientIp)) {
            long timeUntilReset = rateLimitingService.getTimeUntilReset(clientIp);
            ErrorResponse errorResponse = new ErrorResponse(
                "Rate limit exceeded. Try again in " + timeUntilReset + " seconds."
            );
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }
        
        Quote quote = quoteService.getRandomQuote();
        return ResponseEntity.ok(quote);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}
