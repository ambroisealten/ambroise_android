package com.alten.ambroise.forum.data.model.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "document_table", indices = {@Index(value = "_id", unique = true)})
public class Document implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public Long _id;
    @NonNull
    public String title;
    @NonNull
    public String uri;

    public static final Creator<Document> CREATOR = new Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

    public Document() {
    }

    public Long get_id() {
        return _id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getUri() {
        return uri;
    }

    public void set_id(final Long _id) {
        this._id = _id;
    }

    public void setTitle(@NonNull final String title) {
        this.title = title;
    }

    public void setUri(@NonNull final String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Document(Parcel in) {
        if (in.readByte() == 0) {
            _id = null;
        } else {
            _id = in.readLong();
        }
        title = in.readString();
        uri = in.readString();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        if (_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(_id);
        }
        dest.writeString(title);
        dest.writeString(uri);
    }
}
