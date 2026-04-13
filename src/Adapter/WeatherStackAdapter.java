package Adapter;

public class WeatherStackAdapter implements WeatherProvider{

    private WeatherStackAPI api;

    public WeatherStackAdapter(WeatherStackAPI api) {
        this.api = api;
    }

    @Override
    public double getTemperature(String city) {
        return fahrToCels(api.queryTempFahrenheit(city));
    }

    @Override
    public String getCondition(String city) {
        int k = api.queryConditionCode(city);
        if (k == 0) return "Clear";
        if(k ==1) return "Partly Cloudy";
        if(k == 2) return "Cloudy";
        if(k == 3) return "Rain";
        if(k == 4) return "Storm";
        return "Sunny";
    }

    @Override
    public int getHumidity(String city) {
        double humidity = api.queryHumidityRatio(city);
        return (int) (humidity * 100);
    }

    public double fahrToCels(double fah){
        return (5.0/9.0) * (fah - 32);
    }
}
