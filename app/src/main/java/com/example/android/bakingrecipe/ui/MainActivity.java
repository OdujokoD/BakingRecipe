package com.example.android.bakingrecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import static com.example.android.bakingrecipe.util.Constants.RECIPE_OBJECT;

public class MainActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeAdapterOnClickListener{

    @BindView(R.id.rv_recipe)
    RecyclerView mRecipeList;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.ll_network_error)
    LinearLayout mNetworkErrorContainer;
    @BindView(R.id.tv_network_error_message)
    TextView mNetworkMessage;
    @BindView(R.id.btn_retry)
    TextView mRetryNetworkConnection;

    private RecipeAdapter recipeAdapter;
    private ArrayList<Recipe> mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        boolean multiPane = findViewById(R.id.ll_multipane_layout) != null;

        if(multiPane){
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mRecipeList.setLayoutManager(layoutManager);
        }else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);

            mRecipeList.setLayoutManager(layoutManager);
        }

        mRecipeList.setHasFixedSize(true);

        recipeAdapter = new RecipeAdapter(this);
        mRecipeList.setAdapter(recipeAdapter);

        if(savedInstanceState != null){
            mRecipe = savedInstanceState.getParcelableArrayList(RECIPE_OBJECT);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            loadRecipes(mRecipe);
        } else{
            mLoadingIndicator.setVisibility(View.VISIBLE);
            fetchRecipes();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPE_OBJECT, mRecipe);
    }

    private void fetchRecipes(){
        RecipeService service = NetworkSetup.setupNetwork.create(RecipeService.class);
        service.getRecipes()
                .enqueue(new Callback<ArrayList<Recipe>>() {

                    @Override
                    public void onResponse(Call<ArrayList<Recipe>> call,
                                           Response<ArrayList<Recipe>> response) {
                        loadRecipes(response.body());
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                        hideErrorMessage();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                        showErrorMessage();
                    }
                });
    }

    private void loadRecipes(ArrayList<Recipe> recipes){
        mRecipe = recipes;
        recipeAdapter.setRecipe(recipes);
    }

    @Override
    public void recipeOnClick(Recipe currentRecipe) {
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
        detailIntent.putExtra(RECIPE_OBJECT, currentRecipe);
        startActivity(detailIntent);
    }

    private void hideErrorMessage(){
        mNetworkErrorContainer.setVisibility(View.INVISIBLE);
        mRecipeList.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        String errorMessage = "Oops!..\nWe can't seem to connect to the network.";
        mNetworkErrorContainer.setVisibility(View.VISIBLE);
        mRecipeList.setVisibility(View.INVISIBLE);
        mNetworkMessage.setText(errorMessage);
        mRetryNetworkConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideErrorMessage();
                mLoadingIndicator.setVisibility(View.VISIBLE);
                fetchRecipes();
            }
        });
    }
}
