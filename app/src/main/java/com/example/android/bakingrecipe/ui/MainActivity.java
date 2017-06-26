package com.example.android.bakingrecipe.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, savedInstanceState == null ? "True" : "False", Toast.LENGTH_LONG).show();

        if(savedInstanceState == null) {

            RecipeListFragment recipeListFragment = new RecipeListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.fl_recipe_list_container, recipeListFragment)
                    .commit();

        }
    }

}
