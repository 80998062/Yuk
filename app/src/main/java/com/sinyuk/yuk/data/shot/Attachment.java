package com.sinyuk.yuk.data.shot;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sinyuk on 16/9/9.
 */
public class Attachment implements Parcelable {

    /**
     * id : 206165
     * url : https://d13yacurqjgara.cloudfront.net/users/1/screenshots/1412410/attachments/206165/weathered-ball-detail.jpg
     * thumbnail_url : https://d13yacurqjgara.cloudfront.net/users/1/screenshots/1412410/attachments/206165/thumbnail/weathered-ball-detail.jpg
     * size : 116375
     * content_type : image/jpeg
     * views_count : 325
     * created_at : 2014-02-07T16:35:09Z
     */

    private long id;
    private String url;
    @SerializedName("thumbnail_url")
    private String thumbnailUrl;
    private long size;
    @SerializedName("content_type")
    private String contentType;
    @SerializedName("views_count")
    private int viewsCount;
    @SerializedName("created_at")
    private String createdAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.url);
        dest.writeString(this.thumbnailUrl);
        dest.writeLong(this.size);
        dest.writeString(this.contentType);
        dest.writeInt(this.viewsCount);
        dest.writeString(this.createdAt);
    }

    public Attachment() {
    }

    protected Attachment(Parcel in) {
        this.id = in.readLong();
        this.url = in.readString();
        this.thumbnailUrl = in.readString();
        this.size = in.readLong();
        this.contentType = in.readString();
        this.viewsCount = in.readInt();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel source) {
            return new Attachment(source);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", size=" + size +
                ", contentType='" + contentType + '\'' +
                ", viewsCount=" + viewsCount +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
