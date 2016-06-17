package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dindonkey.chucknorrisjokes.androidtest.AppActivityTestCase;
import it.dindonkey.chucknorrisjokes.androidtest.EspressoExecutor;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
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
public class JokesAppActivityMockTest extends AppActivityTestCase
{
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
    public void should_show_test_joke()
    {
        when(mChuckNorrisServiceApiMock.getJokes()).thenReturn(Observable.just(TEST_JOKES));

        mActivityRule.launchActivity(new Intent());

        onView(withText("test joke")).check(matches(isDisplayed()));
    }
}
