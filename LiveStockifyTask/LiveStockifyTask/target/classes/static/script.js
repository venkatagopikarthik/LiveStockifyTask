// Quote API Configuration
const API_BASE_URL = 'http://localhost:8081/api';
const QUOTE_ENDPOINT = `${API_BASE_URL}/quote`;

// Global state
let quoteCount = 0;
let currentQuote = '';
let currentAuthor = '';
let countdownInterval = null;

// DOM Elements
const quoteCard = document.getElementById('quoteCard');
const quoteContent = document.getElementById('quoteContent');
const quoteText = document.getElementById('quoteText');
const quoteAuthor = document.getElementById('quoteAuthor');
const loading = document.getElementById('loading');
const errorMessage = document.getElementById('errorMessage');
const errorText = document.getElementById('errorText');
const getQuoteBtn = document.getElementById('getQuoteBtn');
const shareBtn = document.getElementById('shareBtn');
const rateLimitInfo = document.getElementById('rateLimitInfo');
const rateLimitText = document.getElementById('rateLimitText');
const countdownTime = document.getElementById('countdownTime');
const quoteCountElement = document.getElementById('quoteCount');

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    updateQuoteCount();
    // Load initial quote
    getQuote();
});

// Main function to fetch a quote
async function getQuote() {
    try {
        // Show loading state
        showLoading();
        
        // Disable button during request
        getQuoteBtn.disabled = true;
        getQuoteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Loading...';
        
        // Make API request
        const response = await fetch(QUOTE_ENDPOINT, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        
        const data = await response.json();
        
        if (response.ok) {
            // Success - display quote
            displayQuote(data.quote);
            quoteCount++;
            updateQuoteCount();
            hideError();
            hideRateLimit();
        } else if (response.status === 429) {
            // Rate limit exceeded
            handleRateLimit(data.error);
        } else {
            // Other error
            throw new Error(data.error || 'Failed to fetch quote');
        }
        
    } catch (error) {
        console.error('Error fetching quote:', error);
        showError('Failed to connect to the server. Please check if the API is running.');
    } finally {
        // Reset button state
        getQuoteBtn.disabled = false;
        getQuoteBtn.innerHTML = '<i class="fas fa-magic"></i> Get New Quote';
        hideLoading();
    }
}

// Display the quote
function displayQuote(quote) {
    // Parse quote and author
    const parts = quote.split(' - ');
    currentQuote = parts[0].trim();
    currentAuthor = parts.length > 1 ? parts[1].trim() : '';
    
    // Update DOM
    quoteText.textContent = currentQuote;
    quoteAuthor.textContent = currentAuthor ? `— ${currentAuthor}` : '';
    
    // Show content and share button
    quoteContent.style.display = 'block';
    shareBtn.style.display = 'flex';
    
    // Add fade-in animation
    quoteCard.classList.add('fade-in');
    setTimeout(() => {
        quoteCard.classList.remove('fade-in');
    }, 500);
}

// Show loading state
function showLoading() {
    loading.style.display = 'block';
    quoteContent.style.display = 'none';
    shareBtn.style.display = 'none';
    hideError();
    hideRateLimit();
}

// Hide loading state
function hideLoading() {
    loading.style.display = 'none';
}

// Show error message
function showError(message) {
    errorText.textContent = message;
    errorMessage.style.display = 'block';
    quoteContent.style.display = 'none';
    shareBtn.style.display = 'none';
    hideRateLimit();
}

// Hide error message
function hideError() {
    errorMessage.style.display = 'none';
}

// Handle rate limit
function handleRateLimit(errorMessage) {
    // Extract time from error message
    const timeMatch = errorMessage.match(/(\d+)\s+seconds/);
    const waitTime = timeMatch ? parseInt(timeMatch[1]) : 60;
    
    rateLimitText.textContent = errorMessage;
    rateLimitInfo.style.display = 'block';
    quoteContent.style.display = 'none';
    shareBtn.style.display = 'none';
    hideError();
    
    // Start countdown
    startCountdown(waitTime);
}

// Hide rate limit info
function hideRateLimit() {
    rateLimitInfo.style.display = 'none';
    if (countdownInterval) {
        clearInterval(countdownInterval);
        countdownInterval = null;
    }
}

// Start countdown timer
function startCountdown(seconds) {
    let timeLeft = seconds;
    countdownTime.textContent = timeLeft;
    
    countdownInterval = setInterval(() => {
        timeLeft--;
        countdownTime.textContent = timeLeft;
        
        if (timeLeft <= 0) {
            clearInterval(countdownInterval);
            countdownInterval = null;
            hideRateLimit();
        }
    }, 1000);
}

// Update quote count display
function updateQuoteCount() {
    quoteCountElement.textContent = quoteCount;
}

// Share quote functionality
function shareQuote() {
    if (!currentQuote) return;
    
    const shareText = `"${currentQuote}"${currentAuthor ? ` - ${currentAuthor}` : ''}`;
    
    if (navigator.share) {
        // Use native sharing if available
        navigator.share({
            title: 'Inspirational Quote',
            text: shareText,
            url: window.location.href
        }).catch(err => {
            console.log('Error sharing:', err);
            fallbackShare(shareText);
        });
    } else {
        // Fallback to clipboard
        fallbackShare(shareText);
    }
}

// Fallback sharing method
function fallbackShare(text) {
    if (navigator.clipboard) {
        navigator.clipboard.writeText(text).then(() => {
            showTemporaryMessage('Quote copied to clipboard!');
        }).catch(err => {
            console.error('Failed to copy to clipboard:', err);
            showTemporaryMessage('Failed to copy quote');
        });
    } else {
        // Last resort - show in alert
        alert(`Share this quote:\n\n${text}`);
    }
}

// Show temporary message
function showTemporaryMessage(message) {
    // Create temporary message element
    const messageEl = document.createElement('div');
    messageEl.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: #4CAF50;
        color: white;
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        z-index: 1000;
        font-weight: 500;
        animation: slideIn 0.3s ease;
    `;
    messageEl.textContent = message;
    
    document.body.appendChild(messageEl);
    
    // Remove after 3 seconds
    setTimeout(() => {
        messageEl.style.animation = 'slideOut 0.3s ease';
        setTimeout(() => {
            document.body.removeChild(messageEl);
        }, 300);
    }, 3000);
}

// Add CSS for slideOut animation
const style = document.createElement('style');
style.textContent = `
    @keyframes slideOut {
        from {
            opacity: 1;
            transform: translateX(0);
        }
        to {
            opacity: 0;
            transform: translateX(100%);
        }
    }
`;
document.head.appendChild(style);

// Keyboard shortcuts
document.addEventListener('keydown', function(event) {
    // Space bar or Enter to get new quote
    if (event.code === 'Space' || event.code === 'Enter') {
        event.preventDefault();
        if (!getQuoteBtn.disabled) {
            getQuote();
        }
    }
    
    // 'S' key to share quote
    if (event.code === 'KeyS' && currentQuote) {
        event.preventDefault();
        shareQuote();
    }
});

// Add some fun interactions
quoteCard.addEventListener('mouseenter', function() {
    this.style.transform = 'translateY(-5px) scale(1.02)';
});

quoteCard.addEventListener('mouseleave', function() {
    this.style.transform = 'translateY(0) scale(1)';
});

// Auto-refresh quote every 30 seconds (optional)
let autoRefreshInterval = null;

function startAutoRefresh() {
    autoRefreshInterval = setInterval(() => {
        if (!getQuoteBtn.disabled) {
            getQuote();
        }
    }, 30000); // 30 seconds
}

function stopAutoRefresh() {
    if (autoRefreshInterval) {
        clearInterval(autoRefreshInterval);
        autoRefreshInterval = null;
    }
}

// Start auto-refresh after 10 seconds of inactivity
let inactivityTimer = setTimeout(startAutoRefresh, 10000);

// Reset inactivity timer on user interaction
document.addEventListener('click', () => {
    clearTimeout(inactivityTimer);
    stopAutoRefresh();
    inactivityTimer = setTimeout(startAutoRefresh, 10000);
});

document.addEventListener('keydown', () => {
    clearTimeout(inactivityTimer);
    stopAutoRefresh();
    inactivityTimer = setTimeout(startAutoRefresh, 10000);
});
