package com.sinyuk.yuk.data.shot;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.data.team.Team;
import com.sinyuk.yuk.data.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sinyuk on 16.6.16.
 */
public class Shot implements Parcelable {

    public static final Creator<Shot> CREATOR = new Creator<Shot>() {
        @Override
        public Shot createFromParcel(Parcel source) {
            return new Shot(source);
        }

        @Override
        public Shot[] newArray(int size) {
            return new Shot[size];
        }
    };
    @SerializedName("id")
    private final int id;
    @SerializedName("title")
    private final String title;
    @SerializedName("description")
    private final String description;
    @SerializedName("width")
    private final int width;
    @SerializedName("height")
    private final int height;
    @SerializedName("images")
    private final Images images;
    @SerializedName("views_count")
    private final int viewsCount;
    @SerializedName("likes_count")
    private final int likesCount;
    @SerializedName("comments_count")
    private final int commentsCount;
    @SerializedName("attachments_count")
    private final int attachmentsCount;
    @SerializedName("rebounds_count")
    private final int reboundsCount;
    @SerializedName("buckets_count")
    private final int bucketsCount;
    @SerializedName("created_at")
    private final String createdAt;
    @SerializedName("updated_at")
    private final String updatedAt;
    @SerializedName("html_url")
    private final String htmlUrl;
    @SerializedName("attachments_url")
    private final String attachmentsUrl;
    @SerializedName("buckets_url")
    private final String bucketsUrl;
    @SerializedName("comments_url")
    private final String commentsUrl;
    @SerializedName("likes_url")
    private final String likesUrl;
    @SerializedName("projects_url")
    private final String projectsUrl;
    @SerializedName("rebounds_url")
    private final String reboundsUrl;
    @SerializedName("animated")
    private final boolean animated;
    @SerializedName("tags")
    private final ArrayList<String> tags;
    @SerializedName("user")
    private final User user;
    @SerializedName("team")
    private final Team team;
    /**
     * extras
     */
    private boolean hasFadedIn = false;

    public Shot(int id, String title, String description, int width, int height, Images images, int viewsCount,
                int likesCount, int commentsCount, int attachmentsCount, int reboundsCount, int bucketsCount, String createdAt, String updatedAt, String htmlUrl, String attachmentsUrl, String bucketsUrl, String commentsUrl, String likesUrl, String projectsUrl,
                String reboundsUrl, boolean animated, ArrayList<String> tags, User user, Team team) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.width = width;
        this.height = height;
        this.images = images;
        this.viewsCount = viewsCount;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.attachmentsCount = attachmentsCount;
        this.reboundsCount = reboundsCount;
        this.bucketsCount = bucketsCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.htmlUrl = htmlUrl;
        this.attachmentsUrl = attachmentsUrl;
        this.bucketsUrl = bucketsUrl;
        this.commentsUrl = commentsUrl;
        this.likesUrl = likesUrl;
        this.projectsUrl = projectsUrl;
        this.reboundsUrl = reboundsUrl;
        this.animated = animated;
        this.tags = tags;
        this.user = user;
        this.team = team;
    }

    protected Shot(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.images = in.readParcelable(Images.class.getClassLoader());
        this.viewsCount = in.readInt();
        this.likesCount = in.readInt();
        this.commentsCount = in.readInt();
        this.attachmentsCount = in.readInt();
        this.reboundsCount = in.readInt();
        this.bucketsCount = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.htmlUrl = in.readString();
        this.attachmentsUrl = in.readString();
        this.bucketsUrl = in.readString();
        this.commentsUrl = in.readString();
        this.likesUrl = in.readString();
        this.projectsUrl = in.readString();
        this.reboundsUrl = in.readString();
        this.animated = in.readByte() != 0;
        this.tags = in.createStringArrayList();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.team = in.readParcelable(Team.class.getClassLoader());
    }

    private String checkNotNull(String str, String placeholder) {
        if (TextUtils.isEmpty(str)) {
            str = placeholder;
        }
        return str;
    }

    public String bestQuality() {
        return TextUtils.isEmpty(images.getHidpi()) ? images.getNormal() : images.getHidpi();
    }

    public String normalQuality() {
        return TextUtils.isEmpty(images.getNormal()) ? images.getTeaser() : images.getNormal();
    }

    Date getCreatedDate(String createdAt) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DribbleApi.DATE_FORMAT, Locale.CHINA);
        try {
            return simpleDateFormat.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Images getImages() {
        return images;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getAttachmentsCount() {
        return attachmentsCount;
    }

    public int getReboundsCount() {
        return reboundsCount;
    }

    public int getBucketsCount() {
        return bucketsCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getAttachmentsUrl() {
        return attachmentsUrl;
    }

    public String getBucketsUrl() {
        return bucketsUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public String getProjectsUrl() {
        return projectsUrl;
    }

    public String getReboundsUrl() {
        return reboundsUrl;
    }

    public boolean isAnimated() {
        return animated;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public User getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isHasFadedIn() {
        return hasFadedIn;
    }

    public void setHasFadedIn(boolean hasFadedIn) {
        this.hasFadedIn = hasFadedIn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeParcelable(this.images, flags);
        dest.writeInt(this.viewsCount);
        dest.writeInt(this.likesCount);
        dest.writeInt(this.commentsCount);
        dest.writeInt(this.attachmentsCount);
        dest.writeInt(this.reboundsCount);
        dest.writeInt(this.bucketsCount);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.attachmentsUrl);
        dest.writeString(this.bucketsUrl);
        dest.writeString(this.commentsUrl);
        dest.writeString(this.likesUrl);
        dest.writeString(this.projectsUrl);
        dest.writeString(this.reboundsUrl);
        dest.writeByte(this.animated ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.tags);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.team, flags);
    }

    public boolean equalInVisual(Shot anotherShot) {
        return false;
    }
}
