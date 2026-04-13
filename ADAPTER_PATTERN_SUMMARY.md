# Adapter Pattern - Weather Provider Implementation

## Overview
This implementation demonstrates the **Adapter Pattern**, which allows incompatible interfaces to work together. The pattern solves the problem of integrating multiple weather APIs with different data formats into a unified interface.

---

## Problem: Why We Need the Adapter Pattern

Without adapters, we would have this issue:
- **OpenWeatherMapAPI**: Returns data in the correct format already (Celsius, strings, percentages)
- **WeatherStackAPI**: Returns data in different formats (Fahrenheit, numeric codes, ratios)
- **WeatherDashboard**: Expects a unified interface

### The Hard Way (Without Adapters):
We'd need to modify WeatherDashboard for each API, or create conversion logic everywhere → Code duplication and maintenance nightmare!

### The Solution (With Adapters):
Create adapter classes that implement a common **WeatherProvider** interface, translating each API's response to the expected format.

---

## Architecture

### 1. **WeatherProvider Interface** (The Contract)
```
public interface WeatherProvider {
    double getTemperature(String city);      // Returns temperature in Celsius
    String getCondition(String city);        // Returns human-readable condition
    int getHumidity(String city);            // Returns humidity as integer percentage
}
```

### 2. **OpenWeatherMapAPI** (Existing API)
- Already returns data in the correct format
- Methods: `getTemperatureCelsius()`, `getWeatherCondition()`, `getHumidityPercent()`
- No conversion needed

### 3. **WeatherStackAPI** (Existing API with incompatible format)
- Returns temperature in **Fahrenheit** (needs conversion to Celsius)
- Returns condition as **numeric codes** (needs conversion to strings)
- Returns humidity as **decimal ratio** (needs conversion to percentage)

---

## Implementation

### Adapter 1: OpenWeatherMapAdapter
```
OpenWeatherMapAPI → OpenWeatherMapAdapter → WeatherProvider
```

**No conversion needed** - Just delegates directly:
- `getTemperature()` → calls `getTemperatureCelsius()`
- `getCondition()` → calls `getWeatherCondition()`
- `getHumidity()` → calls `getHumidityPercent()`

### Adapter 2: WeatherStackAdapter
```
WeatherStackAPI → WeatherStackAdapter → WeatherProvider
```

**Performs necessary conversions**:
- **Temperature**: Fahrenheit to Celsius using formula `(F - 32) × 5/9`
  - Example: 108.5°F → 42.5°C
- **Condition**: Numeric code to human-readable string
  - 0 = "Clear", 1 = "Partly Cloudy", 2 = "Cloudy", 3 = "Rain", 4 = "Storm"
- **Humidity**: Decimal ratio (0.0-1.0) to integer percentage (0-100)
  - Example: 0.25 → 25%

### Provider 3: MockWeatherProvider
```
MockWeatherProvider (implements WeatherProvider)
```

**For testing purposes** - Returns hardcoded values without calling any API:
- Delhi: 30°C, Sunny, 40% humidity
- Mumbai: 28°C, Cloudy, 70% humidity
- Others: 25°C, Clear, 50% humidity

---

## WeatherDashboard (Depends on Interface, Not Implementation)

```java
public class WeatherDashboard {
    private WeatherProvider provider;  // Depends on interface
    
    public WeatherDashboard(WeatherProvider provider) {
        this.provider = provider;
    }
    
    public void displayWeather(String city) {
        // Works identically regardless of which provider is used!
        double tempC = provider.getTemperature(city);
        String condition = provider.getCondition(city);
        int humidity = provider.getHumidity(city);
        
        System.out.println(city);
        System.out.println("Temp: " + tempC);
        System.out.println("Condition: " + condition);
        System.out.println("Humidity: " + humidity);
    }
}
```

**Key Benefit**: Can use ANY implementation of WeatherProvider without modification!

---

## Usage Example

```java
// Using OpenWeatherMap API
WeatherDashboard dash1 = new WeatherDashboard(
    new OpenWeatherMapAdapter(new OpenWeatherMapAPI("API_KEY"))
);
dash1.displayWeather("Delhi");

// Using WeatherStack API
WeatherDashboard dash2 = new WeatherDashboard(
    new WeatherStackAdapter(new WeatherStackAPI("API_KEY"))
);
dash2.displayWeather("Delhi");

// Using Mock (for testing)
WeatherDashboard dash3 = new WeatherDashboard(
    new MockWeatherProvider()
);
dash3.displayWeather("Delhi");
```

**Output (Identical for all three!):**
```
Delhi
Temp: 42.5
Condition: Sunny
Humidity: 25
```

---

## Program Output

```
=== OpenWeatherMap ===
[OpenWeatherMap] Fetching Delhi
Delhi
Temp: 42.5
Condition: Sunny
Humidity: 25
[OpenWeatherMap] Fetching Mumbai
Mumbai
Temp: 33.0
Condition: Rainy
Humidity: 89

=== WeatherStack ===
[WeatherStack] Querying Delhi
Delhi
Temp: 42.5
Condition: Clear
Humidity: 25
[WeatherStack] Querying Mumbai
Mumbai
Temp: 33.0
Condition: Rain
Humidity: 89

=== Mock Provider ===
Delhi
Temp: 30.0
Condition: Sunny
Humidity: 40
Mumbai
Temp: 28.0
Condition: Cloudy
Humidity: 70
```

---

## Key Benefits of the Adapter Pattern

✅ **Unified Interface**: Multiple incompatible APIs work through one interface  
✅ **Loose Coupling**: WeatherDashboard depends only on the interface, not concrete implementations  
✅ **Easy to Extend**: Add new weather providers by creating new adapters  
✅ **Easy to Test**: MockWeatherProvider allows testing without API calls  
✅ **Reusability**: Adapters can be used in multiple places with different dashboards  
✅ **Maintainability**: Changes to one API only affect its adapter, not the whole system  

---

## Files Included

1. **WeatherProvider.java** - Interface defining the contract
2. **OpenWeatherMapAPI.java** - Existing API (correct format)
3. **OpenWeatherMapAdapter.java** - Adapter for OpenWeatherMap
4. **WeatherStackAPI.java** - Existing API (incorrect format)
5. **WeatherStackAdapter.java** - Adapter with necessary conversions
6. **MockWeatherProvider.java** - Test implementation
7. **WeatherDashboard.java** - Client code using the interface
8. **Main.java** - Demonstrates all three providers

---

## Real-World Applications

- **Database Drivers**: Converting different database protocols to JDBC
- **Payment Gateways**: Adapting Stripe, PayPal, Square APIs to unified interface
- **Logging Frameworks**: Adapting Log4j, SLF4J, Logback to common interface
- **ORM Frameworks**: Hibernate adapting different SQL dialects
- **Cloud Providers**: Adapting AWS, Azure, GCP APIs to common interface

---

