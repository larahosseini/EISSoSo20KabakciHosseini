package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;

public class Session {

    private static String configDir = "config";
    private static String fileName = "session.json";
    private String token = "";

    public void createSession(String token) {
        // ordner config
        File configFile = new File(configDir);

        if (!configFile.exists()) {
            configFile.mkdir();
        }

        String filePath = configDir + "/" + fileName;
        FileWriter writer = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode tokenNode = mapper.createObjectNode();

        ((ObjectNode) tokenNode).put("token", token);

        String jsonString = "";

        try {
            writer = new FileWriter(filePath);
            jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tokenNode);
            System.out.println("Writing token to file: " + jsonString);
            writer.write(jsonString);
            writer.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSession(){
        // ordner config
        File configFile = new File(configDir);

        String filePath = configDir + "/" + fileName;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<?, ?> map = mapper.readValue(Paths.get(filePath).toFile(), Map.class);
            String token = (String) map.get("token");
            System.out.println(token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
