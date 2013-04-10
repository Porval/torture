package com.jumbo.torture.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Torture {
    public static final String AUTHORITY = "com.jumbo.torture.provider.torture";

    // This class cannot be instantiated
    private Torture(){}


    public static final class TortureMsg implements BaseColumns {
        private TortureMsg(){}

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/tortureMsges");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.torture.msges";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single note.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.torture.msges";

        public static final String DEFAULT_SORT_ORDER = "_id DESC";

        /**
         * The title of the note
         * <P>Type: TEXT</P>
         */
        public static final String TITLE = "title";

        /**
         * The content itself
         * <P>Type: TEXT</P>
         */
        public static final String CONTENT = "content";

        /**
         * The timestamp for when the note was created
         * <P>Type: Long (long from System.curentTimeMillis())</P>
         */
        public static final String CREATED_TIME = "created_time";

        /**
         * The timestamp for when the note was created
         * <P>Type: Long (long from System.curentTimeMillis())</P>
         */
        public static final String MODIFY_TIME	= "modify_time";

        /**
         * The comments
         * <P>Type: TEXT</P>
         */
        public static final String COMMENTS = "comments";

        /**
         * Type : Integer
         */
        public static final String PRIORITY = "prioprity";

        /**
         *
         *	Type : Integer
         */
        public static final String STATUS	= "status";

        /**
         * type : Integer
         */
        public static final String TAGS_ID	= "tagsid";
    }

}