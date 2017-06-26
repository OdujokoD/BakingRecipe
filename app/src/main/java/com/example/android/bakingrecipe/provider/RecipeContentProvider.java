package com.example.android.bakingrecipe.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.bakingrecipe.data.RecipeContract;
import com.example.android.bakingrecipe.data.RecipeDBHelper;

import static com.example.android.bakingrecipe.data.RecipeContract.RecipeEntry.TABLE_NAME;


public class RecipeContentProvider extends ContentProvider {

    private static final int RECIPES = 100;
    private static final int RECIPE_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDBHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(RecipeContract.AUTHORITY,
                RecipeContract.PATH_RECIPE, RECIPES);
        uriMatcher.addURI(RecipeContract.AUTHORITY,
                RecipeContract.PATH_RECIPE + "/#", RECIPE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new RecipeDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor recipeCursor;

        switch (match) {
            case RECIPES:
                recipeCursor =  db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        recipeCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return recipeCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri recipeUri;

        switch (match) {
            case RECIPES:
                long id = db.insert(RecipeContract.RecipeEntry.TABLE_NAME,
                        null, values);
                if ( id > 0 ) {
                    recipeUri = ContentUris.withAppendedId(RecipeContract.RecipeEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return recipeUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int mDbHelper;

        switch (match) {
            case RECIPE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                mDbHelper = db.delete(RecipeContract.RecipeEntry.TABLE_NAME,
                        "recipeId=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (mDbHelper != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return mDbHelper;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
