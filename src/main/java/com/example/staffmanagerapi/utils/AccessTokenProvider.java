package com.example.staffmanagerapi.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AccessTokenProvider {
    private static String adminAccessToken;
    private static String collaboratorAccessToken;
    private static String ADMIN_API_URL = "http://ci.check-consulting.net:10000/auth/realms/staff-manager-admin/protocol/openid-connect/token";
    private static String COLLABORATOR_API_URL = "http://ci.check-consulting.net:10000/auth/realms/staff-manager-collab/protocol/openid-connect/token";
    private static Gson gson = new Gson();

    public static String getAdminAccessToken(String username, String password) {

        try {
            // setup

            /*

            // old code, not working ( why ? )

            AccessTokenRequestBody body = new AccessTokenRequestBody(username, password);
            String jsonBody = gson.toJson(body);
            log.info("Sending body : {}",jsonBody);

            */

            // using verbose method to create the body , com.google.gson.Gson failed !
            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("username", username);
            bodyParams.put("password", password);
            bodyParams.put("grant_type", "password");
            bodyParams.put("client_id", "postman-client-id");

            String encodedBody = bodyParams.entrySet()
                    .stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
            log.info("Sending body : {}", encodedBody);

            // create http request
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(ADMIN_API_URL))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(encodedBody))
                    .build();

            // send it
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> response = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            // Parse the JSON response
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

            // Extract the access_token
            // Done
            // log.info("Access Token: {}", jsonResponse.get("access_token").getAsString());
            return jsonResponse.get("access_token").getAsString();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getCollaboratorAccessToken(String username, String password) {
        return "a faire";
    }
}