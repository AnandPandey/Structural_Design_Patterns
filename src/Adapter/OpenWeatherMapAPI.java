package Adapter;

public class OpenWeatherMapAPI {
    private String apiKey;
    public OpenWeatherMapAPI(String apiKey) {
        this.apiKey = apiKey;
    }
    // Returns temperature in Celsius
    public double getTemperatureCelsius(String city) {
        System.out.println("[OpenWeatherMap] Fetching " + city);
        // Simulated API call
        if (city.equals("Delhi")) return 42.5;
        if (city.equals("Mumbai")) return 33.0;
        if (city.equals("Bangalore")) return 28.5;
        return 25.0;
    }
    // Returns condition as a human-readable string
    public String getWeatherCondition(String city) {
        if (city.equals("Delhi")) return "Sunny";
        if (city.equals("Mumbai")) return "Rainy";

        return "Cloudy";
    }
    // Returns humidity as a percentage
    public int getHumidityPercent(String city) {
        if (city.equals("Delhi")) return 25;
        if (city.equals("Mumbai")) return 89;
        return 60;
    }
}
