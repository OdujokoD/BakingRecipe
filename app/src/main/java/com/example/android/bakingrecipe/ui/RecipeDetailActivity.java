package com.example.android.bakingrecipe.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.data.RecipeContract;
import com.example.android.bakingrecipe.loaders.RecipeDbLoader;
import com.example.android.bakingrecipe.model.Recipe;
import com.google.gson.Gson;

import static com.example.android.bakingrecipe.util.Constants.MULTIPANE_STRING;
import static com.example.android.bakingrecipe.util.Constants.RECIPE_LOADER_ID;
import static com.example.android.bakingrecipe.util.Constants.RECIPE_OBJECT;
import static com.example.android.bakingrecipe.util.Constants.STEP_DESCRIPTION;
import static com.example.android.bakingrecipe.util.Constants.STEP_VIDEO;

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailsFragment.onStepLoad, RecipeDetailsFragment.onStepClickMultipane,
        RecipeDbLoader.SendResult{

    private boolean multiPane;

    private Recipe recipe;
    private RecipeDbLoader recipeDbLoader;
    private boolean databaseIsEmpty;
    private final String DATABASE_STRING = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        multiPane = findViewById(R.id.ll_multipane_layout) != null;
        recipeDbLoader = new RecipeDbLoader(this);

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(RECIPE_OBJECT)){
            recipe = parentIntent.getExtras().getParcelable(RECIPE_OBJECT);
        }

        if(savedInstanceState == null) {

            Bundle arguments = new Bundle();

            databaseIsEmpty = false;
            checkRecipeInDb();

            if(parentIntent.hasExtra(RECIPE_OBJECT)){
                arguments.putParcelable(RECIPE_OBJECT, recipe);
                arguments.putBoolean(MULTIPANE_STRING, multiPane);
            }

            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.fl_recipe_details_container, recipeDetailsFragment)
                    .commit();

        }else{
            databaseIsEmpty = savedInstanceState.getBoolean(DATABASE_STRING, true);
            checkDatabaseStatus();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DATABASE_STRING, databaseIsEmpty);
    }

    private void checkDatabaseStatus(){
        Gson gson = new Gson();
        String json = gson.toJson(recipe);

        if(databaseIsEmpty){
            saveRecipe(json);
        }else{
            updateRecipe(json);
        }
    }

    private void checkRecipeInDb(){
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Recipe> fetchRecipeLoader = loaderManager.getLoader(RECIPE_LOADER_ID);

        if(fetchRecipeLoader == null){
            loaderManager.initLoader(RECIPE_LOADER_ID, null, recipeDbLoader);
        }
        else {
            loaderManager.restartLoader(RECIPE_LOADER_ID, null, recipeDbLoader);
        }
    }

    public void getFirstStep(String videoUrl, String description) {
        if(multiPane){
            Bundle arguments = new Bundle();
            arguments.putString(STEP_VIDEO, videoUrl);
            arguments.putString(STEP_DESCRIPTION, description);

            VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
            videoPlayerFragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.fl_video_player_container, videoPlayerFragment)
                    .commit();
        }
    }

    public void getStep(String videoUrl, String description) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle arguments = new Bundle();
        arguments.putString(STEP_VIDEO, videoUrl);
        arguments.putString(STEP_DESCRIPTION, description);
        videoPlayerFragment.setArguments(arguments);

        fragmentManager.beginTransaction()
                .replace(R.id.fl_video_player_container, videoPlayerFragment)
                .commit();
    }

    private void saveRecipe(String json){
        ContentValues recipeContentValues = new ContentValues();
        recipeContentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_GSON, json);

        getApplicationContext().getContentResolver()
                .insert(RecipeContract.RecipeEntry.CONTENT_URI, recipeContentValues);

    }

    private void updateRecipe(String json){
        ContentValues recipeContentValues = new ContentValues();
        recipeContentValues.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_GSON, json);

        getApplicationContext().getContentResolver()
                .update(RecipeContract.RecipeEntry.CONTENT_URI, recipeContentValues,
                        null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecipeAcquire(Cursor data) {
        databaseIsEmpty = data.getCount() == 0;
        checkDatabaseStatus();
    }
}
