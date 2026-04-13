package Adapter;

public class MockWeatherProvider implements WeatherProvider {

    @Override
    public double getTemperature(String city) {
        // Hardcoded values for testing
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
