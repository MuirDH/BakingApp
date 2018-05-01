package com.dragonnedevelopment.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dragonnedevelopment.bakingapp.activities.DetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * BakingApp Created by Muir on 01/05/2018.
 */

@RunWith(AndroidJUnit4.class)
public class DetailActivityUITest {

    @Rule
    public ActivityTestRule<DetailActivity> activityTestRule = new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void testRecipeDetailContainer_Visibility(){
        onView(withId(R.id.container_recipe_detail))
                .check(matches(isDisplayed()));


    }
}
