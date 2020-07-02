package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.helper.eissoso20kabakcihosseini.models.Address;
import com.helper.eissoso20kabakcihosseini.models.User;

import java.io.IOException;

public class UserDeserializer extends StdDeserializer<User> {

    public UserDeserializer() {
        this(User.class);
    }

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String id = "";
        String email = "";
        String city = "";
        String street = "";
        String streetNumber = "";
        int zipcode = -1;

        if (node.get("_id") != null) {
            id = node.get("_id").asText();
            System.out.println("ID: " + id);
        }
        if (node.get("email") != null) {
            email = node.get("email").asText();
        }
        if (node.get("address").get("city") != null) {
            city = node.get("address").get("city").asText();
        }
        if (node.get("address").get("street") != null) {
            street = node.get("address").get("street").asText();
        }
        if (node.get("address").get("streetNumber") != null) {
            streetNumber = node.get("address").get("streetNumber").asText();
        }
        if (node.get("address").get("zipcode") != null) {
            zipcode = node.get("address").get("zipcode").asInt();
        }
        Address address = new Address(city, street, streetNumber, zipcode);
        return new User(email, address, id);
    }
}
