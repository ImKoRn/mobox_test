package com.korn.im.testapp.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created by korn on 15.09.16.
 */
public class Photo implements Parcelable{
    private static final String IMAGE = "media";
    private static final String TITLE = "title";

    public final String imageSource;
    public final String imageDescription;

    public Photo(String imageSource, String imageDescription) {
        this.imageSource = imageSource;
        this.imageDescription = imageDescription;
    }

    public Photo(JsonObject asJsonObject) {
        imageSource = asJsonObject.getAsJsonObject(IMAGE).get("m").getAsString();
        imageDescription = asJsonObject.get(TITLE).getAsString();
    }

    public Photo(Parcel parcel) {
        imageSource = parcel.readString();
        imageDescription = parcel.readString();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Photo) {
            return ((Photo) obj).imageDescription.equals(imageDescription) &&
                    ((Photo) obj).imageSource.equals(imageSource);
        }

        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageSource);
        parcel.writeString(imageDescription);
    }

    public final static Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel parcel) {
            return new Photo(parcel);
        }

        @Override
        public Photo[] newArray(int i) {
            return new Photo[i];
        }
    };
}
