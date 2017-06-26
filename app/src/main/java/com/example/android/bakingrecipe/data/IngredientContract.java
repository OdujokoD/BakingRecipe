package com.example.android.bakingrecipe.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientContract {
    public static final String AUTHORITY = "com.example.android.bakingrecipe.IngredientContentProvider";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_INGREDIENT = "ingredient";

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();
        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_INGREDIENT_NAME = "ingredientName";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_RECIPE_ID = "recipeId";
    }
}
