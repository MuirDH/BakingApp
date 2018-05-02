package com.dragonnedevelopment.bakingapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.SimpleIdlingResource;
import com.dragonnedevelopment.bakingapp.activities.DetailActivity;
import com.dragonnedevelopment.bakingapp.activities.MainActivity;
import com.dragonnedevelopment.bakingapp.adapters.RecipeListAdapter;
import com.dragonnedevelopment.bakingapp.exceptions.NoConnectivityException;
import com.dragonnedevelopment.bakingapp.interfaces.RecipeInterface;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.network.RetroFitController;
import com.dragonnedevelopment.bakingapp.utils.Config;

import java.util.ArrayList;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * BakingApp Created by Muir on 16/04/2018.
 */
public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeListOnClickHandler{

    private MainActivity parentActivity;
    private RecipeListAdapter recipeListAdapter;
    private ArrayList<Recipe> recipeArrayList;
    private Unbinder unbinder;

    SimpleIdlingResource simpleIdlingResource;

    // ButterKnife bindings
    @BindView(R.id.recyclerview_recipes)
    RecyclerView recyclerViewRecipes;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.textview_empty_list)
    TextView textViewEmptyList;

    @BindInt(R.integer.grid_column_count) int gridColumnCount;

    @BindString(R.string.alert_recipe_load_failure) String recipeLoadFailureAlert;

    @BindString(R.string.error_recipe_load) String recipeLoadError;

    // Empty Constructor
    public RecipeListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        parentActivity = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        // Initialise RecyclerView for displaying recipes using a grid layout
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(parentActivity, gridColumnCount);
        recyclerViewRecipes.setLayoutManager(layoutManager);
        recyclerViewRecipes.setHasFixedSize(true);

        // Set Adapter
        recipeListAdapter = new RecipeListAdapter(this);
        recyclerViewRecipes.setAdapter(recipeListAdapter);

        recipeListAdapter.setRecipeData(recipeArrayList);
        recipeListAdapter.notifyDataSetChanged();

        simpleIdlingResource = (SimpleIdlingResource) parentActivity.getIdlingResource();
        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(false);
        }

        // load recipes data
        loadRecipeData();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Load data into the adapter and display it in the RecyclerView layout.
     * Display alert messages if there is no connectivity or if a failure happened while
     * fetching/loading data.
     */
    public void loadRecipeData() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            textViewEmptyList.setVisibility(View.GONE);

            RecipeInterface recipeInterface = RetroFitController.getClient(parentActivity).create(RecipeInterface.class);

            final Call<ArrayList<Recipe>> recipeListCall = recipeInterface.getRecipe();

            recipeListCall.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                    int statusCode = response.code();

                    if (response.isSuccessful()) {
                        postDataLoad(true, "");

                        recipeArrayList = response.body();
                        MainActivity.recipeArrayList = recipeArrayList;
                        recipeListAdapter.setRecipeData(recipeArrayList);
                        recipeListAdapter.notifyDataSetChanged();
                    }else {
                        postDataLoad(false, recipeLoadFailureAlert);
                        Timber.e(recipeLoadError, statusCode);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable throwable) {
                    postDataLoad(false, recipeLoadFailureAlert);
                    Timber.e(throwable.getMessage());
                }
            });
        }catch (NoConnectivityException noConnectivityException){
            postDataLoad(false, recipeLoadFailureAlert);
            Timber.e(noConnectivityException.getMessage());
        }
    }

    /**
     * Show or hide the progress bar and empty list message depending on whether or not data is
     * retrieved
     * @param isLoadSuccessful boolean which checks if the data has been retrieved and loaded
     * @param message string which is displayed if the response is successful
     */
    private void postDataLoad(boolean isLoadSuccessful, String message) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        if (isLoadSuccessful) {
            textViewEmptyList.setVisibility(View.GONE);
        }else {
            textViewEmptyList.setText(message);
            textViewEmptyList.setVisibility(View.VISIBLE);
        }

        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(isLoadSuccessful);
        }
    }

    @Override
    public void onClick(Recipe recipe) {
        Bundle bundleRecipe = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipe);
        bundleRecipe.putParcelableArrayList(Config.INTENT_KEY_SELECTED_RECIPE, selectedRecipe);

        Intent intent = new Intent(parentActivity, DetailActivity.class);
        intent.putExtras(bundleRecipe);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
