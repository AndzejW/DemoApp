package com.andrzej.demoapplication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Andrzej on 04.02.2017.
 */

public class Element implements Parcelable{
    public static final String ELEMENT_TAG="ELEMENT_TAG";

    @SerializedName("name")
    String title;
    String description;
    String language;
    @SerializedName("watchers_count")
    int watchers;
    @SerializedName("forks_count")
    int forks;
    @SerializedName("updated_at")
    String lastUpdate;

    private Element(Parcel in) {
        title = in.readString();
        description = in.readString();
        language = in.readString();
        watchers = in.readInt();
        forks = in.readInt();
        lastUpdate = in.readString();
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(language);
        parcel.writeInt(watchers);
        parcel.writeInt(forks);
        parcel.writeString(lastUpdate);
    }
}
