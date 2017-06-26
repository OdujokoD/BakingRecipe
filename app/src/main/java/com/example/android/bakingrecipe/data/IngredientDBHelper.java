package com.example.android.bakingrecipe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IngredientDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredient.db";
    private static final int DATABASE_VERSION = 1;

    public IngredientDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_INGREDIENT_TABLE = "CREATE TABLE " +
                IngredientContract.IngredientEntry.TABLE_NAME + " (" +
                IngredientContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IngredientContract.IngredientEntry.COLUMN_INGREDIENT_NAME + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_QUANTITY + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL" +
                "); ";

        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientContract.IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
