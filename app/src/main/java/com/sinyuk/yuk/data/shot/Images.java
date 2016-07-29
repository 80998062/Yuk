package com.sinyuk.yuk.data.shot;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sinyuk on 16/7/29.
 */
public class Images implements Parcelable{

    @SerializedName("hidpi")
    private String hidpi;
    @SerializedName("normal")
    private String normal;
    @SerializedName("teaser")
    private String teaser;

    public Images(String hidpi, String normal, String teaser) {
        this.hidpi = hidpi;
        this.normal = normal;
        this.teaser = teaser;
    }

    protected Images(Parcel in) {
        hidpi = in.readString();
        normal = in.readString();
        teaser = in.readString();
    }

    public String bestQuality() {
        return !TextUtils.isEmpty(hidpi) ? hidpi : normal;
    }

    public String normalQuality() {
        return !TextUtils.isEmpty(normal) ? normal : teaser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hidpi);
        dest.writeString(normal);
        dest.writeString(teaser);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };

    public String getHidpi() {
        return hidpi;
    }

    public String getNormal() {
        return normal;
    }

    public String getTeaser() {
        return teaser;
    }

    public static Creator<Images> getCREATOR() {
        return CREATOR;
    }
}
