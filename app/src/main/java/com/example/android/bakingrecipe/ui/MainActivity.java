package com.example.android.bakingrecipe.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingrecipe.R;

import static com.example.android.bakingrecipe.util.Constants.MULTIPANE_STRING;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean multiPane = findViewById(R.id.ll_multipane_layout) != null;

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putBoolean(MULTIPANE_STRING, multiPane);
            RecipeListFragment recipeListFragment = new RecipeListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            recipeListFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .add(R.id.fl_recipe_list_container, recipeListFragment)
                    .commit();

        }
    }
}
