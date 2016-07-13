package com.sinyuk.yuk.api;

/**
 * Created by Sinyuk on 16.6.16.
 */
public class DribbleApi {

    public static final String END_POINT = "https://api.dribbble.com/v1/";

    // Shots

    /**
     * List Types
     * Default: Results of any type.
     */
    public static final String ALL = "";
    public static final String ANIMATED = "animated";
    public static final String ATTACHMENTS = "attachments";
    public static final String DEBUTS = "debuts";
    public static final String PLAYOFFS = "playoffs";
    public static final String REBOUNDS = "rebounds";
    public static final String TEAMS = "teams";

    /**
     * timeframe
     * Default: Results from now.
     */
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String EVER = "ever";

    /**
     * data
     * Limit the timeframe to a specific date, week, month, or year. Must be in the format of YYYY-MM-DD.
     */

    /**
     * sort
     * Default: Results are sorted by popularity.
     */
    public static final String COMMENTS = "comments";
    public static final String RECENT = "recent";
    public static final String VIEWS = "views";

    public static final String DATE_FORMAT  = "yyyy-MM-dd'T'HH:mm:ss'Z'";
}
