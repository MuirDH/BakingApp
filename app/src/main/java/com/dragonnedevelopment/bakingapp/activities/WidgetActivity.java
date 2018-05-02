package com.dragonnedevelopment.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.models.Ingredient;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.utils.Config;
import com.dragonnedevelopment.bakingapp.utils.Utils;
import com.dragonnedevelopment.bakingapp.widget.BakingWidgetService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * BakingApp Created by Muir on 16/04/2018.
 */
public class WidgetActivity extends AppCompatActivity {

    final Context context = this;
    private RadioButton radioButton;
    private RadioButton[] radioButtons;
    private RadioGroup radioGroupRecipeOptions;
    private Button button;
    private AppWidgetManager appWidgetManager;
    private Toast toast;
    private int appWidgetId;
    private RadioGroup.LayoutParams layoutParams;
    public ArrayList<Recipe> recipeArrayList;
    private String[] widgetRecipe;
    int previousRecipeId;
    private List<Ingredient> ingredients;

    // Butterknife resource binding
    @BindString(R.string.display_ingredient)
    String displayIngredient;
    @BindString(R.string.app_widget_text)
    String widgetDefaultText;
    @BindString(R.string.info_ingredients_saved)
    String ingredientsSavedInformation;
    @BindString(R.string.info_no_chosen_recipe)
    String noRecipeChosenInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);

        if (MainActivity.recipeArrayList == null) {
            startActivity(new Intent(this, MainActivity.class));
            Utils.showToastMessage(context, toast, getString(R.string.alert_app_launch)).show();
            finish();
        }

        recipeArrayList = MainActivity.recipeArrayList;
        widgetRecipe = new String[3];

        // retrieve intent extras
        processIntentExtras();

        //populate the Radio Options
        displayRecipeOptions();

        // process widget
        appWidgetManager = AppWidgetManager.getInstance(context);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processWidgetRecipe();
            }
        });
    }

    /**
     * displays the recipe options to choose from in the form of radio buttons
     */
    public void displayRecipeOptions() {
        radioGroupRecipeOptions = findViewById(R.id.radiogroup_recipe_options);
        button = findViewById(R.id.button_choose_recipe);
        layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        radioButtons = new RadioButton[recipeArrayList.size()];

        int i = 0;
        for (Recipe recipe : recipeArrayList) {
            radioButtons[i] = new RadioButton(this);
            radioGroupRecipeOptions.addView(radioButtons[i]);
            radioButtons[i].setText(recipe.getRecipeName());
            radioButtons[i].setTag(recipe.getRecipeId());
            layoutParams.setMargins(20, 20, 20, 20);
            radioButtons[i].setLayoutParams(layoutParams);
            radioButtons[i].setPadding(40, 0, 0, 0);

            if (previousRecipeId != 0 && previousRecipeId == recipe.getRecipeId()) {
                radioButtons[i].setChecked(true);
            }
        }
    }

    /**
     * processes the selected recipe ingredients to be displayed in the widget
     */
    public void processWidgetRecipe() {
        int selectId = radioGroupRecipeOptions.getCheckedRadioButtonId();
        radioButton = findViewById(selectId);

        if (radioButton != null) {
            // recipe id
            widgetRecipe[0] = radioButton.getTag().toString();

            // recipe name
            widgetRecipe[1] = radioButton.getText().toString();

            // recipe ingredients
            int id = Integer.valueOf(widgetRecipe[0]) - 1;
            ingredients = recipeArrayList.get(id).getRecipeIngredients();

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

            widgetRecipe[2] = ingredientDisplayString.toString();

            Utils.showToastMessage(this, toast, ingredientsSavedInformation).show();

            BakingWidgetService.startActionUpdateWidget(context, widgetRecipe);
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        } else {
            widgetRecipe[0] = "0";
            widgetRecipe[1] = "";
            widgetRecipe[2] = widgetDefaultText;

            Utils.showToastMessage(this, toast, noRecipeChosenInformation).show();
        }
    }

    /**
     * receive Intent Extras passed on from the home screen Widget
     */
    private void processIntentExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            String[] previousRecipe = bundle.getStringArray(Config.INTENT_KEY_WIDGET_RECIPE);
            previousRecipeId = (previousRecipe != null) ? Integer.valueOf(previousRecipe[0]) : 0;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntentExtras();
    }
}
