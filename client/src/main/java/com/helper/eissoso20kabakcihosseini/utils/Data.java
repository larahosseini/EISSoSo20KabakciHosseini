package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helper.eissoso20kabakcihosseini.models.User;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

public class Data {
    private static final String CONFIG_DIR = "config";

    public static void saveUser(User user) {
        String filename = String.format("data-%s.json", user.getEmail());
        String filePath = CONFIG_DIR + "/" + filename;
        FileWriter writer = null;
        ObjectMapper mapper = new ObjectMapper();

        createConfigDir();

        try {
            writer = new FileWriter(filePath);
            String json = mapper.writeValueAsString(user);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User getUserData(String email) throws IOException {
        createConfigDir();

        ObjectMapper mapper = new ObjectMapper();
        String filename = String.format("data-%s.json", email);
        File configDir = new File(CONFIG_DIR);

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().contains(email);
            }
        };

        File[] files = configDir.listFiles(filter);
        if(files == null) throw new IOException("No User Data Found");
        for (File file : files) {
            if (file.getName().contains(email)) {
                User user = mapper.readValue(file, User.class);
                System.out.println(user);
                return user;
            }
        }
        return null;
    }

    private static void createConfigDir() {
        File configFile = new File(CONFIG_DIR);

        if (!configFile.exists()) {
            configFile.mkdir();
        }
    }
}
