package com.dragonnedevelopment.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.dragonnedevelopment.bakingapp.activities.DetailActivity;
import com.dragonnedevelopment.bakingapp.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

/**
 * BakingApp Created by Muir on 01/05/2018.
 */
public class DetailActivityIntentTest {

    private final static String RECIPE_NAME = "Nutella Pie";
    private IdlingResource idlingResource;

    @Rule
    public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResources() {
        idlingResource = activityRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @Before
    public void stubAllExternalIntents(){
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    /**
     * checks if an intent is launched to open the detail activity. If in two pane mode, it checks
     * if the player view is visible. If it isn't, it checks if the detail activity is available.
     */
    @Test
    public void launchDetailActivityIntent() {

        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(RECIPE_NAME)), click()));

        Context targetContext = InstrumentationRegistry.getTargetContext();
        Boolean isTwoPane = targetContext.getResources().getBoolean(R.bool.two_pane_layout);

        if (isTwoPane) {
            // Check if video player container is present
            onView(withId(R.id.player_rl)).check(matches(isDisplayed()));
        } else {
            // Check if DetailActivity opens
            intended(hasComponent(DetailActivity.class.getName()));
        }

    }

    @After
    public void unregisterIdlingResource(){
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

}
