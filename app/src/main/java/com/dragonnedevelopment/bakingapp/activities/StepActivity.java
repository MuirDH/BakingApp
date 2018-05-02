package com.dragonnedevelopment.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.fragments.RecipeStepDetailFragment;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.utils.Config;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * BakingApp Created by Muir on 16/04/2018.
 */
public class StepActivity extends AppCompatActivity {

    private Recipe recipe;
    private int stepId;
    private int stepCount;
    private Bundle stepBundle;
    private ArrayList<Recipe> selectedRecipe;
    private FragmentManager fragmentManager;

    @BindView(R.id.button_previous)
    ImageButton previousButton;

    @BindView(R.id.button_next)
    ImageButton nextButton;

    @BindView(R.id.textview_step_number)
    TextView textViewStepNumber;

    @BindString(R.string.display_stepnum)
    String displayStepNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            stepId = extras.getInt(Config.INTENT_KEY_SELECTED_STEP);
            stepCount = extras.getInt(Config.INTENT_KEY_STEP_COUNT);
        }

        recipe = DetailActivity.recipe.get(0);
        setTitle(recipe.getRecipeName());

        fragmentManager = getSupportFragmentManager();
        displayStepNum();

        // create fragment instance only once
        if (savedInstanceState == null) {
            displayStepFragment();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Config.STATE_SELECTED_STEP, stepId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            stepId = savedInstanceState.getInt(Config.STATE_SELECTED_STEP);
            displayStepNum();
        } else {
            displayStepFragment();
        }
    }

    /**
     * OnClick handlers for step navigation buttons
     *
     * @param view the previous and next buttons
     */
    @OnClick({R.id.button_previous, R.id.button_next})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.button_previous:
                submitPrevious();
                break;

            case R.id.button_next:
                submitNext();
                break;
        }
    }

    /**
     * navigates to previous step of the selected recipe, if there is one.
     */
    public void submitPrevious() {
        if (stepId > 0) {
            stepId--;
            displayStepNum();
            displayStepFragment();
        }
    }

    /**
     * navigates to the next step of the selected recipe, if there is one.
     */
    public void submitNext() {
        if (stepId < (stepCount - 1)) {
            stepId++;
            displayStepNum();
            displayStepFragment();
        }
    }

    // replaces fragment with new step details
    public void displayStepFragment() {
        stepBundle = new Bundle();
        selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipe);
        stepBundle.putParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE, selectedRecipe);
        stepBundle.putInt(Config.INTENT_KEY_SELECTED_STEP, stepId);
        stepBundle.putInt(Config.INTENT_KEY_STEP_COUNT, stepCount);

        RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();

        stepDetailFragment.setArguments(stepBundle);
        fragmentManager
                .beginTransaction()
                .replace(R.id.container_recipe_step_detail, stepDetailFragment)
                .commit();
    }

    // displays the current step number
    public void displayStepNum() {
        textViewStepNumber.setText(String.format(displayStepNumber, stepId, (stepCount - 1)));
    }

}
