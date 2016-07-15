package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.androidtest.AppActivityTestCase;
import it.dindonkey.chucknorrisjokes.androidtest.EspressoExecutor;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyGif;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import it.dindonkey.chucknorrisjokes.navigator.ContextNavigator;
import it.dindonkey.chucknorrisjokes.navigator.Navigator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class JokesAppActivityMockTest extends AppActivityTestCase
{
    public static final GiphyGif TEST_GIPHYGIF = new GiphyGif("foobar");
    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Mock
    ChuckNorrisServiceApi mChuckNorrisServiceApiMock;
    @Mock
    GiphyServiceApi mGiphyServiceApi;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(mChuckNorrisServiceApiMock.getJokes()).thenReturn(Observable.just(TEST_JOKES));
        when(mGiphyServiceApi.getRandomGif(anyString(), anyString())).thenReturn(Observable.just(
                TEST_GIPHYGIF));

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.from(EspressoExecutor.getCachedThreadPool()),
                AndroidSchedulers.mainThread());
        JokesRepository jokesRepository = new InMemoryJokesRepository(mChuckNorrisServiceApiMock,
                mGiphyServiceApi,
                schedulerManager);
        Navigator navigator = new ContextNavigator(getApplication().getApplicationContext());

        JokesPresenter jokesPresenter = new JokesPresenter(jokesRepository, navigator);
        getApplication().setJokesUserActionsListener(jokesPresenter);
    }

    @Test
    public void should_show_test_joke()
    {
        mActivityRule.launchActivity(new Intent());

        onView(withText("test joke")).check(matches(isDisplayed()));
    }


    @Test
    public void should_open_joke_detail_when_a_joke_is_selected() throws Exception
    {
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.jokes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.joke_detail)).check(matches(isDisplayed()));
    }
}
