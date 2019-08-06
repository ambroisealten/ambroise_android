package com.alten.ambroise.forum.utils.converter;

import androidx.room.TypeConverter;

import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.utils.Nationality;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromList(List list) {
        if (list == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List>() {
        }.getType();
        return gson.toJson(list, type);
    }

    @TypeConverter
    public List toList(String listString) {
        if (listString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List>() {
        }.getType();
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

    @TypeConverter
    public String fromMobility(Mobility mobility) {
        if (mobility == null) {
            return (null);
        }
        Gson gson = new Gson();
        return gson.toJson(mobility,Mobility.class);
    }

    @TypeConverter
    public Mobility toMobility(String mobilityString) {
        if (mobilityString == null) {
            return (null);
        }
        Gson gson = new Gson();
        return gson.fromJson(mobilityString,Mobility.class);
    }
}
