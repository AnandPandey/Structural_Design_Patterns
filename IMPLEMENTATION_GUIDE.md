# Adapter Pattern - Complete Implementation Guide

## Step-by-Step Implementation

### Step 1: Define the WeatherProvider Interface

The interface acts as a contract that all providers must implement. This ensures compatibility regardless of the underlying API.

```java
package Adapter;

public interface WeatherProvider {
    double getTemperature(String city);    // Celsius
    String getCondition(String city);      // Human-readable (e.g., "Sunny")
    int getHumidity(String city);          // Percentage (0-100)
}
```

**Why this design?**
- **Single Responsibility**: Each method has one clear purpose
- **Consistency**: All implementations return data in the same units
- **Simplicity**: Easy to understand and implement

---

### Step 2: Create OpenWeatherMapAdapter

This adapter wraps the OpenWeatherMapAPI which already returns data in the correct format.

```java
package Adapter;

public class OpenWeatherMapAdapter implements WeatherProvider {
    private final OpenWeatherMapAPI api;

    public OpenWeatherMapAdapter(OpenWeatherMapAPI api) {
        this.api = api;
    }

    @Override
    public double getTemperature(String city) {
        return api.getTemperatureCelsius(city);
    }

    @Override
    public String getCondition(String city) {
        return api.getWeatherCondition(city);
    }

    @Override
    public int getHumidity(String city) {
        return api.getHumidityPercent(city);
    }
}
```

**Key Points:**
- Minimal adapter - just delegates to API methods
- No conversion needed - API already returns correct format
- Composition over inheritance - uses dependency injection

---

### Step 3: Create WeatherStackAdapter with Conversions

This adapter wraps WeatherStackAPI and performs necessary data transformations.

```java
package Adapter;

public class WeatherStackAdapter implements WeatherProvider {
    private final WeatherStackAPI api;

    public WeatherStackAdapter(WeatherStackAPI api) {
        this.api = api;
    }

    @Override
    public double getTemperature(String city) {
        // Convert Fahrenheit to Celsius: C = (F - 32) × 5/9
        return fahrToCels(api.queryTempFahrenheit(city));
    }

    @Override
    public String getCondition(String city) {
        // Convert numeric code to human-readable string
        int code = api.queryConditionCode(city);
        switch(code) {
            case 0: return "Clear";
            case 1: return "Partly Cloudy";
            case 2: return "Cloudy";
            case 3: return "Rain";
            case 4: return "Storm";
            default: return "Unknown";
        }
    }

    @Override
    public int getHumidity(String city) {
        // Convert decimal ratio (0.0-1.0) to percentage (0-100)
        double ratio = api.queryHumidityRatio(city);
        return (int) (ratio * 100);
    }

    // Helper method for temperature conversion
    private double fahrToCels(double fahrenheit) {
        return (5.0 / 9.0) * (fahrenheit - 32);
    }
}
```

**Conversion Details:**

1. **Temperature Conversion (Fahrenheit → Celsius)**
   ```
   Formula: C = (F - 32) × 5/9
   Example: 108.5°F → (108.5 - 32) × 5/9 = 76.5 × 5/9 = 42.5°C
   ```

2. **Condition Code Mapping**
   ```
   0 → "Clear"
   1 → "Partly Cloudy"
   2 → "Cloudy"
   3 → "Rain"
   4 → "Storm"
   ```

3. **Humidity Ratio to Percentage**
   ```
   Formula: percentage = ratio × 100
   Example: 0.25 → 25%, 0.89 → 89%
   ```

---

### Step 4: Create MockWeatherProvider for Testing

This implementation allows testing without making actual API calls.

```java
package Adapter;

public class MockWeatherProvider implements WeatherProvider {
    @Override
    public double getTemperature(String city) {
        if (city.equals("Delhi")) return 30.0;
        if (city.equals("Mumbai")) return 28.0;
        return 25.0;
    }

    @Override
    public String getCondition(String city) {
        if (city.equals("Delhi")) return "Sunny";
        if (city.equals("Mumbai")) return "Cloudy";
        return "Clear";
    }

    @Override
    public int getHumidity(String city) {
        if (city.equals("Delhi")) return 40;
        if (city.equals("Mumbai")) return 70;
        return 50;
    }
}
```

**Benefits of Mock Provider:**
- ✅ No network calls (faster tests)
- ✅ Predictable, consistent results
- ✅ Easy to test edge cases
- ✅ Works offline
- ✅ No API rate limits

---

### Step 5: Refactor WeatherDashboard to Use Interface

The dashboard should depend on the interface, not concrete implementations.

```java
package Adapter;

public class WeatherDashboard {
    private WeatherProvider provider;  // Depends on INTERFACE, not implementation

    // Constructor injection - allows any provider
    public WeatherDashboard(WeatherProvider provider) {
        this.provider = provider;
    }

    // Display method works with ANY provider
    public void displayWeather(String city) {
        double tempC = provider.getTemperature(city);
        String condition = provider.getCondition(city);
        int humidity = provider.getHumidity(city);

        System.out.println(city);
        System.out.println("Temp: " + tempC + "°C");
        System.out.println("Condition: " + condition);
        System.out.println("Humidity: " + humidity + "%");
    }
}
```

**Design Pattern: Dependency Injection**
- Constructor accepts interface, not concrete class
- Allows easy switching between implementations
- Enables testing with mock objects

---

### Step 6: Update Main.java to Demonstrate All Providers

```java
package Adapter;

public class Main {
    public static void main(String[] args) {
        String[] cities = {"Delhi", "Mumbai"};

        // 1. Using OpenWeatherMap API
        System.out.println("=== OpenWeatherMap ===");
        WeatherDashboard dash1 = new WeatherDashboard(
            new OpenWeatherMapAdapter(new OpenWeatherMapAPI("OWM-KEY-123"))
        );
        for (String city : cities) {
            dash1.displayWeather(city);
        }

        // 2. Using WeatherStack API
        System.out.println("\n=== WeatherStack ===");
        WeatherDashboard dash2 = new WeatherDashboard(
            new WeatherStackAdapter(new WeatherStackAPI("WS-KEY-456"))
        );
        for (String city : cities) {
            dash2.displayWeather(city);
        }

        // 3. Using Mock Provider (for testing)
        System.out.println("\n=== Mock Provider (Testing) ===");
        WeatherDashboard dash3 = new WeatherDashboard(
            new MockWeatherProvider()
        );
        for (String city : cities) {
            dash3.displayWeather(city);
        }
    }
}
```

**Key Insight**: Same `displayWeather()` method works identically with all three providers!

---

## Comparison: With vs Without Adapter Pattern

### WITHOUT Adapter (Bad - Tight Coupling)
```java
// Dashboard needs to know about each API
public class WeatherDashboard {
    private OpenWeatherMapAPI api1;
    private WeatherStackAPI api2;
    
    // Multiple methods, each handling different API format
    public void displayWithOpenWeather(String city) { /* ... */ }
    public void displayWithWeatherStack(String city) { /* ... */ }
    
    // Code duplication!
    // Hard to add new APIs
    // Difficult to test
}
```

**Problems:**
- ❌ Dashboard tightly coupled to specific APIs
- ❌ Code duplication (display logic repeated)
- ❌ Hard to add new providers
- ❌ Difficult to test (requires real API calls)
- ❌ High maintenance cost

### WITH Adapter (Good - Loose Coupling)
```java
public class WeatherDashboard {
    private WeatherProvider provider;  // Depends on interface
    
    public WeatherDashboard(WeatherProvider provider) {
        this.provider = provider;
    }
    
    // Single method works with any provider
    public void displayWeather(String city) { /* ... */ }
}
```

**Benefits:**
- ✅ Dashboard depends on interface, not implementation
- ✅ No code duplication
- ✅ Easy to add new providers (just create new adapter)
- ✅ Easy to test (use MockWeatherProvider)
- ✅ Low maintenance cost
- ✅ Follows SOLID principles

---

## Conversion Formulas Reference

### Fahrenheit to Celsius
```
C = (F - 32) × 5/9

Examples:
- 32°F = 0°C (freezing point)
- 98.6°F = 37°C (body temperature)
- 108.5°F = 42.5°C
- 91.4°F = 33°C
```

### Percentage from Decimal Ratio
```
Percentage = Ratio × 100

Examples:
- 0.25 → 25%
- 0.89 → 89%
- 0.60 → 60%
```

---

## Testing the Implementation

### Compile
```bash
javac -d out/production/Structural\ Patterns src/Adapter/*.java
```

### Run
```bash
java -cp out/production/Structural\ Patterns Adapter.Main
```

### Expected Output
```
=== OpenWeatherMap ===
[OpenWeatherMap] Fetching Delhi
Delhi
Temp: 42.5°C
Condition: Sunny
Humidity: 25%

[OpenWeatherMap] Fetching Mumbai
Mumbai
Temp: 33.0°C
Condition: Rainy
Humidity: 89%

=== WeatherStack ===
[WeatherStack] Querying Delhi
Delhi
Temp: 42.5°C
Condition: Clear
Humidity: 25%

[WeatherStack] Querying Mumbai
Mumbai
Temp: 33.0°C
Condition: Rain
Humidity: 89%

=== Mock Provider (Testing) ===
Delhi
Temp: 30.0°C
Condition: Sunny
Humidity: 40%

Mumbai
Temp: 28.0°C
Condition: Cloudy
Humidity: 70%
```

---

## Design Patterns Used

### 1. **Adapter Pattern** (Main Pattern)
- Converts incompatible interface to compatible one
- Allows integration of external systems
- Located in: OpenWeatherMapAdapter, WeatherStackAdapter

### 2. **Dependency Injection** (Supporting Pattern)
- WeatherDashboard depends on interface, not implementation
- Makes code testable and flexible
- Located in: WeatherDashboard constructor

### 3. **Factory-like Pattern** (Optional Enhancement)
```java
// Could add a factory to create providers easily
public class WeatherProviderFactory {
    public static WeatherProvider createOpenWeather(String apiKey) {
        return new OpenWeatherMapAdapter(new OpenWeatherMapAPI(apiKey));
    }
    
    public static WeatherProvider createWeatherStack(String apiKey) {
        return new WeatherStackAdapter(new WeatherStackAPI(apiKey));
    }
    
    public static WeatherProvider createMock() {
        return new MockWeatherProvider();
    }
}

// Usage becomes simpler:
WeatherProvider provider = WeatherProviderFactory.createOpenWeather("KEY");
```

---

## Summary

The Adapter Pattern successfully:

1. ✅ Creates a unified interface (`WeatherProvider`)
2. ✅ Adapts incompatible APIs to work together
3. ✅ Maintains separation of concerns
4. ✅ Allows easy testing with mocks
5. ✅ Enables seamless switching between providers
6. ✅ Follows SOLID principles
7. ✅ Reduces code duplication
8. ✅ Makes the codebase maintainable and extensible

---

