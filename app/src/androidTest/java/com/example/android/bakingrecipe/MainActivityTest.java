package com.example.android.bakingrecipe;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingrecipe.ui.MainActivity;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.OkHttpClient;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private IdlingResource idlingResource;
    private static final String RECIPE_NAME = "Nutella Pie";

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        OkHttpClient client = new OkHttpClient();
        idlingResource = OkHttp3IdlingResource.create("okhttp", client);
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void isRecyclerViewDisplayed(){
        onView(withId(R.id.rv_recipe)).check(matches(isDisplayed()));
    }

    @Test
    public void displayRecipeDetail_OnClickOfView(){
        onView(withRecyclerView(R.id.rv_recipe).atPosition(0)).perform(click());
        onView(withId(R.id.fl_recipe_details_container)).check(matches(isDisplayed()));

    }

    @Test
    public void recyclerView_scrollToPosition() {

        onView(withRecyclerView(R.id.rv_recipe).atPosition(0)).perform(click());
    }

    @Test
    public void getRecipeName_OnClickOfView() {

        onView(withRecyclerView(R.id.rv_recipe).atPosition(0))
                .check(matches(hasDescendant(withText(RECIPE_NAME))));
    }


    @After
    public void unRegisterIdlingResource(){
        if(idlingResource != null){
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

}
