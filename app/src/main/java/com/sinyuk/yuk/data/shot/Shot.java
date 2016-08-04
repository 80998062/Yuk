package com.sinyuk.yuk.data.shot;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Default;
import com.litesuits.orm.db.annotation.Ignore;
import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.enums.AssignType;
import com.sinyuk.yuk.api.DribbleApi;
import com.sinyuk.yuk.data.team.Team;
import com.sinyuk.yuk.data.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sinyuk on 16.6.16.
 */
@Table("shot")
public class Shot implements Parcelable {
    public static final String COL_TYPE = "type";
    public static final String COL_INDEX = "fake_index";
    /**
     * It seems that a string = "" will be considered as NULL in liteOrm
     */
    private final static String PLACE_HOLDER = " ";

    @Default("others")
    @Column(COL_TYPE)
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    @Column(COL_INDEX)
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private final int fakeIndex;
    @NotNull
    @Unique
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
    @Ignore
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
    @Ignore
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
    @Ignore
    private final List<String> tags;
    @SerializedName("user")
    @Ignore
    private final User user;
    @SerializedName("team")
    @Ignore
    private final Team team;
    /**
     * extras
     */
    @Column("user_name")
    private String username;
    @Column("hidpi")
    private String hidpi;
    @Column("normal")
    private String normal;
    @Column("teaser")
    private String teaser;
    @Column("avatar_url")
    private String avatarUrl;
    @Column("playerOrTeam")
    private String playerOrTeam;
    @Column("pro")
    private boolean pro;
    @Ignore
    private boolean hasFadedIn = false;
    @Ignore
    private Spanned parsedDescription;

    public Shot(String type, int fakeIndex, int id, String title, String description, int width, int height, Images images, int viewsCount, int likesCount, int commentsCount, int attachmentsCount, int reboundsCount, int bucketsCount, String createdAt, String updatedAt, String htmlUrl, String attachmentsUrl, String bucketsUrl, String commentsUrl, String likesUrl, String projectsUrl, String reboundsUrl, boolean animated, List<String> tags, User user, Team team, String username, String hidpi, String normal, String teaser, String avatarUrl, String playerOrTeam, boolean pro, Spanned parsedDescription) {
        this.type = type;
        this.fakeIndex = fakeIndex;
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
        this.username = username;
        this.hidpi = hidpi;
        this.normal = normal;
        this.teaser = teaser;
        this.avatarUrl = avatarUrl;
        this.playerOrTeam = playerOrTeam;
        this.pro = pro;
        this.parsedDescription = parsedDescription;
    }

    public void addExtras(String username,
                          String hidpi,
                          String normal,
                          String teaser,
                          String avatarUrl,
                          String playerOrTeam,
                          boolean pro) {
        this.username = checkNotNull(username, PLACE_HOLDER); // username 不能为空
        this.hidpi = checkNotNull(hidpi, PLACE_HOLDER);
        this.normal = checkNotNull(normal, PLACE_HOLDER);
        this.teaser = checkNotNull(teaser, PLACE_HOLDER);
        this.avatarUrl = checkNotNull(avatarUrl, PLACE_HOLDER);
        this.playerOrTeam = checkNotNull(playerOrTeam, PLACE_HOLDER);
        this.pro = pro;

    }

    private String checkNotNull(String str, String placeholder) {
        if (TextUtils.isEmpty(str)) {str = placeholder;}
        return str;
    }

    public String bestQuality() {
        return !TextUtils.isEmpty(hidpi) ? hidpi : normal;
    }

    public String normalQuality() {
        return !TextUtils.isEmpty(normal) ? normal : teaser;
    }

    Date getCreatedDate(String createdAt) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DribbleApi.DATE_FORMAT);
        try {
            return simpleDateFormat.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeInt(this.fakeIndex);
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
        dest.writeString(this.username);
        dest.writeString(this.hidpi);
        dest.writeString(this.normal);
        dest.writeString(this.teaser);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.playerOrTeam);
        dest.writeByte(this.pro ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasFadedIn ? (byte) 1 : (byte) 0);
    }

    protected Shot(Parcel in) {
        this.type = in.readString();
        this.fakeIndex = in.readInt();
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
        this.username = in.readString();
        this.hidpi = in.readString();
        this.normal = in.readString();
        this.teaser = in.readString();
        this.avatarUrl = in.readString();
        this.playerOrTeam = in.readString();
        this.pro = in.readByte() != 0;
        this.hasFadedIn = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Shot> CREATOR = new Parcelable.Creator<Shot>() {
        @Override
        public Shot createFromParcel(Parcel source) {return new Shot(source);}

        @Override
        public Shot[] newArray(int size) {return new Shot[size];}
    };

    public static String getColType() {
        return COL_TYPE;
    }

    public static String getColIndex() {
        return COL_INDEX;
    }

    public static String getPlaceHolder() {
        return PLACE_HOLDER;
    }

    public String getType() {
        return type;
    }

    public int getFakeIndex() {
        return fakeIndex;
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

    public List<String> getTags() {
        return tags;
    }

    public User getUser() {
        return user;
    }

    public Team getTeam() {
        return team;
    }

    public String getUsername() {
        return username;
    }

    public String getHidpi() {
        return hidpi;
    }

    public String getNormal() {
        return normal;
    }

    public String getTeaser() {
        return teaser;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getPlayerOrTeam() {
        return playerOrTeam;
    }

    public boolean isPro() {
        return pro;
    }

    public boolean isHasFadedIn() {
        return hasFadedIn;
    }

    public void setHasFadedIn(boolean hasFadedIn) {
        this.hasFadedIn = hasFadedIn;
    }

    public Spanned getParsedDescription() {
        return parsedDescription;
    }

    public static Creator<Shot> getCREATOR() {
        return CREATOR;
    }
}
