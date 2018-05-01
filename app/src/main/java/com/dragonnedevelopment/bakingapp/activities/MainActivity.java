package com.dragonnedevelopment.bakingapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dragonnedevelopment.bakingapp.BaseApplication;
import com.dragonnedevelopment.bakingapp.R;
import com.dragonnedevelopment.bakingapp.SimpleIdlingResource;
import com.dragonnedevelopment.bakingapp.fragments.RecipeListFragment;
import com.dragonnedevelopment.bakingapp.models.Recipe;
import com.dragonnedevelopment.bakingapp.network.ConnectivityReceiver;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    final Context context = this;
    private RecipeListFragment recipeListFragment;
    public static ArrayList<Recipe> recipeArrayList;

    private SimpleIdlingResource idlingResource;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindString(R.string.alert_connectivity_status_ok) String connectivityOk;
    @BindString(R.string.alert_connectivity_status_not_ok) String connectivityNotOk;

    /**
     * Only called from test.
     * It creates and returns a new {@link SimpleIdlingResource}
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recipeListFragment =(RecipeListFragment) getFragmentManager().findFragmentById(R.id.fragment_recipe_list);

        // Get IdlingResource instance
        getIdlingResource();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.setReceiverStatus(true);
        BaseApplication.setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseApplication.setReceiverStatus(false);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        showSnack(isConnected);
        if (isConnected) {
            recipeListFragment.loadRecipeData();
        }
    }

    private void showSnack(boolean isConnected) {
        String message;
        message = (isConnected) ? connectivityOk : connectivityNotOk;
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_action_refresh:
                recipeListFragment.loadRecipeData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
