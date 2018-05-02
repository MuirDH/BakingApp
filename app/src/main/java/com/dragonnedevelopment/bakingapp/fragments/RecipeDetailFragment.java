package com.dragonnedevelopment.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.activities.DetailActivity;
import com.dragonnedevelopment.bakingapp.adapters.StepsAdapter;
import com.dragonnedevelopment.bakingapp.models.Ingredient;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.models.Step;
import com.dragonnedevelopment.bakingapp.utils.Config;
import com.dragonnedevelopment.bakingapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BakingApp Created by Muir on 30/04/2018.
 */
public class RecipeDetailFragment extends Fragment implements StepsAdapter.StepsOnClickHandler {

    private DetailActivity parentActivity;
    private Unbinder unbinder;
    private Recipe selectedRecipe;
    private StepsAdapter stepsAdapter;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    public Integer selectedStepId;

    @BindView(R.id.recyclerview_steps)
    RecyclerView recyclerViewSteps;

    @BindView(R.id.textview_ingredients)
    TextView textViewIngredients;

    @BindBool(R.bool.two_pane_layout)
    boolean isTwoPaneLayout;

    @BindString(R.string.display_ingredient)
    String displayIngredient;
    @BindString(R.string.error_missing_callback)
    String errorMissingCallback;

    private OnListItemClickListener callBack;

    public void setArguments(Bundle recipeBundle) {
    }

    @Override
    public void onClick(Step step) {
        selectedStepId = step.getStepId();
        callBack.onItemSelected(selectedStepId);
        stepsAdapter.setSelected(selectedStepId);
    }

    // calls a method in the host activity named onItemSelected
    public interface OnListItemClickListener {
        void onItemSelected(int stepId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // ensure that the host activity has implemented the callback interface or throw an exception
        try {
            callBack = (OnListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format(errorMissingCallback, context.toString()));
        }
    }

    /**
     * Empty constructor
     */
    public RecipeDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentActivity = (DetailActivity) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            ArrayList<Recipe> recipes = getArguments().getParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE);
            selectedStepId = getArguments().getInt(Config.INTENT_KEY_SELECTED_STEP);

            selectedRecipe = recipes.get(0);


            ingredients = selectedRecipe.getRecipeIngredients();
            steps = selectedRecipe.getRecipeSteps();

            displayRecipeIngredients();
            displayRecipeSteps();
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * display the steps of a recipe in a recycler view
     */
    private void displayRecipeSteps() {
        // initialise the recycler view to display the steps of a recipe using a LinearLayout
        RecyclerView.LayoutManager layoutManagerSteps = new LinearLayoutManager(parentActivity);
        recyclerViewSteps.setLayoutManager(layoutManagerSteps);

        // set the adapter
        stepsAdapter = new StepsAdapter(parentActivity, this);
        recyclerViewSteps.setAdapter(stepsAdapter);
        stepsAdapter.setStepsData(steps);

        if (selectedStepId == null) {
            stepsAdapter.setSelected(0);
        } else {
            stepsAdapter.setSelected(selectedStepId);
        }
    }

    /**
     * displays the Recipe Ingredients in a RecylerView
     */
    private void displayRecipeIngredients() {
        StringBuilder ingredientDisplayString = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            ingredientDisplayString.append(
                    String.format(
                            displayIngredient,
                            Utils.convertStringToFirstCapital(ingredient.getIngredient()),
                            Double.toString(ingredient.getIngredientQuantity()),
                            ingredient.getIngredientMeasure().toLowerCase()
                    )
            );
        }

        textViewIngredients.setText(ingredientDisplayString.toString());
    }


}
