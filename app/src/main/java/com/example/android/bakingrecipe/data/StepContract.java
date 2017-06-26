package com.example.android.bakingrecipe.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class StepContract {
    public static final String AUTHORITY = "com.example.android.bakingrecipe.StepContentProvider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_STEPS = "movieReviews";

    public static final class StepEntry implements BaseColumns {
                public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();
        public static final String TABLE_NAME = "step";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoUrl";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";
        public static final String COLUMN_RECIPE_ID = "recipeId";
    }
}
