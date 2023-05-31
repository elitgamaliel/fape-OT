package com.inretailpharma.digital.ordertracker.utils;

import com.google.gson.Gson;

public class ConvertToUtils {
    private ConvertToUtils() {
    }

    private static final Gson GSON = new Gson();

    public static String convertFromObjectToJson(Object object) {
        return GSON.toJson(object);
    }
}
