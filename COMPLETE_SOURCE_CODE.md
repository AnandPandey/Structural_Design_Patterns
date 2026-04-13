# Complete Source Code - Adapter Pattern Implementation

## File Structure
```
Adapter/
├── WeatherProvider.java (Interface)
├── OpenWeatherMapAPI.java (Existing API #1)
├── OpenWeatherMapAdapter.java (Adapter #1)
├── WeatherStackAPI.java (Existing API #2)
├── WeatherStackAdapter.java (Adapter #2 - with conversions)
├── MockWeatherProvider.java (Testing implementation)
├── WeatherDashboard.java (Client code)
└── Main.java (Demo)
```

---

## 1. WeatherProvider.java (The Interface)

```java
package Adapter;

public interface WeatherProvider {
    /**
     * Get temperature in Celsius
     * @param city The city name
     * @return Temperature in Celsius
     */
    double getTemperature(String city);

    /**
     * Get weather condition as human-readable string
     * @param city The city name
     * @return Condition (e.g., "Sunny", "Rainy", "Cloudy")
     */
    String getCondition(String city);

    /**
     * Get humidity as integer percentage
     * @param city The city name
     * @return Humidity percentage (0-100)
     */
    int getHumidity(String city);
}
```

**Purpose**: Defines the contract that all weather providers must implement.
**Benefit**: Enables loose coupling and dependency injection.

---

## 2. OpenWeatherMapAPI.java (Existing API - Already in Correct Format)

```java
package Adapter;

public class OpenWeatherMapAPI {
    private String apiKey;

    public OpenWeatherMapAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    // Returns temperature in CELSIUS (correct format!)
    public double getTemperatureCelsius(String city) {
        System.out.println("[OpenWeatherMap] Fetching " + city);
        // Simulated API call
        if (city.equals("Delhi")) return 42.5;
        if (city.equals("Mumbai")) return 33.0;
        if (city.equals("Bangalore")) return 28.5;
        return 25.0;
    }

    // Returns condition as a HUMAN-READABLE STRING (correct format!)
    public String getWeatherCondition(String city) {
        if (city.equals("Delhi")) return "Sunny";
        if (city.equals("Mumbai")) return "Rainy";
        return "Cloudy";
    }

    // Returns humidity as PERCENTAGE (correct format!)
    public int getHumidityPercent(String city) {
        if (city.equals("Delhi")) return 25;
        if (city.equals("Mumbai")) return 89;
        return 60;
    }
}
```

**Key Points**:
- Already returns data in the expected format
- Method names are descriptive but different from interface
- Adapter simply bridges these differences

---

## 3. OpenWeatherMapAdapter.java (Simple Adapter)

```java
package Adapter;

public class OpenWeatherMapAdapter implements WeatherProvider {
    private final OpenWeatherMapAPI api;

    /**
     * Constructor injection of the API
     * @param api The OpenWeatherMapAPI instance
     */
    public OpenWeatherMapAdapter(OpenWeatherMapAPI api) {
        this.api = api;
    }

    @Override
    public double getTemperature(String city) {
        // Direct delegation - no conversion needed
        return api.getTemperatureCelsius(city);
    }

    @Override
    public String getCondition(String city) {
        // Direct delegation - already in correct format
        return api.getWeatherCondition(city);
    }

    @Override
    public int getHumidity(String city) {
        // Direct delegation - already in correct format
        return api.getHumidityPercent(city);
    }
}
```

**Why it's simple**: 
- OpenWeatherMapAPI already returns data in the correct format
- Adapter just delegates to the appropriate methods
- No data transformation needed

---

## 4. WeatherStackAPI.java (Existing API - Incompatible Format)

```java
package Adapter;

public class WeatherStackAPI {
    private String accessKey;

    public WeatherStackAPI(String accessKey) {
        this.accessKey = accessKey;
    }

    // Returns temperature in FAHRENHEIT (wrong format!)
    public double queryTempFahrenheit(String location) {
        System.out.println("[WeatherStack] Querying " + location);
        // Simulated API call
        if (location.equals("Delhi")) return 108.5;      // Should be 42.5°C
        if (location.equals("Mumbai")) return 91.4;      // Should be 33°C
        if (location.equals("Bangalore")) return 83.3;   // Should be 28.5°C
        return 77.0;                                      // Should be 25°C
    }

    // Returns condition as NUMERIC CODE (wrong format!)
    // 0=Clear, 1=Partly Cloudy, 2=Cloudy, 3=Rain, 4=Storm
    public int queryConditionCode(String location) {
        if (location.equals("Delhi")) return 0;
        if (location.equals("Mumbai")) return 3;
        return 2;
    }

    // Returns humidity as DECIMAL RATIO (wrong format!)
    // Range: 0.0 to 1.0, should be 0-100 percentage
    public double queryHumidityRatio(String location) {
        if (location.equals("Delhi")) return 0.25;    // Should be 25%
        if (location.equals("Mumbai")) return 0.89;   // Should be 89%
        return 0.60;                                   // Should be 60%
    }
}
```

**Problem**: Data is in incompatible formats
- Fahrenheit instead of Celsius
- Numeric codes instead of readable strings  
- Decimal ratios instead of percentages

---

## 5. WeatherStackAdapter.java (Complex Adapter with Conversions)

```java
package Adapter;

public class WeatherStackAdapter implements WeatherProvider {
    private final WeatherStackAPI api;

    /**
     * Constructor injection of the API
     * @param api The WeatherStackAPI instance
     */
    public WeatherStackAdapter(WeatherStackAPI api) {
        this.api = api;
    }

    @Override
    public double getTemperature(String city) {
        // Conversion required: Fahrenheit to Celsius
        return fahrToCels(api.queryTempFahrenheit(city));
    }

    @Override
    public String getCondition(String city) {
        // Conversion required: Numeric code to string
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
        // Conversion required: Decimal ratio to percentage
        double ratio = api.queryHumidityRatio(city);
        return (int) (ratio * 100);
    }

    /**
     * Helper method to convert Fahrenheit to Celsius
     * Formula: C = (F - 32) × 5/9
     * @param fahrenheit Temperature in Fahrenheit
     * @return Temperature in Celsius
     */
    private double fahrToCels(double fahrenheit) {
        return (5.0 / 9.0) * (fahrenheit - 32);
    }
}
```

**Key Features**:
1. **Temperature Conversion** - Formula: C = (F - 32) × 5/9
2. **Condition Mapping** - Maps numeric codes to readable strings
3. **Humidity Conversion** - Converts decimal ratio to percentage
4. **Encapsulation** - Helper method `fahrToCels()` is private

---

## 6. MockWeatherProvider.java (Testing Implementation)

```java
package Adapter;

public class MockWeatherProvider implements WeatherProvider {
    /**
     * Returns hardcoded temperature for testing
     * No API call required!
     * @param city The city name
     * @return Hardcoded temperature in Celsius
     */
    @Override
    public double getTemperature(String city) {
        if (city.equals("Delhi")) return 30.0;
        if (city.equals("Mumbai")) return 28.0;
        return 25.0;
    }

    /**
     * Returns hardcoded condition for testing
     * No API call required!
     * @param city The city name
     * @return Hardcoded weather condition
     */
    @Override
    public String getCondition(String city) {
        if (city.equals("Delhi")) return "Sunny";
        if (city.equals("Mumbai")) return "Cloudy";
        return "Clear";
    }

    /**
     * Returns hardcoded humidity for testing
     * No API call required!
     * @param city The city name
     * @return Hardcoded humidity percentage
     */
    @Override
    public int getHumidity(String city) {
        if (city.equals("Delhi")) return 40;
        if (city.equals("Mumbai")) return 70;
        return 50;
    }
}
```

**Benefits for Testing**:
- ✅ No network calls (fast tests)
- ✅ Predictable results
- ✅ Works offline
- ✅ No API rate limits
- ✅ Easy to simulate various scenarios

---

## 7. WeatherDashboard.java (Client Code - Depends on Interface)

```java
package Adapter;

public class WeatherDashboard {
    /**
     * Depends on interface, NOT concrete implementation
     * This is the key principle: Depend on abstraction, not concrete classes
     */
    private WeatherProvider provider;

    /**
     * Constructor injection - allows any WeatherProvider implementation
     * @param provider Any implementation of WeatherProvider
     */
    public WeatherDashboard(WeatherProvider provider) {
        this.provider = provider;
    }

    /**
     * Display weather information
     * Works identically with ANY WeatherProvider implementation
     * @param city The city to display weather for
     */
    public void displayWeather(String city) {
        // Get data from provider (works with any implementation!)
        double tempC = provider.getTemperature(city);
        String condition = provider.getCondition(city);
        int humidity = provider.getHumidity(city);

        // Display uniformly
        System.out.println(city);
        System.out.println("Temp: " + tempC + "°C");
        System.out.println("Condition: " + condition);
        System.out.println("Humidity: " + humidity + "%");
    }
}
```

**Design Principle: Dependency Injection**
- Constructor parameter accepts interface type
- Client code doesn't care about concrete implementation
- Easy to switch providers without changing dashboard

---

## 8. Main.java (Complete Demo)

```java
package Adapter;

public class Main {
    public static void main(String[] args) {
        String[] cities = {"Delhi", "Mumbai"};

        // ===== DEMONSTRATION 1: OpenWeatherMap API =====
        System.out.println("=== OpenWeatherMap ===");
        WeatherDashboard dash1 = new WeatherDashboard(
            new OpenWeatherMapAdapter(
                new OpenWeatherMapAPI("OWM-KEY-123")
            )
        );
        for (String city : cities) {
            dash1.displayWeather(city);
        }

        // ===== DEMONSTRATION 2: WeatherStack API =====
        System.out.println("\n=== WeatherStack ===");
        WeatherDashboard dash2 = new WeatherDashboard(
            new WeatherStackAdapter(
                new WeatherStackAPI("WS-KEY-456")
            )
        );
        for (String city : cities) {
            dash2.displayWeather(city);
        }

        // ===== DEMONSTRATION 3: Mock Provider (Testing) =====
        System.out.println("\n=== Mock Provider ===");
        WeatherDashboard dash3 = new WeatherDashboard(
            new MockWeatherProvider()
        );
        for (String city : cities) {
            dash3.displayWeather(city);
        }
    }
}
```

**Key Observations**:
- Same `WeatherDashboard` class used for all three providers
- Same `displayWeather()` method works identically
- Output format is identical regardless of provider
- Easy to add more providers without modifying dashboard

---

## Compilation and Execution

### Compile
```bash
cd "C:\Users\ap901\OneDrive\Desktop\sem 4\Projects\Structural Patterns"
javac -d "out\production\Structural Patterns" src\Adapter\*.java
```

### Run
```bash
java -cp "out\production\Structural Patterns" Adapter.Main
```

### Output
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

=== Mock Provider ===
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

## Key Takeaways

1. **Adapter Pattern Purpose**: Convert incompatible interfaces to compatible ones
2. **WeatherProvider Interface**: Defines the contract all providers must follow
3. **OpenWeatherMapAdapter**: Simple adapter - just delegates (no conversion)
4. **WeatherStackAdapter**: Complex adapter - performs necessary conversions
5. **MockWeatherProvider**: Test implementation - hardcoded values
6. **WeatherDashboard**: Client code - depends on interface, not implementation
7. **Dependency Injection**: Enables flexibility and testability
8. **Same Output**: All three providers produce identical dashboard output

---

