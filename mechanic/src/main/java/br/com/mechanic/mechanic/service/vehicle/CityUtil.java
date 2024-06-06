package br.com.mechanic.mechanic.service.vehicle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CityUtil {

    @Value("${google.api.geocoding.url}")
    private String geocodingUrl;

    @Value("${google.api.key}")
    private String apiKey;

    public boolean isValidCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(geocodingUrl, city, apiKey);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody().contains("\"status\" : \"OK\"");
    }

    public String formatCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }

        String[] words = city.toLowerCase().split("\\s+");
        StringBuilder formattedCity = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                formattedCity.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return formattedCity.toString().trim();
    }
}
