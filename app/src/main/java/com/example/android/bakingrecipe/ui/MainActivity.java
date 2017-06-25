package com.example.android.bakingrecipe.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.RecipeAdapter;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.network.NetworkSetup;
import com.example.android.bakingrecipe.network.RecipeService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeAdapterOnClickListener{

    @BindView(R.id.rv_recipe)
    RecyclerView mRecipe;


    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecipe.setLayoutManager(layoutManager);
        mRecipe.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(this);
        mRecipe.setAdapter(recipeAdapter);
        fetchRecipes();
    }

    @Override
    public void recipeOnClick(Recipe currentRecipe) {

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
}
