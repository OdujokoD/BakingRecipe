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

import com.example.android.bakingrecipe.data.IngredientContract;
import com.example.android.bakingrecipe.data.IngredientDBHelper;

import static com.example.android.bakingrecipe.data.IngredientContract.IngredientEntry.TABLE_NAME;

public class IngredientContentProvider extends ContentProvider {

    private static final int INGREDIENT = 200;
    private static final int INGREDIENT_WITH_ID = 201;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private IngredientDBHelper mDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(IngredientContract.AUTHORITY,
                IngredientContract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(IngredientContract.AUTHORITY,
                IngredientContract.PATH_INGREDIENT + "/#", INGREDIENT_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHelper = new IngredientDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor ingredientCursor;

        switch (match) {
            case INGREDIENT:
                ingredientCursor =  db.query(TABLE_NAME,
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

        ingredientCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return ingredientCursor;
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
        Uri ingredientUri;

        switch (match) {
            case INGREDIENT:
                long id = db.insert(TABLE_NAME,
                        null, values);
                if ( id > 0 ) {
                    ingredientUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ingredientUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int mDbHelper;

        switch (match) {
            case INGREDIENT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                mDbHelper = db.delete(TABLE_NAME,
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
