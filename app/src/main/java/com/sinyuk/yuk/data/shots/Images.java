package com.sinyuk.yuk.data.shots;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sinyuk on 16/7/6.
 */
public class Images implements Parcelable {

    @SerializedName("hidpi")
    private String mHidpi;
    @SerializedName("normal")
    private String mNormal;
    @SerializedName("teaser")
    private String mTeaser;

    public String getHidpi() { return mHidpi;}

    public void setHidpi(String hidpi) { mHidpi = hidpi;}

    public String getNormal() { return mNormal;}

    public void setNormal(String normal) { mNormal = normal;}

    public String getTeaser() { return mTeaser;}

    public void setTeaser(String teaser) { mTeaser = teaser;}


    public String buildUrl(int width, int height) {
        return "";
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mHidpi);
        dest.writeString(this.mNormal);
        dest.writeString(this.mTeaser);
    }

    public Images() {}

    protected Images(Parcel in) {
        this.mHidpi = in.readString();
        this.mNormal = in.readString();
        this.mTeaser = in.readString();
    }

    public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel source) {return new Images(source);}

        @Override
        public Images[] newArray(int size) {return new Images[size];}
    };
}
