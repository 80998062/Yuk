package com.sinyuk.yuk.data.user;

import com.google.gson.annotations.SerializedName;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by Sinyuk on 16.6.17.
 */
@Table("user")
public class User {
    public static final String TEAM  = "TEAM";
    public static final String PLAYER  = "PLAYER";
    public static final String PRO  = "PRO";
    /**
     * id : 1
     * name : Dan Cederholm
     * username : simplebits
     * html_url : https://dribbble.com/simplebits
     * avatar_url : https://d13yacurqjgara.cloudfront.net/users/1/avatars/normal/dc.jpg?1371679243
     * bio : Co-founder &amp; designer of <a href="https://dribbble.com/dribbble">@Dribbble</a>. Principal of SimpleBits. Aspiring clawhammer banjoist.
     * location : Salem, MA
     * links : {"web":"http://simplebits.com","twitter":"https://twitter.com/simplebits"}
     * buckets_count : 10
     * comments_received_count : 3395
     * followers_count : 29262
     * followings_count : 1728
     * likes_count : 34954
     * likes_received_count : 27568
     * projects_count : 8
     * rebounds_received_count : 504
     * shots_count : 214
     * teams_count : 1
     * can_upload_shot : true
     * type : Player
     * pro : true
     * buckets_url : https://dribbble.com/v1/users/1/buckets
     * followers_url : https://dribbble.com/v1/users/1/followers
     * following_url : https://dribbble.com/v1/users/1/following
     * likes_url : https://dribbble.com/v1/users/1/likes
     * shots_url : https://dribbble.com/v1/users/1/shots
     * teams_url : https://dribbble.com/v1/users/1/teams
     * created_at : 2009-07-08T02:51:22Z
     * updated_at : 2014-02-22T17:10:33Z
     */
    @PrimaryKey(AssignType.BY_MYSELF)
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("html_url")
    private String mHtmlUrl;
    @SerializedName("avatar_url")
    private String mAvatarUrl;
    @SerializedName("bio")
    private String mBio;
    @SerializedName("location")
    private String mLocation;
    /**
     * web : http://simplebits.com
     * twitter : https://twitter.com/simplebits
     */

    @SerializedName("links")
    private Links mLinks;
    @SerializedName("buckets_count")
    private int mBucketsCount;
    @SerializedName("comments_received_count")
    private int mCommentsReceivedCount;
    @SerializedName("followers_count")
    private int mFollowersCount;
    @SerializedName("followings_count")
    private int mFollowingsCount;
    @SerializedName("likes_count")
    private int mLikesCount;
    @SerializedName("likes_received_count")
    private int mLikesReceivedCount;
    @SerializedName("projects_count")
    private int mProjectsCount;
    @SerializedName("rebounds_received_count")
    private int mReboundsReceivedCount;
    @SerializedName("shots_count")
    private int mShotsCount;
    @SerializedName("teams_count")
    private int mTeamsCount;
    @SerializedName("can_upload_shot")
    private boolean mCanUploadShot;
    /* "type": "Team",
    "pro": false,
    "type": "Player",
    "pro": true,*/
    @SerializedName("type")
    private String mType;
    @SerializedName("pro")
    private boolean mPro;
    @SerializedName("buckets_url")
    private String mBucketsUrl;
    @SerializedName("followers_url")
    private String mFollowersUrl;
    @SerializedName("following_url")
    private String mFollowingUrl;
    @SerializedName("likes_url")
    private String mLikesUrl;
    @SerializedName("shots_url")
    private String mShotsUrl;
    @SerializedName("teams_url")
    private String mTeamsUrl;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("updated_at")
    private String mUpdatedAt;

    public int getId() { return mId;}

    public void setId(int id) { mId = id;}

    public String getName() { return mName;}

    public void setName(String name) { mName = name;}

    public String getUsername() { return mUsername;}

    public void setUsername(String username) { mUsername = username;}

    public String getHtmlUrl() { return mHtmlUrl;}

    public void setHtmlUrl(String htmlUrl) { mHtmlUrl = htmlUrl;}

    public String getAvatarUrl() { return mAvatarUrl;}

    public void setAvatarUrl(String avatarUrl) { mAvatarUrl = avatarUrl;}

    public String getBio() { return mBio;}

    public void setBio(String bio) { mBio = bio;}

    public String getLocation() { return mLocation;}

    public void setLocation(String location) { mLocation = location;}

    public Links getLinks() { return mLinks;}

    public void setLinks(Links links) { mLinks = links;}

    public int getBucketsCount() { return mBucketsCount;}

    public void setBucketsCount(int bucketsCount) { mBucketsCount = bucketsCount;}

    public int getCommentsReceivedCount() { return mCommentsReceivedCount;}

    public void setCommentsReceivedCount(int commentsReceivedCount) { mCommentsReceivedCount = commentsReceivedCount;}

    public int getFollowersCount() { return mFollowersCount;}

    public void setFollowersCount(int followersCount) { mFollowersCount = followersCount;}

    public int getFollowingsCount() { return mFollowingsCount;}

    public void setFollowingsCount(int followingsCount) { mFollowingsCount = followingsCount;}

    public int getLikesCount() { return mLikesCount;}

    public void setLikesCount(int likesCount) { mLikesCount = likesCount;}

    public int getLikesReceivedCount() { return mLikesReceivedCount;}

    public void setLikesReceivedCount(int likesReceivedCount) { mLikesReceivedCount = likesReceivedCount;}

    public int getProjectsCount() { return mProjectsCount;}

    public void setProjectsCount(int projectsCount) { mProjectsCount = projectsCount;}

    public int getReboundsReceivedCount() { return mReboundsReceivedCount;}

    public void setReboundsReceivedCount(int reboundsReceivedCount) { mReboundsReceivedCount = reboundsReceivedCount;}

    public int getShotsCount() { return mShotsCount;}

    public void setShotsCount(int shotsCount) { mShotsCount = shotsCount;}

    public int getTeamsCount() { return mTeamsCount;}

    public void setTeamsCount(int teamsCount) { mTeamsCount = teamsCount;}

    public boolean isCanUploadShot() { return mCanUploadShot;}

    public void setCanUploadShot(boolean canUploadShot) { mCanUploadShot = canUploadShot;}

    public String getType() { return mType;}

    public void setType(String type) { mType = type;}

    public boolean isPro() { return mPro;}

    public void setPro(boolean pro) { mPro = pro;}

    public String getBucketsUrl() { return mBucketsUrl;}

    public void setBucketsUrl(String bucketsUrl) { mBucketsUrl = bucketsUrl;}

    public String getFollowersUrl() { return mFollowersUrl;}

    public void setFollowersUrl(String followersUrl) { mFollowersUrl = followersUrl;}

    public String getFollowingUrl() { return mFollowingUrl;}

    public void setFollowingUrl(String followingUrl) { mFollowingUrl = followingUrl;}

    public String getLikesUrl() { return mLikesUrl;}

    public void setLikesUrl(String likesUrl) { mLikesUrl = likesUrl;}

    public String getShotsUrl() { return mShotsUrl;}

    public void setShotsUrl(String shotsUrl) { mShotsUrl = shotsUrl;}

    public String getTeamsUrl() { return mTeamsUrl;}

    public void setTeamsUrl(String teamsUrl) { mTeamsUrl = teamsUrl;}

    public String getCreatedAt() { return mCreatedAt;}

    public void setCreatedAt(String createdAt) { mCreatedAt = createdAt;}

    public String getUpdatedAt() { return mUpdatedAt;}

    public void setUpdatedAt(String updatedAt) { mUpdatedAt = updatedAt;}

    public static class Links {
        @SerializedName("web")
        private String mWeb;
        @SerializedName("twitter")
        private String mTwitter;

        public String getWeb() { return mWeb;}

        public void setWeb(String web) { mWeb = web;}

        public String getTwitter() { return mTwitter;}

        public void setTwitter(String twitter) { mTwitter = twitter;}
    }
}
