# Question 3 -  Used a custom deserializer as by default Gson does not handle arbituary precision.

# Project Walkthrough
We have writtten a custom
 * deserializer - CustomStringDeserializer
 * registered this deserializer through - 
   GsonBuilder().registerTypeAdapter(Object.class, new CustomStringDeserializer()).create();

1. Main Method -> Parser.java 
 * Takes a JSON String as input and passes to parseJson where the Deserializer is registered to GsonBuilder and 
   expects the input string to be parsed in its corresponding Object, List, Map or Null.

2. Deserializer -> StringDeserializer.java 
 * takes the Json String and uses JsonDeserializer Interface representing the custom deserializer for JSON
   to Parse the String by checking if -
 * .isJsonObject() then Map<String, Object>
 * .isJsonArray() then ArrayList<> 
 * .isJsonNull() then null
 * .isJsonPrimitive()
    -> .isBoolean() then .getAsBoolean()
    -> .isString() then .getAsString()
    -> .isNumber() then .getAsString()

    * we have put checks to handle arbituary precision for Primitive Type when it .isNumber() where we check if -
      Primitive Value is Big Integer with the Regex Pattern :
        -> .matches("-?\\d+")
    
    or

      Primitive Value is Big Decimal with the Regex Pattern :
        -> .matches("-?\\d*\\.\\d+")
    
      and return the Primitive Value as Big Integer or Big Decimal, respectively.

3. When JSON String .isJsonArray() then we call deserializeList on the String where
 * for every element in the arrayList, we check again for -
    -> if element .isJsonPrimitive()
        -> .isString() then .getAsString()
        -> .isNumber() then .getAsString()

        * we have put checks to handle arbituary precision for Primitive Type when it .isNumber() where we check if -
          Primitive Value is Big Integer with the Regex Pattern :
            -> .matches("-?\\d+")

        or

          Primitive Value is Big Decimal with the Regex Pattern :
            -> .matches("-?\\d*\\.\\d+")

          and add the Primitive Value as String, Big Integer or Big Decimal, respectively in the arrayList.
    -> else
        -> Recursively deserialize

-----------------------------------------------------------------x---------------------------------------------------------------

For Testing
* We passed a hardcoded JsonString -
  {\"name\":\"blox.xyz\",
  \"number\":\"123456789012345678901234567890123456789\",
  \"value\":123456789012345678901234567890.12345678901234567890,",
  \"myList\":[4.56, 7890123456789012345678901234567890],
  \"experience\":{\"level\":\"3\"}
  }

* Got the following result -
  {name=blox.xyz, number=123456789012345678901234567890123456789, value=1.2345678901234568E29, 
  myList=[4.56, 7.890123456789012E33], experience={level=3}}