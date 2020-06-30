package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.helper.eissoso20kabakcihosseini.models.Session;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

public class SessionHandler {

    private static String configDir = "config";
    private static String fileName = "session.json";
    private String token = "";

    public void createSession(String email, String token) {
        // ordner config
        File configFile = new File(configDir);

        if (!configFile.exists()) {
            configFile.mkdir();
        }

        String filePath = configDir + "/" + fileName;
        FileWriter writer = null;
        ObjectMapper mapper = new ObjectMapper();
        Session session = new Session(email, token);

        String jsonString = "";

        try {
            writer = new FileWriter(filePath);
            jsonString = mapper.writeValueAsString(session);
            writer.write(jsonString);
            writer.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Session getSession() {
        String filePath = configDir + "/" + fileName;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(Paths.get(filePath).toFile(), Map.class);
            String token = (String) map.get("token");
            String email = (String) map.get("email");
            System.out.println("Email: " + email + ", Token: " + token);
            return new Session(email, token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
