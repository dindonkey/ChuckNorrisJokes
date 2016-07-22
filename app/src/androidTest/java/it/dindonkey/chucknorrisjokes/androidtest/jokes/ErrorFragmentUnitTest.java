package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.androidtest.AppActivityTestCase;
import it.dindonkey.chucknorrisjokes.events.ReloadJokesEvent;
import it.dindonkey.chucknorrisjokes.events.RxBus;
import it.dindonkey.chucknorrisjokes.jokes.ErrorFragment;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ErrorFragmentUnitTest extends AppActivityTestCase
{
    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Mock
    RxBus mRxBusMock;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        Fragment errorFragment = ErrorFragment.newInstance();
        getApplication().setRxBus(mRxBusMock);
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, errorFragment)
                .commit();
    }

    @Test
    public void should_post_reload_event_to_event_bus_if_reload_button_is_clicked() throws Exception
    {
        onView(withId(R.id.retry_button)).perform(click());

        verify(mRxBusMock).post(any(ReloadJokesEvent.class));
    }
}
