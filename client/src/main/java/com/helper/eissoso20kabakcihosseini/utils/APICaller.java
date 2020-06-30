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
import java.util.Map;

public class APICaller {

    public static int statusCode = -1;
    public static String message = "";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Task<Void> createAccount(String email, String password, String city, String street, String streetNumber, Integer zipcode) throws JsonProcessingException {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Address address = new Address(city, street, streetNumber, zipcode);
                UserRegistration registration = new UserRegistration(email, password, address);
                String jsonData = mapper.writeValueAsString(registration);
                System.out.println(jsonData);

                // HttpClient für das Senden von Anfragen
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .uri(URI.create(URLs.CREATE_USER))
                        .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                Map<?, ?> map = mapper.readValue(response.body(), Map.class);

                statusCode = response.statusCode();
                message = (String) map.get("message");

                System.out.println("Status: " + response.statusCode());
                System.out.println("Message: " + message);

                updateMessage(statusCode + "," + message);

                return null;
            }
        };
    }

    public static Task<Void> activateAccount(String activationLink) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .GET()
                        .uri(URI.create(activationLink))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                ObjectMapper mapper = new ObjectMapper();
                Map<?, ?> map = mapper.readValue(response.body(), Map.class);
                message = (String) map.get("message");
                statusCode = response.statusCode();
                updateMessage(statusCode + "," + message);
                return null;
            }
        };
    }

    public static Task<Void> login(String email, String password) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                Login login = new Login();
                login.setEmail(email);
                login.setPassword(password);
                String jsonData = mapper.writeValueAsString(login);

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                        .uri(URI.create(URLs.LOGIN))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                // parsing data
                String body = response.body();
                System.out.println(body);
                Reader reader = new StringReader(body);
                ObjectNode rootNode = mapper.readValue(reader, ObjectNode.class);
                JsonNode tokenNode = rootNode.get("token");
                // saving token in file
                SessionHandler sessionHandler = new SessionHandler();
                sessionHandler.createSession(email, tokenNode.asText());

                updateMessage(response.statusCode() + "," + tokenNode.asText());

                return null;
            }
        };
    }

    public static Task<Void> getProfile(String id) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .GET()
                        .uri(URI.create(URLs.GET_USERS + "/" + id))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                Map<?, ?> map = mapper.readValue(response.body(), Map.class);
                return null;
            }
        };
    }
}
