package com.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    private final WebClient.Builder webClientBuilder;
    
    public Map<String, Object> getWeatherForecast(String nx, String ny) {
        LocalDateTime now = LocalDateTime.now();
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = "0500"; // 05시 발표 데이터 기준
        
        String apiUrl = String.format(
            "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
            "?serviceKey=%s" +
            "&numOfRows=100" +
            "&pageNo=1" +
            "&base_date=%s" +
            "&base_time=%s" +
            "&nx=%s" +
            "&ny=%s" +
            "&dataType=JSON",
            apiKey, baseDate, baseTime, nx, ny
        );
        
        WebClient webClient = webClientBuilder.build();
        Map<String, Object> response = webClient.get()
            .uri(apiUrl)
            .retrieve()
            .bodyToMono(Map.class)
            .block();
            
        return response;
    }
}
