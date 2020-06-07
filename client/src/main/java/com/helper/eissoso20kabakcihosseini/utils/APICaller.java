package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.helper.eissoso20kabakcihosseini.models.Address;
import com.helper.eissoso20kabakcihosseini.models.UserRegistration;
import javafx.concurrent.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APICaller {

    public static int statusCode = -1;


    public static void createUser(String email, String password, String city, String street, String streetNumber, Integer zipcode) throws JsonProcessingException {
         Task<Void> task = new Task<Void>() {
            ObjectMapper mapper = new ObjectMapper();
            String newUserUrl = "http://localhost:3000/api/users/signup";

            @Override
            protected Void call() throws Exception {

                Address address = new Address(city, street, streetNumber, zipcode);
                UserRegistration registration = new UserRegistration(email, password, address);
                String jsonData = mapper.writeValueAsString(registration);

                System.out.println(jsonData);
                HttpClient client = HttpClient.newHttpClient();


                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .uri(URI.create(newUserUrl))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Status: " + response.statusCode());
                System.out.println(response.body());
                statusCode = response.statusCode();
                return null;
            }
        };
        new Thread(task).start();
    }
}
