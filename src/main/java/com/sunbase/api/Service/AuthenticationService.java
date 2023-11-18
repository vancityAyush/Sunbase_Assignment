package com.sunbase.api.Service;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationService {
    private RestTemplate restTemplate;
    private final String authApiUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment_auth.jsp";

    public String getAccessToken(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate = new RestTemplate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("login_id", username);
        jsonObject.put("password", password);

        HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(authApiUrl, HttpMethod.POST, requestEntity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        String accessToken = jsonResponse.getString("access_token");

        return accessToken;
    }
}
