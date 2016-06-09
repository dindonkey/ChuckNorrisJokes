package it.dindonkey.chucknorrisjokes.androidtest.mock;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import it.dindonkey.chucknorrisjokes.androidtest.ActivityTestCase;
import it.dindonkey.chucknorrisjokes.androidtest.EspressoExecutor;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class JokesActivityMockTest extends ActivityTestCase
{
    private static final List<Joke> TEST_JOKES = Collections.singletonList(new Joke(1,
            "test joke"));

    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    @Mock
    ChuckNorrisServiceApi mChuckNorrisServiceApiMock;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.from(EspressoExecutor.getCachedThreadPool()),
                AndroidSchedulers.mainThread());
        JokesRepository jokesRepository = new InMemoryJokesRepository(mChuckNorrisServiceApiMock,
                schedulerManager);
        JokesPresenter jokesPresenter = new JokesPresenter(jokesRepository);
        getApplication().setJokesUserActionsListener(jokesPresenter);
    }

    @Test
    public void should_show_test_joke() throws Exception
    {
        when(mChuckNorrisServiceApiMock.jokes()).thenReturn(Observable.just(TEST_JOKES));

        mActivityRule.launchActivity(new Intent());

        onView(withText("test joke")).check(matches(isDisplayed()));
    }

}
