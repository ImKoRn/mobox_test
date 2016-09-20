package com.korn.im.testapp.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.korn.im.testapp.pojo.Photo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by korn on 15.09.16.
 */
public class PhotosDeserializer implements JsonDeserializer<List<Photo>> {
    private static final String ITEMS = "items";

    @Override
    public List<Photo> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();
        List<Photo> list = new ArrayList<>(root.getAsJsonArray(ITEMS).size());
        for (JsonElement jsonElement : root.getAsJsonArray(ITEMS))
            list.add(new Photo(jsonElement.getAsJsonObject()));
        return list;
    }
}
