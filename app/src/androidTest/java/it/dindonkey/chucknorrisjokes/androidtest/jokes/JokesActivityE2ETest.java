package it.dindonkey.chucknorrisjokes.androidtest.jokes;

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.androidtest.ActivityTestCase;
import it.dindonkey.chucknorrisjokes.androidtest.EspressoExecutor;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import it.dindonkey.chucknorrisjokes.jokes.JokesActivity;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class JokesActivityE2ETest extends ActivityTestCase
{
    @Rule
    public final ActivityTestRule<JokesActivity> mActivityRule = new ActivityTestRule<>(
            JokesActivity.class,
            false,
            false);

    private MockWebServer mMockWebServer;

    @Before
    public void setUp() throws Exception
    {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        ChuckNorrisServiceApi chuckNorrisServiceApi = ChuckNorrisServiceApiRetrofit.createService(
                mMockWebServer.url("/"));
        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.from(EspressoExecutor.getCachedThreadPool()),
                AndroidSchedulers.mainThread());
        JokesRepository jokesRepository = new InMemoryJokesRepository(chuckNorrisServiceApi,
                schedulerManager);
        JokesPresenter jokesPresenter = new JokesPresenter(jokesRepository);
        getApplication().setJokesUserActionsListener(jokesPresenter);
    }

    @Test
    public void show_jokes() throws Exception
    {
        mockJsonHttpResponse("jokes.json");

        mActivityRule.launchActivity(new Intent());

        onView(withId(R.id.joke_text)).check(matches(withText(containsString("ribbed condoms"))));
    }

    private void mockJsonHttpResponse(String jsonPath) throws Exception
    {
        mMockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(getInstrumentation().getContext(), jsonPath)));
    }

    private String getStringFromFile(Context context, String filePath) throws Exception
    {
        final InputStream stream = context.getResources().getAssets().open(filePath);
        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }

    private String convertStreamToString(InputStream is) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

}
