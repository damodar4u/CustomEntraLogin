package com.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TokenUtils {

    private static final String TOKEN_ENDPOINT_TEMPLATE = "https://login.microsoftonline.com/%s/oauth2/v2.0/token";

    public static String getTokenFromAzureAD(String username, String password, String clientId, String clientSecret, String tenantId) throws Exception {
        // Prepare the token endpoint URL
        String tokenEndpoint = String.format(TOKEN_ENDPOINT_TEMPLATE, tenantId);

        // Create the POST request body
        String requestBody = "grant_type=password"
                + "&client_id=" + URLEncoder.encode(clientId, "UTF-8")
                + "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8")
                + "&scope=" + URLEncoder.encode("User.Read openid profile", "UTF-8")
                + "&username=" + URLEncoder.encode(username, "UTF-8")
                + "&password=" + URLEncoder.encode(password, "UTF-8");

        // Open a connection to the token endpoint
        URL url = new URL(tokenEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes("UTF-8"));
        }

        // Read the response
        int responseCode = connection.getResponseCode();
        BufferedReader reader;
        if (responseCode == 200) {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        connection.disconnect();
        return response.toString();
    }
}

