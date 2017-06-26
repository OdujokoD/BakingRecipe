package com.example.android.bakingrecipe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipe.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_RECIPE_TABLE = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " (" +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL," +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_SERVING_COUNT + " INTEGER NOT NULL, " +
                RecipeContract.RecipeEntry.COLUMN_RECIPE_IMAGE_URL + " TEXT, " +
                " UNIQUE (" + RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE" +
                "); ";

        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        onCreate(db);
    }
}
