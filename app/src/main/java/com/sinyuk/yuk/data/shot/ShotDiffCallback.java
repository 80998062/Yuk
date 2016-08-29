package com.sinyuk.yuk.data.shot;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by Sinyuk on 16/8/29.
 */
public final class ShotDiffCallback extends DiffUtil.Callback {
//    private static final String KEY_ID = "ID";
//    private static final String KEY_TITLE = "TITLE";
//    private static final String KEY_DESCRIPTION = "DESCRIPTION";
//    private static final String KEY_IMAGES = "IMAGES";
//    private static final String KEY_WIDTH = "WIDTH";
//    private static final String KEY_HEIGHT = "HEIGHT";
//    private static final String KEY_VIEWS_COUNT = "VIEWS_COUNT";
//    private static final String KEY_LIKES_COUNT = "LIKES_COUNT";
//    private static final String KEY_COMMENTS_COUNT = "COMMENTS_COUNT";
//    private static final String KEY_ATTACHMENTS_COUNT = "ATTACHMENTS_COUNT";
//    private static final String KEY_REBOUNDS_COUNT = "REBOUNDS_COUNT";
//    private static final String KEY_BUCKETS_COUNT = "BUCKETS_COUNT";
//    private static final String KEY_CREATE_AT = "CREATE_AT";
//    private static final String KEY_UPDATE_AT = "UPDATE_AT";
//    private static final String KEY_HTML_URL = "HTML_URL";
//    private static final String KEY_ATTACHMENT_URL = "ATTACHMENT_URL";
//    private static final String KEY_BUCKETS_URL = "BUCKETS_URL";
//    private static final String KEY_COMMENTS_URL = "COMMENTS_URL";
//    private static final String KEY_LIKES_URL = "LIKES_URL";
//    private static final String KEY_PROJECTS_URL = "PROJECTS_URL";
//    private static final String KEY_REBOUNDS_URL = "REBOUNDS_URL";
//    private static final String KEY_ANIMATED = "ANIMATED";
//    private static final String KEY_TAGS = "TAGS";
//    private static final String KEY_USER = "USER";
//    private static final String KEY_TEAM = "TEAM";
//    private static final String KEY_HAS_FADED = "HAS_FADED";
//    private static final String KEY_PARSED_DESCRIPTION = "PARSED_DESCRIPTION";

    private List<Shot> oldList;
    private List<Shot> newList;

    public ShotDiffCallback(List<Shot> oldList, List<Shot> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();

    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == (newList.get(newItemPosition).getId());

    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUpdatedAt().equals(newList.get(newItemPosition).getUpdatedAt());
    }


//    @Nullable
//    @Override
//    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//        Shot oldItem = oldList.get(oldItemPosition);
//        Shot newItem = newList.get(newItemPosition);
//        Bundle diffBundle = new Bundle();
//        // 1
//        if (newItem.getId() != oldItem.getId()) {
//            diffBundle.putLong(KEY_ID, newItem.getId());
//        }
//        // 2
//        if (!newItem.getTitle().equals(oldItem.getTitle())) {
//            diffBundle.putString(KEY_TITLE, newItem.getTitle());
//        }
//        if (!newItem.getDescription().equals(oldItem.getDescription())) {
//            diffBundle.putString(KEY_DESCRIPTION, newItem.getDescription());
//        }
//        //
//        if (!newItem.getImages().equals(oldItem.getImages())) {
//            diffBundle.putParcelable(KEY_IMAGES, newItem.getImages());
//        }
//        // 3
//        if (newItem.getWidth() != oldItem.getWidth()) {
//            diffBundle.putInt(KEY_WIDTH, newItem.getWidth());
//        }
//        // 4
//        if (newItem.getHeight() != oldItem.getHeight()) {
//            diffBundle.putInt(KEY_HEIGHT, newItem.getHeight());
//        }
//        // 5
//        if (newItem.getViewsCount() != oldItem.getViewsCount()) {
//            diffBundle.putInt(KEY_VIEWS_COUNT, newItem.getViewsCount());
//        }
//        // 6
//        if (newItem.getLikesCount() != oldItem.getLikesCount()) {
//            diffBundle.putInt(KEY_LIKES_COUNT, newItem.getLikesCount());
//        }
//        // 7
//        if (newItem.getCommentsCount() != oldItem.getCommentsCount()) {
//            diffBundle.putInt(KEY_COMMENTS_COUNT, newItem.getCommentsCount());
//        }
//        // 8
//        if (newItem.getAttachmentsCount() != oldItem.getAttachmentsCount()) {
//            diffBundle.putInt(KEY_ATTACHMENTS_COUNT, newItem.getAttachmentsCount());
//        }
//        // 9
//        if (newItem.getBucketsCount() != oldItem.getBucketsCount()) {
//            diffBundle.putInt(KEY_BUCKETS_COUNT, newItem.getBucketsCount());
//        }
//        // 10
//        if (newItem.getReboundsCount() != oldItem.getReboundsCount()) {
//            diffBundle.putInt(KEY_REBOUNDS_COUNT, newItem.getReboundsCount());
//        }
//        // 11
//        if (!newItem.getCreatedAt().equals(oldItem.getCreatedAt())) {
//            diffBundle.putString(KEY_CREATE_AT, newItem.getCreatedAt());
//        }
//        // 12
//        if (!newItem.getUpdatedAt().equals(oldItem.getUpdatedAt())) {
//            diffBundle.putString(KEY_UPDATE_AT, newItem.getUpdatedAt());
//        }
//        // 13
//        if (!newItem.getHtmlUrl().equals(oldItem.getHtmlUrl())) {
//            diffBundle.putString(KEY_HTML_URL, newItem.getHtmlUrl());
//        }
//        // 14
//        if (!newItem.getAttachmentsUrl().equals(oldItem.getAttachmentsUrl())) {
//            diffBundle.putString(KEY_ATTACHMENT_URL, newItem.getAttachmentsUrl());
//        }
//        // 15
//        if (!newItem.getBucketsUrl().equals(oldItem.getBucketsUrl())) {
//            diffBundle.putString(KEY_BUCKETS_URL, newItem.getBucketsUrl());
//        }
//        // 16
//        if (!newItem.getCommentsUrl().equals(oldItem.getCommentsUrl())) {
//            diffBundle.putString(KEY_COMMENTS_URL, newItem.getCommentsUrl());
//        }
//        // 17
//        if (!newItem.getLikesUrl().equals(oldItem.getLikesUrl())) {
//            diffBundle.putString(KEY_LIKES_URL, newItem.getLikesUrl());
//        }
//        // 18
//        if (!newItem.getProjectsUrl().equals(oldItem.getProjectsUrl())) {
//            diffBundle.putString(KEY_PROJECTS_URL, newItem.getProjectsUrl());
//        }
//        // 19
//        if (!newItem.getReboundsUrl().equals(oldItem.getReboundsUrl())) {
//            diffBundle.putString(KEY_REBOUNDS_URL, newItem.getReboundsUrl());
//        }
//        // 20
//        if (!newItem.isAnimated() == oldItem.isAnimated()) {
//            diffBundle.putBoolean(KEY_ANIMATED, newItem.isAnimated());
//        }
//        // 21
//        if (!newItem.getTags().equals(oldItem.getTags())) {
//            diffBundle.putStringArrayList(KEY_TAGS, newItem.getTags());
//        }
//        // 22
//        if (!newItem.getUser().equals(oldItem.getUser())) {
//            diffBundle.putParcelable(KEY_USER, newItem.getUser());
//        }
//        // 23
//        if (!newItem.getTeam().equals(oldItem.getTeam())) {
//            diffBundle.putParcelable(KEY_TEAM, newItem.getTeam());
//        }
//        // 24
//        if (!newItem.isHasFadedIn() == oldItem.isHasFadedIn()) {
//            diffBundle.putBoolean(KEY_HAS_FADED, newItem.isHasFadedIn());
//        }
//        if (diffBundle.size() == 0) {
//            return null;
//        }
//        return diffBundle;
//    }
}
