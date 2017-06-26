package com.example.android.bakingrecipe.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StepDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "step.db";
    private static final int DATABASE_VERSION = 1;

    public StepDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_STEP_TABLE = "CREATE TABLE " +
                StepContract.StepEntry.TABLE_NAME + " (" +
                StepContract.StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                StepContract.StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                StepContract.StepEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                StepContract.StepEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL, " +
                StepContract.StepEntry.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, " +
                StepContract.StepEntry.COLUMN_RECIPE_ID + " TEXT NOT NULL" +
                "); ";

        db.execSQL(CREATE_STEP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StepContract.StepEntry.TABLE_NAME);
        onCreate(db);
    }
}
