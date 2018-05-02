package com.dragonnedevelopment.bakingapp.interfaces;

import com.dragonnedevelopment.bakingapp.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * BakingApp Created by Muir on 02/05/2018.
 *
 * Define the endpoints which includes details of request methods (GET, POST, etc) and parameters.
 */
public interface RecipeInterface {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
