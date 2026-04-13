package Adapter;

public class OpenWeatherMapAdapter implements WeatherProvider{
    private final OpenWeatherMapAPI api;

    public OpenWeatherMapAdapter(OpenWeatherMapAPI api){
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
