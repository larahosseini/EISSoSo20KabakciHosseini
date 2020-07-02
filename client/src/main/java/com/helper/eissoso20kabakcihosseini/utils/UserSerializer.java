package com.helper.eissoso20kabakcihosseini.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.helper.eissoso20kabakcihosseini.models.User;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {


    protected UserSerializer(Class<User> t) {
        super(t);
    }

    public UserSerializer() {
        super(User.class);
    }

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeObjectField("address", user.getAddress());
    }
}
