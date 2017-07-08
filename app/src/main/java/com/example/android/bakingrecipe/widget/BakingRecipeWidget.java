package com.example.android.bakingrecipe.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.ui.RecipeDetailActivity;

import java.util.ArrayList;

import static com.example.android.bakingrecipe.util.Constants.RECIPE_OBJECT;

public class BakingRecipeWidget extends AppWidgetProvider  {

    private static RecipeWidgetService recipeWidgetService = new RecipeWidgetService();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Recipe recipe = recipeWidgetService.handleActionRetrieveRecipe();
        ArrayList<Ingredient> ingredients = recipe.getIngredients();
        String servings = String.valueOf(recipe.getServings()) + " servings";
        String quantity;
        String measure;
        String name;

        String formattedIngredient = "";

        for (Ingredient ingredient: ingredients) {
            quantity = String.valueOf(ingredient.getQuantity());
            measure = ingredient.getMeasure();
            name = ingredient.getIngredient();

            formattedIngredient += "* " + quantity + " " + measure + " " + name + "\n";
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_recipe_widget);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());
        views.setTextViewText(R.id.tv_widget_serving_count, servings);
        views.setTextViewText(R.id.tv_widget_recipe_ingredient, formattedIngredient);

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_OBJECT, recipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.ll_widget_container, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetService.startActionRetrieveRecipe(context);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

