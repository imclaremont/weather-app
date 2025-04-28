package com.weather.controller;

import com.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class WeatherController {
    
    private final WeatherService weatherService;
    
    @GetMapping("/forecast")
    public ResponseEntity<Map<String, Object>> getWeatherForecast(
            @RequestParam(defaultValue = "60") String nx,
            @RequestParam(defaultValue = "127") String ny) {
        return ResponseEntity.ok(weatherService.getWeatherForecast(nx, ny));
    }

    @GetMapping({"", "/"})
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Weather API Server is running");
    }
}
