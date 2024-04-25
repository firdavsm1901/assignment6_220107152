package com.example;

import java.util.HashMap;
import java.util.Map;

class WeatherData {
    private int temperature;
    private String description;
    private int windSpeed;
    private int humidity;

    public WeatherData(int temperature, String description, int windSpeed, int humidity) {
        this.temperature = temperature;
        this.description = description;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}

class WeatherServiceAPI1 {
    public Map<String, Object> fetchWeather() {
        Map<String, Object> weatherData = new HashMap<>();
        weatherData.put("temp", 25);
        weatherData.put("desc", "Sunny");
        weatherData.put("wind_speed", 10);
        weatherData.put("humidity", 50);
        return weatherData;
    }
}

class WeatherServiceAPI2 {
    public Map<String, Object> getWeather() {
        Map<String, Object> weatherData = new HashMap<>();
        weatherData.put("temperature", 22);
        weatherData.put("weather", "Clear");
        weatherData.put("wind", 8);
        weatherData.put("humidity", 60);
        return weatherData;
    }
}

interface WeatherService {
    WeatherData getWeather();
}

class API1WeatherAdapter implements WeatherService {
    private WeatherServiceAPI1 api;

    public API1WeatherAdapter(WeatherServiceAPI1 api) {
        this.api = api;
    }

    @Override
    public WeatherData getWeather() {
        Map<String, Object> data = api.fetchWeather();
        int temperature = (int) data.get("temp");
        String description = (String) data.get("desc");
        int windSpeed = (int) data.get("wind_speed");
        int humidity = (int) data.get("humidity");
        return new WeatherData(temperature, description, windSpeed, humidity);
    }
}

class API2WeatherAdapter implements WeatherService {
    private WeatherServiceAPI2 api;

    public API2WeatherAdapter(WeatherServiceAPI2 api) {
        this.api = api;
    }

    @Override
    public WeatherData getWeather() {
        Map<String, Object> data = api.getWeather();
        int temperature = (int) data.get("temperature");
        String description = (String) data.get("weather");
        int windSpeed = (int) data.get("wind");
        int humidity = (int) data.get("humidity");
        return new WeatherData(temperature, description, windSpeed, humidity);
    }
}

public class Main {
    public static void main(String[] args) {
        WeatherServiceAPI1 api1 = new WeatherServiceAPI1();
        WeatherService api1Adapter = new API1WeatherAdapter(api1);
        WeatherData weatherDataFromAPI1 = api1Adapter.getWeather();
        System.out.println();
        System.out.println("Weather data from API 1: " + weatherDataFromAPI1.getTemperature() + "°C, " + weatherDataFromAPI1.getDescription() + ", Wind Speed: " + weatherDataFromAPI1.getWindSpeed() + " km/h, Humidity: " + weatherDataFromAPI1.getHumidity() + "%");

        WeatherServiceAPI2 api2 = new WeatherServiceAPI2();
        WeatherService api2Adapter = new API2WeatherAdapter(api2);
        WeatherData weatherDataFromAPI2 = api2Adapter.getWeather();
        System.out.println("Weather data from API 2: " + weatherDataFromAPI2.getTemperature() + "°C, " + weatherDataFromAPI2.getDescription() + ", Wind Speed: " + weatherDataFromAPI2.getWindSpeed() + " km/h, Humidity: " + weatherDataFromAPI2.getHumidity() + "%");
        System.out.println();
    }
}
