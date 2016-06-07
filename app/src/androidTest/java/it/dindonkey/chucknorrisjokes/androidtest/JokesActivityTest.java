package it.dindonkey.chucknorrisjokes.androidtest;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class JokesActivityTest
{
    public static final List<Joke> TEST_JOKES = Collections.singletonList(new Joke(1, "test joke"));
    @Mock
    ChuckNorrisServiceApi chuckNorrisServiceApiMock;

    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(JokesActivity.class,
            false,
            false);

    @Test
    public void should_show_jokes_list()
    {
//        when(chuckNorrisServiceApiMock.jokes()).thenReturn(Observable.just(Collections.singletonList(new Joke(1,"test joke"))));
        launchActivity();

        onView(withId(R.id.jokes_list)).check(matches(isDisplayed()));
    }

    @Test
    public void should_update_jokes_list() throws Exception
    {
        launchActivity();
        mActivityRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getJokesFragment().refreshJokes(TEST_JOKES);
            }
        });

        onView(withText("test joke")).check(matches(isDisplayed()));
    }

    @After
    public void tearDown()
    {
        mActivityRule.getActivity().finish();
    }

    private void launchActivity()
    {
        mActivityRule.launchActivity(new Intent());
    }

    private JokesFragment getJokesFragment()
    {
        return (JokesFragment) mActivityRule.getActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
    }
}
