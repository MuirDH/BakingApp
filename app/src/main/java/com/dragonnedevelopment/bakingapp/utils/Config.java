package com.dragonnedevelopment.bakingapp.utils;

/**
 * BakingApp Created by Muir on 30/04/2018.
 */
public class Config {

    // Recipe URL
    public static final String RECIPE_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    // Duration in Milliseconds after which connection times out
    public static final int DURATION_CONNECTION_TIMEOUT = 150000;

    // Keys and constants
    public static final String INTENT_KEY_SELECTED_RECIPE = "selected_recipe";
    public static final String INTENT_KEY_SELECTED_STEP = "selected_step";
    public static final String INTENT_KEY_STEP_COUNT = "step_count";
    public static final String INTENT_KEY_WIDGET_INGREDIENTS = "widget_ingredients";
    public static final String INTENT_KEY_WIDGET_RECIPE = "widget_recipe";
    public static final String PREFERENCE_KEY_STEP_SELECTED = "preference_step_selected";
    public static final String PREFERENCE_KEY_RECIPE = "preference_recipe";
    public static final String STATE_SELECTED_STEP = "state_step";
    public static String STACK_RECIPE_DETAIL = "STACK_RECIPE_DETAIL";
    public static String STACK_RECIPE_STEP_DETAIL = "STACK_RECIPE_STEP_DETAIL";
    public static final String STATE_PLAYER_POSITION = "player_position";

}
