package it.dindonkey.chucknorrisjokes.androidtest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class JokesActivityTest
{
    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(JokesActivity.class,
                                                                                         false,
            false);

    @Test
    public void should_show_jokes_list()
    {
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.jokes_list)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown()
    {
        mActivityRule.getActivity().finish();
    }
}
