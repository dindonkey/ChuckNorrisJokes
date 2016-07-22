package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import org.junit.After;
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
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import rx.functions.Action1;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class JokesActivityUnitTest extends AppActivityTestCase
{
    @Mock
    JokesContract.UserActionsListener mJokesPresenterMock;
    @Mock
    RxBus mRxBusMock;

    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        getApplication().setRxBus(new RxBus());
        getApplication().setJokesUserActionsListener(mJokesPresenterMock);
    }

    @Test
    public void should_bind_view()
    {
        mActivityRule.launchActivity(new Intent());

        verify(mJokesPresenterMock).bindView(getCurrentFragment(mActivityRule.getActivity()));
    }

    @Test
    public void should_unbind_view_on_pause()
    {
        mActivityRule.launchActivity(new Intent());

        rotateScreen(mActivityRule.getActivity());

        verify(mJokesPresenterMock).unBindView();
    }

    @Test
    public void should_cancel_subscription_on_pause() throws Exception
    {
        mActivityRule.launchActivity(new Intent());

        rotateScreen(mActivityRule.getActivity());

        verify(mJokesPresenterMock).clearSubscription();
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
                // Espresso has problems with progress bar animation, this is ugly but necessary
                fixEspressoProgressBarLoop();
            }
        });

        onView(withId(R.id.loading_fragment)).check(matches(isDisplayed()));
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

        verify(mJokesPresenterMock).loadJokes(false);
    }

    @Test
    public void should_show_error() throws Exception
    {
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                getCurrentFragment(mActivityRule.getActivity()).showError();
            }
        });

        onView(withId(R.id.error_fragment)).check(matches(isDisplayed()));
    }

    @Test
    public void should_register_for_reload_event() throws Exception
    {
        getApplication().setRxBus(mRxBusMock);
        mActivityRule.launchActivity(new Intent());

        verify(mRxBusMock).register(eq(ReloadJokesEvent.class), any(Action1.class));
    }

    @Test
    public void should_reload_jokes_if_reload_event_is_received() throws Exception
    {
        mActivityRule.launchActivity(new Intent());

        getApplication().getRxBus().post(new ReloadJokesEvent());

        verify(mJokesPresenterMock).loadJokes(false);
        verify(mJokesPresenterMock).loadJokes(true);
    }

    @After
    public void tearDown()
    {
        mActivityRule.getActivity().finish();
    }

    private void fixEspressoProgressBarLoop()
    {
        ProgressBar progressBar = (ProgressBar) mActivityRule.getActivity()
                .findViewById(R.id.progress_bar);
        Drawable notAnimatedDrawable = ContextCompat.getDrawable(mActivityRule.getActivity(),
                R.drawable.chuck_8bit);
        assert progressBar != null;
        progressBar.setIndeterminateDrawable(notAnimatedDrawable);

    }


}
