package com.example.android.bakingrecipe.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.RecipeAdapter;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.network.NetworkSetup;
import com.example.android.bakingrecipe.network.RecipeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment
        implements RecipeAdapter.RecipeAdapterOnClickListener{

    private RecipeAdapter recipeAdapter;
    private static final String RECIPE_OBJECT = "recipe";
    private ArrayList<Recipe> mRecipe;

    public RecipeListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        RecyclerView mRecipeList = (RecyclerView)rootView.findViewById(R.id.rv_recipe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        mRecipeList.setLayoutManager(layoutManager);
        mRecipeList.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(this);
        mRecipeList.setAdapter(recipeAdapter);

        if(savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelableArrayList(RECIPE_OBJECT);
            loadRecipes(mRecipe);
        } else{
            fetchRecipes();
        }

        return rootView;
    }

    private void fetchRecipes(){
        RecipeService service = NetworkSetup.setupNetwork.create(RecipeService.class);
        service.getRecipes()
                .enqueue(new Callback<ArrayList<Recipe>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Recipe>> call,
                                           Response<ArrayList<Recipe>> response) {
                        loadRecipes(response.body());
                        Log.d("SUCCESS", response.body().toString());
                        //hideProgressBar();
                        //showFab();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                        //showNetworkError();
                        Log.d("LOG_TAG", t.getMessage());
                    }
                });
    }

    private void loadRecipes(ArrayList<Recipe> recipes){
        recipeAdapter.setRecipe(recipes);
    }

    @Override
    public void recipeOnClick(Recipe currentRecipe) {
        Toast.makeText(getActivity(), currentRecipe.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPE_OBJECT, mRecipe);
    }
}
