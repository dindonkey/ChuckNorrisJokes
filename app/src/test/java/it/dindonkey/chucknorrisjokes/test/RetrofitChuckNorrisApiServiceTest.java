package it.dindonkey.chucknorrisjokes.test;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisApiService;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisApiRetrofitService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public class RetrofitChuckNorrisApiServiceTest
{

    private MockWebServer mockWebServer;
    private ChuckNorrisApiService chuckNorrisApiService;
    private TestSubscriber<List<Joke>> mTestSubscriber;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        chuckNorrisApiService = ChuckNorrisApiRetrofitService.createService(mockWebServer.url("/"));
        mTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void should_get_jokes_from_json_request() throws IOException
    {
        mockJsonHttpResponse("jokes.json");
        Joke expected = new Joke(1,"Chuck Norris uses ribbed condoms inside out, so he gets the pleasure.");

        List<Joke> jokes = observableResults(chuckNorrisApiService.jokes());
        Joke actual = jokes.get(0);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.joke, actual.joke);
    }

    @SuppressWarnings("SameParameterValue")
    private void mockJsonHttpResponse(String jsonPath) throws IOException
    {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getStringFromFile(jsonPath)));
    }

    private List<Joke> observableResults(Observable<List<Joke>> observable)
    {
        observable.subscribe(mTestSubscriber);
        return mTestSubscriber.getOnNextEvents().get(0);
    }

    private String getStringFromFile(String path) throws IOException
    {
        return IOUtils.toString(getClass().getClassLoader().getResource(path), "UTF-8");
    }
}
