package Adapter;

public interface WeatherProvider {
    double getTemperature(String city);
    String getCondition(String city);
    int getHumidity(String city);
}
