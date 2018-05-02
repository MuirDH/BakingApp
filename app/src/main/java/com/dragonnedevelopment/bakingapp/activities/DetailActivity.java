package com.dragonnedevelopment.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.fragments.RecipeDetailFragment;
import com.dragonnedevelopment.bakingapp.fragments.RecipeStepDetailFragment;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.utils.Config;
import com.dragonnedevelopment.bakingapp.utils.Utils;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.ButterKnife;

/**
 * BakingApp Created by Muir on 16/04/2018.
 */
public class DetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnListItemClickListener {

    public static ArrayList<Recipe> recipe;
    private Bundle recipeBundle;
    private FragmentManager fragmentManager;

    @BindBool(R.bool.two_pane_layout)
    boolean isTwoPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            recipeBundle = getIntent().getExtras();
            recipe = recipeBundle.getParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE);
        }

        // exit early if selected recipe is null or has no name
        if ((recipe == null) || (Utils.isEmptyString(recipe.get(0).getRecipeName()))) return;
        setTitle(recipe.get(0).getRecipeName());

        // create an instance of FragmentManager
        fragmentManager = getSupportFragmentManager();

        // only create new fragments when there is no previously saved state
        if (savedInstanceState == null) {
            if (isTwoPaneLayout) {
                // create fragment instance for ingredients and steps
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                recipeBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, 0);
                recipeDetailFragment.setArguments(recipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_detail, recipeDetailFragment)
                        .addToBackStack(Config.STACK_RECIPE_DETAIL)
                        .commit();

                // Create fragment instance for Step Details
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, 0);
                recipeStepDetailFragment.setArguments(recipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_step_detail, recipeStepDetailFragment)
                        .addToBackStack(Config.STACK_RECIPE_STEP_DETAIL)
                        .commit();
            } else {
                // create fragment instance for ingredients and steps
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                recipeDetailFragment.setArguments(recipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_recipe_detail, recipeDetailFragment)
                        .addToBackStack(Config.STACK_RECIPE_DETAIL)
                        .commit();
            }
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(int stepId) {
        int stepsCount = recipe.get(0).getRecipeSteps().size();

        if (isTwoPaneLayout) {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE, recipe);
            stepBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, stepId);
            stepBundle.putInt(Config.INTENT_KEY_STEP_COUNT, stepsCount);

            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(stepBundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_recipe_detail, recipeDetailFragment)
                    .commit();

            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(stepBundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_recipe_step_detail, recipeStepDetailFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(Config.INTENT_KEY_SELECTED_STEP, stepId);
            intent.putExtra(Config.INTENT_KEY_STEP_COUNT, stepsCount);
            startActivity(intent);
        }
    }
}
