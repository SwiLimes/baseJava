package com.topjava.webapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.topjava.webapp.model.AbstractSection;

import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AbstractSection.class, new JsonSectionAdapter<>())
            .create();

    public static <T> T read (Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write (T object, Writer writer) {
        GSON.toJson(object, writer);
    }
}
