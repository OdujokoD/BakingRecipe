package com.example.android.bakingrecipe.loaders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.android.bakingrecipe.data.RecipeContract;

public class RecipeDbLoader implements LoaderManager.LoaderCallbacks<Cursor>{

    private Context mContext;
    private SendResult mCallback;

    public RecipeDbLoader(Context context){
        mContext = context;
        mCallback = (SendResult)context;
    }

    public interface SendResult{
        void onRecipeAcquire(Cursor data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(mContext) {

            Cursor mRecipe = null;

            @Override
            protected void onStartLoading() {
                if (mRecipe != null) {
                    deliverResult(mRecipe);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContext().getContentResolver()
                            .query(RecipeContract.RecipeEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null);

                } catch (Exception e) {
                    Log.e("LOADER", "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor recipe) {
                mRecipe = recipe;
                super.deliverResult(recipe);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCallback.onRecipeAcquire(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mCallback.onRecipeAcquire(null);
    }
}
