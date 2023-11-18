package com.sunbase.api.Service;

import com.sunbase.api.Entity.Customer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final String getCustomersUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list";
    private final String addCustomersUrl = "https://qa2.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=create";

    @Autowired
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> addCustomer(Customer customer, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", customer.getFirst_name());
        jsonObject.put("last_name", customer.getLast_name());
        jsonObject.put("street", customer.getStreet());
        jsonObject.put("address", customer.getAddress());
        jsonObject.put("city", customer.getCity());
        jsonObject.put("state", customer.getState());
        jsonObject.put("email", customer.getEmail());
        jsonObject.put("phone", customer.getPhone());

        HttpEntity<?> entity = new HttpEntity<>(jsonObject.toString(), headers);

        return restTemplate.exchange(addCustomersUrl, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<Customer[]> getCustomersList(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(getCustomersUrl, HttpMethod.GET, entity, Customer[].class);
    }

    public ResponseEntity<String> deleteCustomer(String deleteURL, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(deleteURL, HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<String> updateCustomer(Customer customer, String updateUrl, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", customer.getFirst_name());
        jsonObject.put("last_name", customer.getLast_name());
        jsonObject.put("street", customer.getStreet());
        jsonObject.put("address", customer.getAddress());
        jsonObject.put("city", customer.getCity());
        jsonObject.put("state", customer.getState());
        jsonObject.put("email", customer.getEmail());
        jsonObject.put("phone", customer.getPhone());

        HttpEntity<?> entity = new HttpEntity<>(jsonObject.toString(), headers);

        return restTemplate.exchange(updateUrl, HttpMethod.POST, entity, String.class);
    }

}
