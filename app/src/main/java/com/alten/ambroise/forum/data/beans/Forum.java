package com.alten.ambroise.forum.data.beans;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "forum_table")
public class Forum {

    @PrimaryKey(autoGenerate = true)
    private long _id;
    @NonNull
    private String name;
    @NonNull
    private String date;
    @NonNull
    private String place;

    @NonNull
    public long get_id() {
        return _id;
    }

    public void set_id(@NonNull long _id) {
        this._id = _id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getPlace() {
        return place;
    }

    public void setPlace(@NonNull String place) {
        this.place = place;
    }
}
