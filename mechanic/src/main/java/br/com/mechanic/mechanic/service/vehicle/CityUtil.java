package br.com.mechanic.mechanic.service.vehicle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
        String url = String.format("%s?address=%s&key=%s", geocodingUrl, city + ", Brazil", apiKey);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());

            if (!jsonResponse.getString("status").equals("OK")) {
                return false;
            }

            JSONArray results = jsonResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                JSONArray addressComponents = result.getJSONArray("address_components");

                for (int j = 0; j < addressComponents.length(); j++) {
                    JSONObject component = addressComponents.getJSONObject(j);
                    JSONArray types = component.getJSONArray("types");

                    for (int k = 0; k < types.length(); k++) {
                        String type = types.getString(k);
                        if (type.equals("locality") || type.equals("administrative_area_level_2")) {
                            String longName = component.getString("long_name");
                            if (longName.equalsIgnoreCase(city)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false; // City not found in the response
        } catch (Exception e) {
            // Log the error or handle it as needed
            e.printStackTrace();
            return false;
        }
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
