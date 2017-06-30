package com.example.android.bakingrecipe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingrecipe.R;
import com.example.android.bakingrecipe.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewAdapter>{

    private ArrayList<Recipe> recipes;
    private final RecipeAdapterOnClickListener mClickHandler;
    private Context context;

    public RecipeAdapter(RecipeAdapterOnClickListener clickListener){
        mClickHandler = clickListener;
    }

    @Override
    public RecipeAdapterViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_content;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new RecipeAdapterViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewAdapter holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (this.recipes == null ) return 0;
        return this.recipes.size();
    }

    private Recipe getRecipeByPosition(int position){
        return recipes.get(position);
    }

    public interface RecipeAdapterOnClickListener{
        void recipeOnClick(Recipe currentRecipe);
    }

    public void setRecipe(ArrayList<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeAdapterViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView mRecipeName;
        private final TextView mIngredientCount;
        private final TextView mStepCount;
        private final TextView mServings;
        private ImageView mRecipeImage;

        RecipeAdapterViewAdapter(View itemView) {
            super(itemView);
            mRecipeName = (TextView)itemView.findViewById(R.id.tv_recipe_name);
            mIngredientCount = (TextView)itemView.findViewById(R.id.tv_recipe_ingredient_count);
            mStepCount = (TextView)itemView.findViewById(R.id.tv_recipe_step_count);
            mServings = (TextView)itemView.findViewById(R.id.tv_recipe_serving_count);
            mRecipeImage = (ImageView)itemView.findViewById(R.id.iv_recipe_image);
            itemView.setOnClickListener(this);
        }

        void bind(int position){
            Recipe recipe = recipes.get(position);
            String recipeName = recipe.getName();
            String ingredientCount = recipe.getIngredients().size() + " Ingredients";
            String stepCount = recipe.getSteps().size() + " Steps";
            String servings = recipe.getServings()  + " Servings";
            String imageUrl = recipe.getImage();

            mRecipeName.setText(recipeName);
            mIngredientCount.setText(ingredientCount);
            mStepCount.setText(stepCount);
            mServings.setText(servings);

            if(imageUrl.length() == 0) {
                Picasso.with(context)
                        .load(R.drawable.recipe)
                        .into(mRecipeImage);
            }else {
                Picasso.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.recipe)
                        .error(R.drawable.recipe)
                        .into(mRecipeImage);
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.recipeOnClick(getRecipeByPosition(adapterPosition));
        }
    }
}
