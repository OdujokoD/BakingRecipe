package com.example.android.bakingrecipe.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.android.bakingrecipe.data.RecipeContract;
import com.example.android.bakingrecipe.model.Recipe;
import com.google.gson.Gson;

public class RecipeWidgetService extends IntentService{

    public static final String ACTION_RETRIEVE_RECIPE = "com.example.android.bakingrecipe.action.retrieve_recipe";
    private Recipe recipe;
    static Context mContext;

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    public static void startActionRetrieveRecipe(Context context) {
        mContext = context;
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_RETRIEVE_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_RETRIEVE_RECIPE.equals(action)) {
                handleActionRetrieveRecipe();
            }
        }
    }

    public Recipe handleActionRetrieveRecipe() {
        recipe = new Recipe();

        return parseRecipeCursor();
    }

    private Recipe parseRecipeCursor(){
        Cursor recipeCursor = mContext.getContentResolver().query(
                RecipeContract.RecipeEntry.CONTENT_URI, null, null, null, null);

        if (recipeCursor != null && recipeCursor.getCount() > 0) {
            recipeCursor.moveToFirst();
            String recipeJSON = recipeCursor.getString(recipeCursor.getColumnIndex(
                    RecipeContract.RecipeEntry.COLUMN_RECIPE_GSON));
            recipeCursor.close();

            Gson gson = new Gson();
            recipe = gson.fromJson(recipeJSON, Recipe.class);
        }

        return recipe;
    }
}
