package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.helper.eissoso20kabakcihosseini.models.*;
import javafx.concurrent.Task;

import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

                Map<String, String> map = getResponseData(request);
                Map<?, ?> data = mapper.readValue(map.get("body"), Map.class);

                statusCode = Integer.parseInt(map.get("statuscode"));
                message = (String) data.get("message");
                System.out.println(statusCode + " ---- " + message);
                updateMessage(statusCode + "," + message);

                return null;
            }
        };
    }

    public static Task<Void> activateAccount(String activationLink) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Map<String, String> activateAccountResults = verifyAccount(activationLink);
                Map<?, ?> activationJSONMap = mapper.readValue(activateAccountResults.get("body"), Map.class);

                message = (String) activationJSONMap.get("message");
                statusCode = Integer.parseInt(activateAccountResults.get("statuscode"));
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

                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                        .uri(URI.create(URLs.LOGIN))
                        .build();
                Map<String, String> map = getResponseData(request);

                String body = map.get("body");
                Reader reader = new StringReader(body);
                ObjectNode rootNode = mapper.readValue(reader, ObjectNode.class);
                JsonNode tokenNode = rootNode.get("token");
                // saving token in file
                SessionHandler sessionHandler = new SessionHandler();
                sessionHandler.createSession(email, tokenNode.asText());

                updateMessage(map.get("statuscode") + "," + tokenNode.asText());

                return null;
            }
        };
    }

    private static Map<String, String> verifyAccount(String activationLink) throws InterruptedException, ExecutionException, TimeoutException {
        HttpRequest activateAccountRequest = HttpRequest.newBuilder()
                .header("Content-Type", "application/json; charset=utf-8")
                .GET()
                .uri(URI.create(activationLink))
                .build();
        return getResponseData(activateAccountRequest);
    }

    public static Task<Void> deleteAccount(String id) throws InterruptedException, ExecutionException, TimeoutException {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HttpRequest deleteRequest = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .DELETE()
                        .uri(URI.create(URLs.DELETE_USER + "/" +id))
                        .build();
                Map<String, String> data = getResponseData(deleteRequest);
                updateMessage(data.get("statuscode"));
                return null;
            }
        };
    }

    public static Task<Void> getProfile(String id) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HttpRequest profileRequest = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .GET()
                        .uri(URI.create(URLs.GET_USERS + "/" + id))
                        .build();
                Map<String, String> responseMap = getResponseData(profileRequest);
                String body = responseMap.get("body");
                String statuscode = responseMap.get("statuscode");
                updateMessage(statuscode+";" +body);

                return null;
            }
        };
    }

    public static Task<Void> updateUser(User user){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Session session = SessionHandler.getSession();
                ObjectMapper updateMapper = new ObjectMapper();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addSerializer(User.class, new UserSerializer());
                updateMapper.registerModule(simpleModule);
                String json = updateMapper.writeValueAsString(user);
                System.out.println(json);
                HttpRequest updateRequest = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .header("Authorization", "Bearer " + session.getToken())
                        .PUT(HttpRequest.BodyPublishers.ofString(json))
                        .uri(URI.create(URLs.UPDATE_USER + "/" + user.getId()))
                        .build();
                Map<String, String> data = getResponseData(updateRequest);
                System.out.println("Message: " + data.get("body"));
                updateMessage(data.get("statuscode"));
                return null;
            }
        };
    }

    private static Map<String, String> getResponseData(HttpRequest request) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        String body = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        Map<String, String> map = new HashMap<>();
        map.put("statuscode", String.valueOf(response.get().statusCode()));
        map.put("body", body);
        return map;
    }
}
