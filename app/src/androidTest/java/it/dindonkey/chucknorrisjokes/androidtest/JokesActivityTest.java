package it.dindonkey.chucknorrisjokes.androidtest;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class JokesActivityTest
{
    private static final List<Joke> TEST_JOKES = Collections.singletonList(new Joke(1,
            "test joke"));

    @Mock
    JokesContract.UserActionsListener mJokesUserActionsListenerMock;

    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        getApplication().setJokesUserActionsListener(mJokesUserActionsListenerMock);
    }

    @Test
    public void should_show_jokes_list()
    {
        launchActivity();

        onView(withId(R.id.jokes_list)).check(matches(isDisplayed()));
    }

    @Test
    public void should_refresh_jokes()
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

    @Test
    public void should_load_jokes_on_resume()
    {
        launchActivity();

        verify(mJokesUserActionsListenerMock).loadJokes();
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

    private App getApplication()
    {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        return (App) instrumentation.getTargetContext().getApplicationContext();
    }
}
