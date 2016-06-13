package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.androidtest.ActivityTestCase;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class JokesActivityUnitTest extends ActivityTestCase
{
    @Mock
    JokesContract.UserActionsListener mJokesPresenterMock;

    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        getApplication().setJokesUserActionsListener(mJokesPresenterMock);
    }

    @Test
    public void should_bind_view()
    {
        mActivityRule.launchActivity(new Intent());

        verify(mJokesPresenterMock).bindView(getCurrentFragment(mActivityRule.getActivity()));
    }

    @Test
    public void should_unbind_view()
    {
        mActivityRule.launchActivity(new Intent());

        rotateScreen(mActivityRule.getActivity());

        verify(mJokesPresenterMock).unBindView();
    }

    @Test
    public void should_show_jokes_fragment()
    {
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.jokes_list)).check(matches(isDisplayed()));
    }

    @Test
    public void should_show_loading_fragment() throws Exception
    {
        mActivityRule.launchActivity(new Intent());

        mActivityRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getCurrentFragment(mActivityRule.getActivity()).showLoading();
            }
        });

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
    }

    @Test
    public void should_hide_loading_fragment() throws Exception
    {

        mActivityRule.launchActivity(new Intent());

        mActivityRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getCurrentFragment(mActivityRule.getActivity()).showLoading();
                getCurrentFragment(mActivityRule.getActivity()).hideLoading();
            }
        });

        onView(withId(R.id.progress_bar)).check(doesNotExist());
    }

    @Test
    public void should_show_jokes()
    {
        mActivityRule.launchActivity(new Intent());

        mActivityRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getCurrentFragment(mActivityRule.getActivity()).showJokes(TEST_JOKES);
            }
        });

        onView(withText("test joke")).check(matches(isDisplayed()));
    }

    @Test
    public void should_load_jokes_when_activity_starts()
    {
        mActivityRule.launchActivity(new Intent());

        verify(mJokesPresenterMock).loadJokes();
    }

    @After
    public void tearDown()
    {
        mActivityRule.getActivity().finish();
    }


}
