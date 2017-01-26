package com.example.lobna.everydayNews.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lobna on 26-Jan-17.
 */

public class Source implements Parcelable {
    private String id;

    public Source(){}
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected Source(Parcel in) {
        id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Source> CREATOR = new Parcelable.Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };
}
