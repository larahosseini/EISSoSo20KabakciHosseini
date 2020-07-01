package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.helper.eissoso20kabakcihosseini.models.Address;
import com.helper.eissoso20kabakcihosseini.models.User;

import java.io.IOException;

public class UserDeserializer extends StdDeserializer<User> {

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    public UserDeserializer() {
        this(null);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String id = node.get("_id").textValue();
        String email = node.get("email").textValue();
        String city = node.get("address").get("city").textValue();
        String street = node.get("address").get("street").textValue();
        String streetNumber = node.get("address").get("streetNumber").textValue();
        int zipcode = (int) node.get("address").get("zipcode").numberValue();
        Address address = new Address(city, street, streetNumber, zipcode);
        return new User(email, address, id);
    }
}
