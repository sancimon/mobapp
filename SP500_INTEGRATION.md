# S&P 500 Stock Price Integration

## Overview
Added real-time S&P 500 stock price display to the Monthly Report page using Alpha Vantage API.

## What Was Added

### 1. Network Permissions
- `AndroidManifest.xml`: Added INTERNET and ACCESS_NETWORK_STATE permissions

### 2. Dependencies (build.gradle)
- Retrofit 2.9.0 - REST API client
- OkHttp 4.11.0 - HTTP client
- Gson 2.10.1 - JSON parsing

### 3. New Files Created

#### Data Models
- `data_models/StockPrice.kt` - Stock price data model
- Contains AlphaVantageResponse and GlobalQuote models for API parsing

#### API Service
- `api/StockApiService.kt` - Retrofit interface for Alpha Vantage API
- Endpoint: `https://www.alphavantage.co/query`

#### Repository
- `repositories/StockRepository.kt` - Handles API calls and data fetching
- Uses SPY (S&P 500 ETF) as proxy for S&P 500 index

### 4. UI Changes
- `composables/pages/MonthlyReportPage.kt`
  - Added S&P 500 price display above the BACK button
  - Shows: symbol, current price, change amount, change percentage, last updated date
  - Displays loading indicator while fetching
  - Shows error message if API call fails
  - Color-coded: green for positive change, red for negative

## API Configuration

### Alpha Vantage API
- **Base URL**: https://www.alphavantage.co/
- **Current API Key**: "demo" (limited to demo data)
- **Symbol Used**: SPY (SPDR S&P 500 ETF Trust)

### Getting Your Own API Key (FREE)
1. Visit: https://www.alphavantage.co/support/#api-key
2. Sign up for a free account
3. Get your API key
4. Replace "demo" in `StockRepository.kt` line 17:
   ```kotlin
   private const val API_KEY = "YOUR_API_KEY_HERE"
   ```

### API Limits (Free Tier)
- 5 API calls per minute
- 500 API calls per day

## GitHub Actions

### Updated Workflow (.github/workflows/build-apk.yml)
- Added Gradle caching for faster builds
- Added `--no-daemon` flag for CI efficiency
- Set APK retention to 30 days

### Testing the Build
Push your changes to trigger the workflow:
```bash
git add .
git commit -m "Add S&P 500 stock price integration"
git push origin master
```

The workflow will:
1. Build the APK with new networking dependencies
2. Upload the APK as an artifact
3. You can download it from the Actions tab on GitHub

## How It Works

1. When user opens Monthly Report page
2. LaunchedEffect triggers on page load
3. StockRepository fetches data from Alpha Vantage API
4. Response is parsed into StockPrice model
5. UI updates to show:
   - Loading indicator (while fetching)
   - Stock data (on success)
   - Error message (on failure)

## UI Layout

```
┌─────────────────────────────┐
│     Monthly Report          │
│                             │
│  [Expense Overview]         │
│  [Category Breakdown]       │
│                             │
│  ┌───────────────────────┐  │
│  │   SPY (S&P 500)       │  │
│  │   $450.23             │  │
│  │   +2.45 (+0.55%)      │  │
│  │   Last updated: ...   │  │
│  └───────────────────────┘  │
│                             │
│      [  BACK  ]             │
└─────────────────────────────┘
```

## Testing Locally

1. Build and run the app:
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

2. Navigate to: Add Expense → Daily Expenses → Monthly Expenses → Monthly Report

3. The S&P 500 price should load automatically

## Troubleshooting

### "Failed to load stock price"
- Check internet connection
- Verify API key is valid
- Check if you've exceeded rate limits (5 calls/min, 500/day)

### Build Errors
- Run `./gradlew clean build`
- Sync Gradle files in Android Studio
- Check that all dependencies downloaded correctly

### API Returns Empty Data
- Alpha Vantage demo key only works with specific symbols
- Get a real API key from alphavantage.co

## Future Enhancements

Possible improvements:
- Add refresh button to manually reload stock price
- Cache price data to reduce API calls
- Add multiple stock/index options
- Display historical price chart
- Show pre-market/after-hours data
