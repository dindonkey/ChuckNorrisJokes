package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.androidtest.AppActivityTestCase;
import it.dindonkey.chucknorrisjokes.androidtest.EspressoExecutor;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import it.dindonkey.chucknorrisjokes.navigator.ContextNavigator;
import it.dindonkey.chucknorrisjokes.navigator.Navigator;
import okhttp3.mockwebserver.MockWebServer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class JokesAppActivityAcceptanceTest extends AppActivityTestCase
{
    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Before
    public void setUp() throws Exception
    {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        ChuckNorrisServiceApi chuckNorrisServiceApi = ChuckNorrisServiceApiRetrofit.createService(
                mMockWebServer.url("/"));
        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.from(EspressoExecutor.getCachedThreadPool()),
                AndroidSchedulers.mainThread());
        GiphyServiceApi giphyServiceApi = GiphyServiceApiRetrofit.createService(mMockWebServer.url(
                "/"));
        JokesRepository jokesRepository = new InMemoryJokesRepository(chuckNorrisServiceApi,
                giphyServiceApi,
                schedulerManager);
        Navigator navigator = new ContextNavigator(getApplication().getApplicationContext());

        JokesPresenter jokesPresenter = new JokesPresenter(jokesRepository, navigator);
        getApplication().setJokesUserActionsListener(jokesPresenter);
    }

    @Test
    public void should_load_data_from_network_and_show_jokes() throws Exception
    {
        enqueueJsonHttpResponse("jokes.json");
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.joke_text)).check(matches(withText(containsString("ribbed condoms"))));
    }

    @Test
    public void reload_jokes_if_an_error_occours() throws Exception
    {
        enqueueErrorHttpResponse();
        enqueueJsonHttpResponse("jokes.json");

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.error_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.retry_button)).perform(click());
        onView(withId(R.id.joke_text)).check(matches(withText(containsString("ribbed condoms"))));
    }

    @Test
    public void should_open_joke_detail_when_a_joke_is_selected() throws Exception
    {
        enqueueJsonHttpResponse("jokes.json");
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.jokes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.joke_detail)).check(matches(isDisplayed()));
    }

    @Test
    public void should_close_joke_detail_after_back_is_pressed() throws Exception
    {
        enqueueJsonHttpResponse("jokes.json");
        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.jokes_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.joke_detail)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.joke_detail)).check(doesNotExist());
    }
}
