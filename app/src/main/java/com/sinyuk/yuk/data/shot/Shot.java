package com.sinyuk.yuk.data.shot;

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
public class Shot {

    public static final String COL_TYPE = "type";
    public static final String COL_INDEX = "fake_index";
    /**
     * id : 471756
     * title : Sasquatch
     * description : <p>Quick, messy, five minute sketch of something that might become a fictional something.</p>
     * width : 400
     * height : 300
     * images : {"hidpi":null,"normal":"https://d13yacurqjgara.cloudfront.net/users/1/screenshots/471756/sasquatch.png","teaser":"https://d13yacurqjgara.cloudfront.net/users/1/screenshots/471756/sasquatch_teaser.png"}
     * views_count : 4372
     * likes_count : 149
     * comments_count : 27
     * attachments_count : 0
     * rebounds_count : 2
     * buckets_count : 8
     * created_at : 2012-03-15T01:52:33Z
     * updated_at : 2012-03-15T02:12:57Z
     * html_url : https://dribbble.com/shots/471756-Sasquatch
     * attachments_url : https://api.dribbble.com/v1/shots/471756/attachments
     * buckets_url : https://api.dribbble.com/v1/shots/471756/buckets
     * comments_url : https://api.dribbble.com/v1/shots/471756/comments
     * likes_url : https://api.dribbble.com/v1/shots/471756/likes
     * projects_url : https://api.dribbble.com/v1/shots/471756/projects
     * rebounds_url : https://api.dribbble.com/v1/shots/471756/rebounds
     * animated : false
     * tags : ["fiction","sasquatch","sketch","wip"]
     */
    @Default("others")
    @Column(COL_TYPE)
    private String mType;
    @Column(COL_INDEX)
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int fakeIndex;
    @NotNull
    @Unique
    @SerializedName("id")
    private int mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("width")
    private int mWidth;
    @SerializedName("height")
    private int mHeight;
    /**
     * hidpi : null
     * normal : https://d13yacurqjgara.cloudfront.net/users/1/screenshots/471756/sasquatch.png
     * teaser : https://d13yacurqjgara.cloudfront.net/users/1/screenshots/471756/sasquatch_teaser.png
     */

    @SerializedName("images")
    @Ignore
    private Images mImages;
    @SerializedName("views_count")
    private int mViewsCount;
    @SerializedName("likes_count")
    private int mLikesCount;
    @SerializedName("comments_count")
    private int mCommentsCount;
    @SerializedName("attachments_count")
    private int mAttachmentsCount;
    @SerializedName("rebounds_count")
    private int mReboundsCount;
    @SerializedName("buckets_count")
    private int mBucketsCount;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("updated_at")
    @Ignore
    private String mUpdatedAt;
    @SerializedName("html_url")
    private String mHtmlUrl;
    @SerializedName("attachments_url")
    private String mAttachmentsUrl;
    @SerializedName("buckets_url")
    private String mBucketsUrl;
    @SerializedName("comments_url")
    private String mCommentsUrl;
    @SerializedName("likes_url")
    private String mLikesUrl;
    @SerializedName("projects_url")
    private String mProjectsUrl;
    @SerializedName("rebounds_url")
    private String mReboundsUrl;
    @SerializedName("animated")
    private boolean mAnimated;
    @SerializedName("tags")
    @Ignore
    private List<String> mTags;
    @SerializedName("user")
    @Ignore
    private User user;
    @SerializedName("team")
    @Ignore
    private Team team;
    /**
     * 保存到数据库的一些东西
     */
    @Column("user_name")
    private String username;
    @Column("shot_url")
    private String shotUrl;
    @Column("avatar_url")
    private String avatarUrl;
    @Column("playerOrTeam")
    private String playerOrTeam;
    @Column("pro")
    private boolean pro;


    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public int getId() { return mId;}

    public void setId(int id) { mId = id;}

    public String getTitle() { return mTitle;}

    public void setTitle(String title) { mTitle = title;}

    public String getDescription() { return mDescription;}

    public void setDescription(String description) { mDescription = description;}

    public int getWidth() { return mWidth;}

    public void setWidth(int width) { mWidth = width;}

    public int getHeight() { return mHeight;}

    public void setHeight(int height) { mHeight = height;}

    public Images getImages() { return mImages;}

    public void setImages(Images images) { mImages = images;}

    public int getViewsCount() { return mViewsCount;}

    public void setViewsCount(int viewsCount) { mViewsCount = viewsCount;}

    public int getLikesCount() { return mLikesCount;}

    public void setLikesCount(int likesCount) { mLikesCount = likesCount;}

    public int getCommentsCount() { return mCommentsCount;}

    public void setCommentsCount(int commentsCount) { mCommentsCount = commentsCount;}

    public int getAttachmentsCount() { return mAttachmentsCount;}

    public void setAttachmentsCount(int attachmentsCount) { mAttachmentsCount = attachmentsCount;}

    public int getReboundsCount() { return mReboundsCount;}

    public void setReboundsCount(int reboundsCount) { mReboundsCount = reboundsCount;}

    public int getBucketsCount() { return mBucketsCount;}

    public void setBucketsCount(int bucketsCount) { mBucketsCount = bucketsCount;}

    public String getCreatedAt() { return mCreatedAt;}

    public void setCreatedAt(String createdAt) { mCreatedAt = createdAt;}

    public String getUpdatedAt() { return mUpdatedAt;}

    public void setUpdatedAt(String updatedAt) { mUpdatedAt = updatedAt;}

    public String getHtmlUrl() { return mHtmlUrl;}

    public void setHtmlUrl(String htmlUrl) { mHtmlUrl = htmlUrl;}

    public String getAttachmentsUrl() { return mAttachmentsUrl;}

    public void setAttachmentsUrl(String attachmentsUrl) { mAttachmentsUrl = attachmentsUrl;}

    public String getBucketsUrl() { return mBucketsUrl;}

    public void setBucketsUrl(String bucketsUrl) { mBucketsUrl = bucketsUrl;}

    public String getCommentsUrl() { return mCommentsUrl;}

    public void setCommentsUrl(String commentsUrl) { mCommentsUrl = commentsUrl;}

    public String getLikesUrl() { return mLikesUrl;}

    public void setLikesUrl(String likesUrl) { mLikesUrl = likesUrl;}

    public String getProjectsUrl() { return mProjectsUrl;}

    public void setProjectsUrl(String projectsUrl) { mProjectsUrl = projectsUrl;}

    public String getReboundsUrl() { return mReboundsUrl;}

    public void setReboundsUrl(String reboundsUrl) { mReboundsUrl = reboundsUrl;}

    public boolean isAnimated() { return mAnimated;}

    public void setAnimated(boolean animated) { mAnimated = animated;}

    public List<String> getTags() { return mTags;}

    public void setTags(List<String> tags) { mTags = tags;}


    Date getCreatedDate(String createdAt) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DribbleApi.DATE_FORMAT);
        try {
            return simpleDateFormat.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public String getShotUrl() {
        return shotUrl;
    }


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isPro() {
        return pro;
    }

    public String getPlayerOrTeam() {
        return playerOrTeam;
    }

    public void saveExtras(String username,
                           String shotUrl,
                           String avatarUrl,
                           String playerOrTeam,
                           boolean pro) {
        this.username = checkNotNull(username, " "); // username 不能为空
        this.shotUrl = checkNotNull(shotUrl, " ");
        this.avatarUrl = checkNotNull(avatarUrl, " ");
        this.playerOrTeam = checkNotNull(playerOrTeam, " ");
        this.pro = pro;

    }

    private String checkNotNull(String str, String def) {
        if (TextUtils.isEmpty(str)) {str = def;}
        return str;
    }

    public static class Images {

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
    }
}
