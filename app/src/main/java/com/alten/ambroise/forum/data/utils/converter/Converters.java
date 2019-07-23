package com.alten.ambroise.forum.data.utils.converter;

import androidx.room.TypeConverter;

import com.alten.ambroise.forum.data.utils.Nationality;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromList(List<String> list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }

    @TypeConverter
    public List<String> toList(String listString) {
        if (listString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.fromJson(listString, type);
    }

    @TypeConverter
    public String fromNationality(Nationality nationality) {
        if (nationality == null) {
            return (null);
        }
        return nationality.name();
    }

    @TypeConverter
    public Nationality toNationality(String nationalityString) {
        if (nationalityString == null) {
            return (null);
        }
        return Nationality.valueOf(nationalityString);
    }
}
