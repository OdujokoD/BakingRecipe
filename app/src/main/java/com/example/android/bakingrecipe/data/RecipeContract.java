package com.example.android.bakingrecipe.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String AUTHORITY = "com.example.android.bakingrecipe.RecipeContentProvider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPE = "recipe";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();

        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_RECIPE_GSON = "recipeGson";
    }
}
