package com.alten.ambroise.forum.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

public class Mobility implements Parcelable {
    private String geographic;
    private int radius;
    private String unit;
    @Ignore
    private String tag;

    public Mobility(){

    }

    protected Mobility(Parcel in) {
        geographic = in.readString();
        radius = in.readInt();
        unit = in.readString();
    }

    public static final Creator<Mobility> CREATOR = new Creator<Mobility>() {
        @Override
        public Mobility createFromParcel(Parcel in) {
            return new Mobility(in);
        }

        @Override
        public Mobility[] newArray(int size) {
            return new Mobility[size];
        }
    };

    public String getGeographic() {
        return geographic;
    }

    public void setGeographic(String geographic) {
        this.geographic = geographic;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(geographic);
        dest.writeInt(radius);
        dest.writeString(unit);
    }

    @Override
    public String toString(){
        return radius > 0 ? geographic+", within "+radius+unit : geographic;
    }
}
