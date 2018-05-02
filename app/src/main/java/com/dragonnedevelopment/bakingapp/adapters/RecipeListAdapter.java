package com.dragonnedevelopment.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * BakingApp Created by Muir on 01/05/2018.
 * <p>
 * Creates a list of recipe items to a RecyclerView
 */
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private RecipeListOnClickHandler onClickHandler;
    private Context context;
    private ArrayList<Recipe> recipeArrayList;

    /**
     * interface to receive onClick messages
     */
    public interface RecipeListOnClickHandler {
        void onClick(Recipe recipe);
    }

    /**
     * @param onClickHandler handles situations when a single item is clicked
     */
    public RecipeListAdapter(RecipeListOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ButterKnife bindings
        @BindView(R.id.imageview_recipe_icon)
        ImageView imageViewRecipeIcon;
        @BindView(R.id.textview_recipe_name)
        TextView textViewRecipeName;
        @BindView(R.id.textview_recipe_servings)
        TextView textViewRecipeServings;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * called when the child view is clicked
         *
         * @param view - the child view
         */
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipeArrayList.get(adapterPosition);
            onClickHandler.onClick(recipe);
        }
    }

    /**
     * called when a new ViewHolder gets created in the event of the RecyclerView being laid out.
     * This creates enough ViewHolders to fill up the screen and allow scrolling
     *
     * @return a new ViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int listItemLayoutId = R.layout.list_item_recipe;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(listItemLayoutId, parent, false);
        return new RecipeViewHolder(view);
    }

    /**
     * Used by the Recyclerview to list the recipes
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeListAdapter.RecipeViewHolder holder, int position) {
        if (position < getItemCount()) {
            Recipe recipe = recipeArrayList.get(position);
            String recipeName = recipe.getRecipeName();
            int servings = recipe.getRecipeServings();
            String imageUri = recipe.getRecipeImage();

            if (!Utils.isEmptyString(recipeName)) {
                // set recipe name
                holder.textViewRecipeName.setText(recipeName);

                // set the number of servings
                if (!Utils.isNumberZero(servings)) {
                    holder.textViewRecipeServings.setText(context.getString(R.string.display_servings, servings));
                } else {
                    holder.textViewRecipeServings.setText(context.getString(R.string.display_no_servings));
                }

                // set the recipe image if one is available, else display the default image
                if (!Utils.isEmptyString(imageUri)) {
                    Picasso.with(context)
                            .load(imageUri)
                            .placeholder(getImageResourceId(recipeName))
                            .error(getImageResourceId(recipeName))
                            .into(holder.imageViewRecipeIcon);
                } else {
                    holder.imageViewRecipeIcon.setImageResource(getImageResourceId(recipeName));
                }
            }
        }

    }

    /**
     * adds an icon resource to the list item depending on the recipe name
     *
     * @param recipeName the name of the recipe
     * @return the correct icon resource
     */
    public int getImageResourceId(String recipeName) {
        int imageResId;

        if (recipeName.contains(context.getString(R.string.recipe_pie))) {
            imageResId = R.drawable.ic_pie;
        } else if (recipeName.contains(context.getString(R.string.recipe_brownie))) {
            imageResId = R.drawable.ic_brownie;
        } else if (recipeName.contains(context.getString(R.string.recipe_cheesecake))) {
            imageResId = R.drawable.ic_cheesecake;
        } else if (recipeName.contains(context.getString(R.string.recipe_cake))) {
            imageResId = R.drawable.ic_cake;
        } else { // default if recipe name does not match anything else
            imageResId = R.drawable.ic_chef_hat;
        }

        return imageResId;
    }

    @Override
    public int getItemCount() {
        return (recipeArrayList == null) ? 0 : recipeArrayList.size();
    }

    /**
     * refreshes the list after the adapter has been created to avoid creating a new one
     *
     * @param recipeArrayList the recipe list
     */
    public void setRecipeData(ArrayList<Recipe> recipeArrayList) {
        this.recipeArrayList = recipeArrayList;
        notifyDataSetChanged();
    }


}
