package com.sinyuk.yuk.data.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yuk.data.links.Links;

/**
 * Created by Sinyuk on 16.6.17.
 */
public class User implements Parcelable {
    public static final String TEAM = "Team";
    public static final String PLAYER = "Player";
    public static final String PRO = "Pro";
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {return new User(source);}

        @Override
        public User[] newArray(int size) {return new User[size];}
    };
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
    @SerializedName("id")
    private long mId;
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

    public User() {}

    protected User(Parcel in) {
        this.mId = in.readLong();
        this.mName = in.readString();
        this.mUsername = in.readString();
        this.mHtmlUrl = in.readString();
        this.mAvatarUrl = in.readString();
        this.mBio = in.readString();
        this.mLocation = in.readString();
        this.mLinks = in.readParcelable(Links.class.getClassLoader());
        this.mBucketsCount = in.readInt();
        this.mCommentsReceivedCount = in.readInt();
        this.mFollowersCount = in.readInt();
        this.mFollowingsCount = in.readInt();
        this.mLikesCount = in.readInt();
        this.mLikesReceivedCount = in.readInt();
        this.mProjectsCount = in.readInt();
        this.mReboundsReceivedCount = in.readInt();
        this.mShotsCount = in.readInt();
        this.mTeamsCount = in.readInt();
        this.mCanUploadShot = in.readByte() != 0;
        this.mType = in.readString();
        this.mPro = in.readByte() != 0;
        this.mBucketsUrl = in.readString();
        this.mFollowersUrl = in.readString();
        this.mFollowingUrl = in.readString();
        this.mLikesUrl = in.readString();
        this.mShotsUrl = in.readString();
        this.mTeamsUrl = in.readString();
        this.mCreatedAt = in.readString();
        this.mUpdatedAt = in.readString();
    }

    public long getId() { return mId;}

    public void setId(long id) { mId = id;}

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

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mUsername);
        dest.writeString(this.mHtmlUrl);
        dest.writeString(this.mAvatarUrl);
        dest.writeString(this.mBio);
        dest.writeString(this.mLocation);
        dest.writeParcelable(this.mLinks, flags);
        dest.writeInt(this.mBucketsCount);
        dest.writeInt(this.mCommentsReceivedCount);
        dest.writeInt(this.mFollowersCount);
        dest.writeInt(this.mFollowingsCount);
        dest.writeInt(this.mLikesCount);
        dest.writeInt(this.mLikesReceivedCount);
        dest.writeInt(this.mProjectsCount);
        dest.writeInt(this.mReboundsReceivedCount);
        dest.writeInt(this.mShotsCount);
        dest.writeInt(this.mTeamsCount);
        dest.writeByte(this.mCanUploadShot ? (byte) 1 : (byte) 0);
        dest.writeString(this.mType);
        dest.writeByte(this.mPro ? (byte) 1 : (byte) 0);
        dest.writeString(this.mBucketsUrl);
        dest.writeString(this.mFollowersUrl);
        dest.writeString(this.mFollowingUrl);
        dest.writeString(this.mLikesUrl);
        dest.writeString(this.mShotsUrl);
        dest.writeString(this.mTeamsUrl);
        dest.writeString(this.mCreatedAt);
        dest.writeString(this.mUpdatedAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mHtmlUrl='" + mHtmlUrl + '\'' +
                ", mAvatarUrl='" + mAvatarUrl + '\'' +
                ", mBio='" + mBio + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mLinks=" + mLinks +
                ", mBucketsCount=" + mBucketsCount +
                ", mCommentsReceivedCount=" + mCommentsReceivedCount +
                ", mFollowersCount=" + mFollowersCount +
                ", mFollowingsCount=" + mFollowingsCount +
                ", mLikesCount=" + mLikesCount +
                ", mLikesReceivedCount=" + mLikesReceivedCount +
                ", mProjectsCount=" + mProjectsCount +
                ", mReboundsReceivedCount=" + mReboundsReceivedCount +
                ", mShotsCount=" + mShotsCount +
                ", mTeamsCount=" + mTeamsCount +
                ", mCanUploadShot=" + mCanUploadShot +
                ", mType='" + mType + '\'' +
                ", mPro=" + mPro +
                ", mBucketsUrl='" + mBucketsUrl + '\'' +
                ", mFollowersUrl='" + mFollowersUrl + '\'' +
                ", mFollowingUrl='" + mFollowingUrl + '\'' +
                ", mLikesUrl='" + mLikesUrl + '\'' +
                ", mShotsUrl='" + mShotsUrl + '\'' +
                ", mTeamsUrl='" + mTeamsUrl + '\'' +
                ", mCreatedAt='" + mCreatedAt + '\'' +
                ", mUpdatedAt='" + mUpdatedAt + '\'' +
                '}';
    }
}
