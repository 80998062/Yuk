package com.sinyuk.yuk.data.team;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sinyuk.yuk.data.links.Links;

/**
 * Created by Sinyuk on 16/7/16.
 */
public class Team implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("username")
    private String username;
    @SerializedName("html_url")
    private String htmlUrl;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("bio")
    private String bio;
    @SerializedName("location")
    private String location;
    /**
     * web : http://www.ueno.co
     * twitter : https://twitter.com/uenodotco
     */

    @SerializedName("links")
    private Links links;
    @SerializedName("buckets_count")
    private int bucketsCount;
    @SerializedName("comments_received_count")
    private int commentsReceivedCount;
    @SerializedName("followers_count")
    private int followersCount;
    @SerializedName("followings_count")
    private int followingsCount;
    @SerializedName("likes_count")
    private int likesCount;
    @SerializedName("likes_received_count")
    private int likesReceivedCount;
    @SerializedName("projects_count")
    private int projectsCount;
    @SerializedName("rebounds_received_count")
    private int reboundsReceivedCount;
    @SerializedName("shots_count")
    private int shotsCount;
    @SerializedName("can_upload_shot")
    private boolean canUploadShot;
    @SerializedName("type")
    private String type;
    @SerializedName("pro")
    private boolean pro;
    @SerializedName("buckets_url")
    private String bucketsUrl;
    @SerializedName("followers_url")
    private String followersUrl;
    @SerializedName("following_url")
    private String followingUrl;
    @SerializedName("likes_url")
    private String likesUrl;
    @SerializedName("projects_url")
    private String projectsUrl;
    @SerializedName("shots_url")
    private String shotsUrl;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("members_count")
    private int membersCount;
    @SerializedName("members_url")
    private String membersUrl;
    @SerializedName("team_shots_url")
    private String teamShotsUrl;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username;}

    public String getHtmlUrl() { return htmlUrl;}

    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl;}

    public String getAvatarUrl() { return avatarUrl;}

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl;}

    public String getBio() { return bio;}

    public void setBio(String bio) { this.bio = bio;}

    public String getLocation() { return location;}

    public void setLocation(String location) { this.location = location;}

    public Links getLinks() { return links;}

    public void setLinks(Links links) { this.links = links;}

    public int getBucketsCount() { return bucketsCount;}

    public void setBucketsCount(int bucketsCount) { this.bucketsCount = bucketsCount;}

    public int getCommentsReceivedCount() { return commentsReceivedCount;}

    public void setCommentsReceivedCount(int commentsReceivedCount) { this.commentsReceivedCount = commentsReceivedCount;}

    public int getFollowersCount() { return followersCount;}

    public void setFollowersCount(int followersCount) { this.followersCount = followersCount;}

    public int getFollowingsCount() { return followingsCount;}

    public void setFollowingsCount(int followingsCount) { this.followingsCount = followingsCount;}

    public int getLikesCount() { return likesCount;}

    public void setLikesCount(int likesCount) { this.likesCount = likesCount;}

    public int getLikesReceivedCount() { return likesReceivedCount;}

    public void setLikesReceivedCount(int likesReceivedCount) { this.likesReceivedCount = likesReceivedCount;}

    public int getProjectsCount() { return projectsCount;}

    public void setProjectsCount(int projectsCount) { this.projectsCount = projectsCount;}

    public int getReboundsReceivedCount() { return reboundsReceivedCount;}

    public void setReboundsReceivedCount(int reboundsReceivedCount) { this.reboundsReceivedCount = reboundsReceivedCount;}

    public int getShotsCount() { return shotsCount;}

    public void setShotsCount(int shotsCount) { this.shotsCount = shotsCount;}

    public boolean isCanUploadShot() { return canUploadShot;}

    public void setCanUploadShot(boolean canUploadShot) { this.canUploadShot = canUploadShot;}

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public boolean isPro() { return pro;}

    public void setPro(boolean pro) { this.pro = pro;}

    public String getBucketsUrl() { return bucketsUrl;}

    public void setBucketsUrl(String bucketsUrl) { this.bucketsUrl = bucketsUrl;}

    public String getFollowersUrl() { return followersUrl;}

    public void setFollowersUrl(String followersUrl) { this.followersUrl = followersUrl;}

    public String getFollowingUrl() { return followingUrl;}

    public void setFollowingUrl(String followingUrl) { this.followingUrl = followingUrl;}

    public String getLikesUrl() { return likesUrl;}

    public void setLikesUrl(String likesUrl) { this.likesUrl = likesUrl;}

    public String getProjectsUrl() { return projectsUrl;}

    public void setProjectsUrl(String projectsUrl) { this.projectsUrl = projectsUrl;}

    public String getShotsUrl() { return shotsUrl;}

    public void setShotsUrl(String shotsUrl) { this.shotsUrl = shotsUrl;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

    public String getUpdatedAt() { return updatedAt;}

    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt;}

    public int getMembersCount() { return membersCount;}

    public void setMembersCount(int membersCount) { this.membersCount = membersCount;}

    public String getMembersUrl() { return membersUrl;}

    public void setMembersUrl(String membersUrl) { this.membersUrl = membersUrl;}

    public String getTeamShotsUrl() { return teamShotsUrl;}

    public void setTeamShotsUrl(String teamShotsUrl) { this.teamShotsUrl = teamShotsUrl;}



    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.bio);
        dest.writeString(this.location);
        dest.writeParcelable(this.links, flags);
        dest.writeInt(this.bucketsCount);
        dest.writeInt(this.commentsReceivedCount);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingsCount);
        dest.writeInt(this.likesCount);
        dest.writeInt(this.likesReceivedCount);
        dest.writeInt(this.projectsCount);
        dest.writeInt(this.reboundsReceivedCount);
        dest.writeInt(this.shotsCount);
        dest.writeByte(this.canUploadShot ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeByte(this.pro ? (byte) 1 : (byte) 0);
        dest.writeString(this.bucketsUrl);
        dest.writeString(this.followersUrl);
        dest.writeString(this.followingUrl);
        dest.writeString(this.likesUrl);
        dest.writeString(this.projectsUrl);
        dest.writeString(this.shotsUrl);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeInt(this.membersCount);
        dest.writeString(this.membersUrl);
        dest.writeString(this.teamShotsUrl);
    }

    public Team() {}

    protected Team(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.htmlUrl = in.readString();
        this.avatarUrl = in.readString();
        this.bio = in.readString();
        this.location = in.readString();
        this.links = in.readParcelable(Links.class.getClassLoader());
        this.bucketsCount = in.readInt();
        this.commentsReceivedCount = in.readInt();
        this.followersCount = in.readInt();
        this.followingsCount = in.readInt();
        this.likesCount = in.readInt();
        this.likesReceivedCount = in.readInt();
        this.projectsCount = in.readInt();
        this.reboundsReceivedCount = in.readInt();
        this.shotsCount = in.readInt();
        this.canUploadShot = in.readByte() != 0;
        this.type = in.readString();
        this.pro = in.readByte() != 0;
        this.bucketsUrl = in.readString();
        this.followersUrl = in.readString();
        this.followingUrl = in.readString();
        this.likesUrl = in.readString();
        this.projectsUrl = in.readString();
        this.shotsUrl = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.membersCount = in.readInt();
        this.membersUrl = in.readString();
        this.teamShotsUrl = in.readString();
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {return new Team(source);}

        @Override
        public Team[] newArray(int size) {return new Team[size];}
    };
}