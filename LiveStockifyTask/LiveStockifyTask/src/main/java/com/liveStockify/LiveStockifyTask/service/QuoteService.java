package com.liveStockify.LiveStockifyTask.service;

import com.liveStockify.LiveStockifyTask.model.Quote;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class QuoteService {
    
    private final List<String> quotes = Arrays.asList(
        "The only way to do great work is to love what you do. - Steve Jobs",
        "Innovation distinguishes between a leader and a follower. - Steve Jobs",
        "Life is what happens to you while you're busy making other plans. - John Lennon",
        "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt",
        "It is during our darkest moments that we must focus to see the light. - Aristotle",
        "The way to get started is to quit talking and begin doing. - Walt Disney",
        "Don't be pushed around by the fears in your mind. Be led by the dreams in your heart. - Roy T. Bennett",
        "Believe you can and you're halfway there. - Theodore Roosevelt",
        "The only impossible journey is the one you never begin. - Tony Robbins",
        "In the middle of difficulty lies opportunity. - Albert Einstein",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston Churchill",
        "The way to get started is to quit talking and begin doing. - Walt Disney",
        "Don't let yesterday take up too much of today. - Will Rogers",
        "You learn more from failure than from success. Don't let it stop you. Failure builds character. - Unknown",
        "If you are working on something that you really care about, you don't have to be pushed. The vision pulls you. - Steve Jobs",
        "People who are crazy enough to think they can change the world, are the ones who do. - Rob Siltanen",
        "We may encounter many defeats but we must not be defeated. - Maya Angelou",
        "The only person you are destined to become is the person you decide to be. - Ralph Waldo Emerson",
        "Go confidently in the direction of your dreams. Live the life you have imagined. - Henry David Thoreau",
        "When you have a dream, you've got to grab it and never let go. - Carol Burnett"
    );
    
    private final Random random = new Random();
    
    public Quote getRandomQuote() {
        String randomQuote = quotes.get(random.nextInt(quotes.size()));
        return new Quote(randomQuote);
    }
}
