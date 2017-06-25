package com.example.android.bakingrecipe.network;

import com.example.android.bakingrecipe.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
    @GET("/android-baking-app-json")
    Call<ArrayList<Recipe>> getRecipes();
}
