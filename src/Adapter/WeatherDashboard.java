package Adapter;

public class WeatherDashboard {
    private WeatherProvider provider;

    public WeatherDashboard(WeatherProvider provider){
        this.provider = provider;
    }

    public void displayWeather(String city){
        double tempC = provider.getTemperature(city);
        String condition = provider.getCondition(city);
        int humidity = provider.getHumidity(city);

        System.out.println(city);
        System.out.println("Temp: " + tempC);
        System.out.println("Condition: " + condition);
        System.out.println("Humidity: " + humidity);
    }


}
