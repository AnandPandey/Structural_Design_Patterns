package Adapter;

public class WeatherStackAPI {
    private String accessKey;
    public WeatherStackAPI(String accessKey) {
        this.accessKey = accessKey;
    }
    // Returns temperature in FAHRENHEIT (not Celsius!)
    public double queryTempFahrenheit(String location) {
        System.out.println("[WeatherStack] Querying " + location);
        if (location.equals("Delhi")) return 108.5;
        if (location.equals("Mumbai")) return 91.4;
        if (location.equals("Bangalore")) return 83.3;
        return 77.0;
    }
    // Returns condition as a NUMERIC CODE (not a string!)
    // 0=Clear, 1=Partly Cloudy, 2=Cloudy, 3=Rain, 4=Storm
    public int queryConditionCode(String location) {
        if (location.equals("Delhi")) return 0;
        if (location.equals("Mumbai")) return 3;
        return 2;
    }
    // Returns humidity as a decimal (0.0 to 1.0, not percentage!)
    public double queryHumidityRatio(String location) {
        if (location.equals("Delhi")) return 0.25;
        if (location.equals("Mumbai")) return 0.89;
        return 0.60;
    }
}
