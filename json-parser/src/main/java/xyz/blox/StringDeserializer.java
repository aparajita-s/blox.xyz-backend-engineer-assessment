package xyz.blox;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class StringDeserializer {

    static class CustomStringDeserializer implements JsonDeserializer<Object> {

        // Method to deserialize a JSON Array to a List of Objects
        public List<Object> deserializeList(JsonArray jsonArray, JsonDeserializationContext context) {
            List<Object> list = new java.util.ArrayList<>();
            
            for (JsonElement element : jsonArray) {
                System.out.println(element + " (" + element.getClass().getSimpleName() + ")");

                if (element.isJsonPrimitive()) {
                    JsonPrimitive primitive = element.getAsJsonPrimitive();

                    if (primitive.isNumber()) {
                        String numberStr = primitive.getAsString();

                        if (numberStr.matches("-?\\d+")) { // Integer number
                            list.add(new BigInteger(numberStr));
                        } else if (numberStr.matches("-?\\d*\\.\\d+")) { // Decimal number
                            list.add(new BigDecimal(numberStr));
                        } else {
                            // If the number is in a scientific format or some other number format
                            try {
                                list.add(new BigDecimal(numberStr));
                            } catch (NumberFormatException e) {
                                list.add(numberStr); // Default fallback
                            }
                        }
                    } else {
                        list.add(primitive.getAsString()); // String value
                    }
                } else {
                    list.add(context.deserialize(element, Object.class)); // Recursively deserialize
                }
            }
            return list;
        }

        @Override
        public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

            // Handle JSON Map deserialization
            if (jsonElement.isJsonObject()) {
                System.out.println("isJsonObject");
                return context.deserialize(jsonElement, new TypeToken<Map<String, Object>>() {}.getType());
            } 
            
            // Handle JSON List deserialization
            else if (jsonElement.isJsonArray()) {
                System.out.println("isJsonArray");
                return deserializeList(jsonElement.getAsJsonArray(), context);
            }
            
            // Handle JSON Primitive deserialization (Boolean, String, BigDecimal, BigInteger, or Null)
            else if (jsonElement.isJsonPrimitive()) {
                System.out.println("isJsonPrimitive");
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

                if (jsonPrimitive.isBoolean()) {
                    return jsonPrimitive.getAsBoolean();
                } else if (jsonPrimitive.isString()) {
                    return jsonPrimitive.getAsString();
                } else if (jsonPrimitive.isNumber()) {
                    String numberStr = jsonPrimitive.getAsString();

                    if (numberStr.matches("-?\\d+")) {
                        return new BigInteger(numberStr);
                    } else if (numberStr.matches("-?\\d*\\.\\d+")) {
                        return new BigDecimal(numberStr);
                    } else {
                        try {
                            return jsonPrimitive.getAsInt();
                        } catch (NumberFormatException e) {
                            return numberStr; // Fallback if the number format is not recognized
                        }
                    }
                } else if (jsonPrimitive.isJsonNull()) {
                    return null;
                }
            }

            // If none of the above cases match, throw an error
            throw new JsonParseException("Unsupported JSON element: " + jsonElement);
        }
    }
}