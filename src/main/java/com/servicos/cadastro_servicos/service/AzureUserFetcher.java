package com.servicos.cadastro_servicos.service;



import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.servicos.cadastro_servicos.Security.AzureConfig;
import com.servicos.cadastro_servicos.model.AzureUser;
@Component
public class AzureUserFetcher {
    private final String TENANT_ID;
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    private final Cache<String, List<AzureUser>> userCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Autowired
    public AzureUserFetcher(AzureConfig azureConfig) {
        this.TENANT_ID = azureConfig.getTenantId();
        this.CLIENT_ID = azureConfig.getClientId();
        this.CLIENT_SECRET = azureConfig.getClientSecret();
    }

    public List<AzureUser> getUsers() throws Exception {
        // Obtém a lista de usuários do cache, ou busca da API se não estiver no cache
        return userCache.get("users", k -> {
            try {
                return fetchUsers(getAccessToken());
            } catch (Exception e) {
                throw new RuntimeException("Erro ao buscar usuários", e);
            }
        });
    }

    public String getAccessToken() throws Exception {
        String tokenUrl = "https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/token";
        
        String requestBody = "grant_type=client_credentials" +
                             "&client_id=" + CLIENT_ID +
                             "&client_secret=" + CLIENT_SECRET +
                             "&scope=https://graph.microsoft.com/.default";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonResponse = new JSONObject(response.body());
        return jsonResponse.optString("access_token", null);
    }

    public List<AzureUser> fetchUsers(String accessToken) throws Exception {
        List<AzureUser> userList = new ArrayList<>();
        String usersUrl = "https://graph.microsoft.com/v1.0/users?$select=id,displayName,mail,officeLocation";
        
        HttpClient client = HttpClient.newHttpClient();
        while (usersUrl != null) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(usersUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());

            JSONArray users = jsonResponse.optJSONArray("value");
            if (users != null) {
                for (int i = 0; i < users.length(); i++) {
                    JSONObject userJson = users.getJSONObject(i);
                    String id = userJson.optString("id");
                    String displayName = userJson.optString("displayName");
                    String mail = userJson.optString("mail");
                    String officeLocation = userJson.optString("officeLocation", null);

                    if (officeLocation != null && !officeLocation.isEmpty()) {
                        String formattedName = displayName + " - " + officeLocation;
                        userList.add(new AzureUser(id, formattedName, mail));
                    }
                }
            }

            usersUrl = jsonResponse.optString("@odata.nextLink", null);
        }
        return userList;
    }
}