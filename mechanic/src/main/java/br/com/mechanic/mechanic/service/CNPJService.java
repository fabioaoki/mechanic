package br.com.mechanic.mechanic.service;


import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CNPJService {

    private static final String API_URL = "https://www.receitaws.com.br/v1/cnpj/";

    public JSONObject consultarCNPJ(String cnpj) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(API_URL + cnpj);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Retorna a resposta JSON
                return new JSONObject(response.toString());
            } else {
                throw new RuntimeException("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during CNPJ consultation", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}