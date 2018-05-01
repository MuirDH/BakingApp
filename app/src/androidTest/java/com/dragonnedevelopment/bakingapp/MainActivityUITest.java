package com.dragonnedevelopment.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dragonnedevelopment.bakingapp.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * BakingApp Created by Muir on 01/05/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    private final static String RECIPE_NAME = "Yellow Cake";
    private final static String STEP_NAME = "0) Recipe Introduction";
    private final static String STEP_DESCRIPTION = "Recipe Introduction";
    private final static String STEP_NUM_0 = "Step 0 of 12";
    private final static String STEP_NUM_1 = "Step 1 of 12";
    private final static int RECIPE_LIST_SCROLL_POSITION = 2;
    private final static int STEPS_WITH_MEDIA = 0;
    private final static int STEPS_WITHOUT_MEDIA = 1;

    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        idlingResource = activityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    /**
     * tests if correct recipe name is displayed on a specified position of the RecyclerView list.
     */
    @Test
    public void testRecipeNameAtPosition(){
        //perform scroll action on Recipe RecyclerView list
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions
                .scrollToPosition(RECIPE_LIST_SCROLL_POSITION));

        // check if recipe name, as displayed on the specified position of the Recipe RecyclerView list,
        // matches the given name
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));

    }

    /**
     * test to see if clicking on a given position (ID = 2) of the Recipe RecyclerView list displays
     * the correct ingredients and steps. Then test to see if clicking on a given position (ID = 0)
     * of the Step RecyclerView list displays the step description, media, and step number.
     * @throws Exception if the test fails
     */
    @Test
    public void testClickRecipeAtPosition() throws Exception{
        // perform a click action on the Recipe RecyclerView list
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));

        // check if the ingredients TextView is displayed
        onView(withId(R.id.ingredients_tv))
                .check(matches(isDisplayed()));

        // perform a scroll action on the Steps RecyclerView list
        onView(withId(R.id.steps_rv))
                .perform(RecyclerViewActions
                .scrollToPosition(STEPS_WITH_MEDIA));

        // check if the step name, as displayed on the speciifed position of the Steps Recyclerview list,
        // matches the given name
        onView(withText(STEP_NAME))
                .check(matches(isDisplayed()));

        // perform a click action on the steps list (ID = 0)
        onView(withId(R.id.steps_rv))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(STEPS_WITH_MEDIA, click()));

        // check if all the views are displayed as expected in the StepsActivity (after the step
        // item is clicked)
        onView(withId(R.id.previous_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.next_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.step_description_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.step_number_tv)).check(matches(isDisplayed()));
        onView(withId(R.id.step_description_tv)).check(matches(withText(STEP_DESCRIPTION)));
        onView(withId(R.id.step_number_tv)).check(matches(withText(STEP_NUM_0)));
        onView(withId(R.id.playerview_recipe_video)).check(matches(isDisplayed()));
        onView(withId(R.id.no_media_iv)).check(matches(not(isDisplayed())));
    }

    /**
     * test to see if clicking on a given position (ID = 2) of the Recipe RecyclerView list displays
     * the ingresdients and steps. Then test to see if clicking on a given position (ID = 1) of the
     * Step RecyclerView list displays the step description, default image (as this step does not
     * have any media attached to it), and the step number.
     * @throws Exception if the test fails
     */
    @Test
    public void testClickStepAtPosition() throws Exception {
        // perform click action on the Recipe RecyclerView list
        onView(withId(R.id.recipes_rv))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(RECIPE_LIST_SCROLL_POSITION, click()));

        // perform a scroll action on the Steps RecyclerView list
        onView(withId(R.id.steps_rv))
                .perform(RecyclerViewActions
                .scrollToPosition(STEPS_WITHOUT_MEDIA));
        onView(withId(R.id.steps_rv))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(STEPS_WITHOUT_MEDIA, click()));

        onView(withId(R.id.step_number_tv))
                .check(matches(withText(STEP_NUM_1)));
        onView(withId(R.id.no_media_iv))
                .check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource(){
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
