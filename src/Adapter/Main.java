package Adapter;

public class Main {
    public static void main(String[] args){
        // Using OpenWeatherMap
        WeatherDashboard dash1 = new WeatherDashboard(new OpenWeatherMapAdapter(new OpenWeatherMapAPI("OWM-KEY-123")));
        System.out.println("=== OpenWeatherMap ===");
        dash1.displayWeather("Delhi");
        dash1.displayWeather("Mumbai");

        // Using WeatherStack
        WeatherDashboard dash2 = new WeatherDashboard(new WeatherStackAdapter(new WeatherStackAPI("WS-KEY-456")));
        System.out.println("=== WeatherStack ===");
        dash2.displayWeather("Delhi");
        dash2.displayWeather("Mumbai");

        // Using Mock for testing
        WeatherDashboard dash3 = new WeatherDashboard(new MockWeatherProvider());
        System.out.println("=== Mock Provider ===");
        dash3.displayWeather("Delhi");
        dash3.displayWeather("Mumbai");
    }
}
