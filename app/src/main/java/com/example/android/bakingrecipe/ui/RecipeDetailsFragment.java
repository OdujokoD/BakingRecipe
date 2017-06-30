package com.example.android.bakingrecipe.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.adapter.StepAdapter;
import com.example.android.bakingrecipe.model.Ingredient;
import com.example.android.bakingrecipe.model.Recipe;
import com.example.android.bakingrecipe.model.Step;

import java.util.ArrayList;

import static com.example.android.bakingrecipe.util.Constants.RECIPE_OBJECT;
import static com.example.android.bakingrecipe.util.Constants.STEP_DESCRIPTION;
import static com.example.android.bakingrecipe.util.Constants.STEP_VIDEO;

public class RecipeDetailsFragment extends Fragment
        implements StepAdapter.StepAdapterOnClickListener{

    private StepAdapter stepAdapter;
    private Recipe recipe;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> ingredients;

    private TextView mIngredients;
    private onStepLoad mCallback;

    public RecipeDetailsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        mIngredients = (TextView)rootView.findViewById(R.id.tv_ingredient_list);
        RecyclerView mStepList = (RecyclerView)rootView.findViewById(R.id.rv_recipe_steps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        mStepList.setLayoutManager(layoutManager);
        mStepList.setHasFixedSize(true);

        stepAdapter = new StepAdapter(this);
        mStepList.setAdapter(stepAdapter);

        if(savedInstanceState != null){
            recipe = savedInstanceState.getParcelable(RECIPE_OBJECT);
            mSteps = recipe.getSteps();
            ingredients = recipe.getIngredients();
        }else{
            if (getArguments() != null) {
                recipe = getArguments().getParcelable(RECIPE_OBJECT);
                mSteps = recipe.getSteps();
                ingredients = recipe.getIngredients();
            }
        }

        loadIngredient(ingredients);
        loadSteps(mSteps);


        return rootView;
    }

    interface onStepLoad{
        void getFirstStep(String videoUrl, String description);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (onStepLoad) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Override
    public void stepOnClick(String videoURL, String description) {
        if(videoURL.length() <= 0){
            Toast.makeText(getActivity(), "No video available for this step.", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra(STEP_VIDEO, videoURL);
            intent.putExtra(STEP_DESCRIPTION, description);
            startActivity(intent);
        }
    }

    private void loadIngredient(ArrayList<Ingredient> ingredients){
        String quantity;
        String measure;
        String name;

        String formattedIngredient = "";

        for (Ingredient ingredient: ingredients) {
            quantity = String.valueOf(ingredient.getQuantity());
            measure = ingredient.getMeasure();
            name = ingredient.getIngredient();

            formattedIngredient += "* " + quantity + " " + measure + " " + name + "\n\n";
        }

        mIngredients.setText(formattedIngredient);

    }

    private void loadSteps(ArrayList<Step> steps){
        mSteps = steps;
        stepAdapter.setStep(steps);
        if(steps != null) {
            mCallback.getFirstStep(steps.get(0).getVideoURL(), steps.get(0).getDescription());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_OBJECT, recipe);
    }
}
