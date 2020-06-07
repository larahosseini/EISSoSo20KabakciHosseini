package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.helper.eissoso20kabakcihosseini.models.Address;
import com.helper.eissoso20kabakcihosseini.models.Login;
import com.helper.eissoso20kabakcihosseini.models.UserRegistration;
import javafx.concurrent.Task;

import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APICaller {

    public static int statusCode = -1;
    public static String message = "";
    private String token = "";


    public static void createUser(String email, String password, String city, String street, String streetNumber, Integer zipcode) throws JsonProcessingException {
        Task<String> task = new Task<String>() {
            @Override
            protected String call() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                String newUserUrl = "http://localhost:3000/api/users/signup";

                Address address = new Address(city, street, streetNumber, zipcode);
                UserRegistration registration = new UserRegistration(email, password, address);
                String jsonData = mapper.writeValueAsString(registration);
                System.out.println(jsonData);

                // HttpClient für das Senden von Anfragen
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
                message = response.body();
                return response.body();
            }
        };
        new Thread(task).start();
    }

    public static void activateAccount(String activationLink) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .GET()
                        .uri(URI.create(activationLink))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());
                return null;
            }
        };
        new Thread(task).start();
    }

    public static void login(String email, String password) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                Login login = new Login();
                login.setEmail(email);
                login.setPassword(password);
                String jsonData = mapper.writeValueAsString(login);
                String loginUrl = "http://localhost:3000/login";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                        .uri(URI.create(loginUrl))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String body = response.body();
                Reader reader = new StringReader(body);
                ObjectNode rootNode = mapper.readValue(reader, ObjectNode.class);
                JsonNode tokenNode = rootNode.get("token");
                System.out.println("Token: " + tokenNode.asText());


                Session session = new Session();
                session.createSession(tokenNode.asText());

                System.out.println(Session.getSession());
                return null;
            }
        };
        new Thread(task).start();
    }
}
