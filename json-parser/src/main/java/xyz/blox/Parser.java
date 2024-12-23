package xyz.blox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import xyz.blox.StringDeserializer.CustomStringDeserializer;

public class Parser {

    public static Object parseJson (String str) throws JsonSyntaxException{
        Gson gson = new GsonBuilder().registerTypeAdapter(Object.class, new CustomStringDeserializer()).create();

        return gson.fromJson(str, Object.class);
    }

    public static void main(String[] args) {
        String jsonString = "{\"name\":\"blox.xyz\",\"number\":\"123456789012345678901234567890123456789\",\"value\":123456789012345678901234567890.12345678901234567890," +
                            "\"myList\":[4.56, 7890123456789012345678901234567890],\"experience\":{\"level\":\"3\"}}";
        
        try {
            Object result = parseJson(jsonString);
            System.out.println(result);
        }
        catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}
