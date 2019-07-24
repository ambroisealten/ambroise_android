package com.alten.ambroise.forum.data.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "forum_table")
public class Forum implements Parcelable {


    public static final Creator<Forum> CREATOR = new Creator<Forum>() {
        @Override
        public Forum createFromParcel(Parcel in) {
            return new Forum(in);
        }

        @Override
        public Forum[] newArray(int size) {
            return new Forum[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    private long _id;
    @NonNull
    private String name;
    @NonNull
    private String date;
    @NonNull
    private String place;

    public Forum() {
    }

    protected Forum(Parcel in) {
        _id = in.readLong();
        name = Objects.requireNonNull(in.readString());
        date = Objects.requireNonNull(in.readString());
        place = Objects.requireNonNull(in.readString());
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(place);
    }
}
