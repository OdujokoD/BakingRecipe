package com.example.android.bakingrecipe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.android.bakingrecipe.R;

import static com.example.android.bakingrecipe.util.Constants.RECIPE_OBJECT;
import static com.example.android.bakingrecipe.util.Constants.STEP_DESCRIPTION;
import static com.example.android.bakingrecipe.util.Constants.STEP_VIDEO;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailsFragment.onStepLoad{

    private boolean multiPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        multiPane = findViewById(R.id.ll_multipane_layout) != null;

        if(savedInstanceState == null) {

            Bundle arguments = new Bundle();
            Intent parentIntent = getIntent();

            if(parentIntent.hasExtra(RECIPE_OBJECT)){
                arguments.putParcelable(RECIPE_OBJECT, parentIntent.getExtras().getParcelable(RECIPE_OBJECT));
            }

            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setArguments(arguments);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.fl_recipe_details_container, recipeDetailsFragment)
                    .commit();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
