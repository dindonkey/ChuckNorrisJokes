package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public class ChuckNorrisServiceApiRetrofitTest extends SharedTestCase
{
    private ChuckNorrisServiceApi mChuckNorrisServiceApi;
    private TestSubscriber<List<Joke>> mTestSubscriber;

    @Before
    public void setUp() throws Exception
    {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        mChuckNorrisServiceApi = ChuckNorrisServiceApiRetrofit.createService(mMockWebServer.url("/"));
        mTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void should_call_jokes_api_path() throws Exception
    {
        mChuckNorrisServiceApi.getJokes().subscribe(mTestSubscriber);

        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals("/jokes/random/10", request.getPath());
    }

    @Test
    public void should_get_jokes_from_json_request() throws IOException
    {
        enqueueJsonHttpResponse("jokes.json");
        Joke expected = new Joke(1, "Chuck Norris uses ribbed condoms inside out, so he gets the pleasure.");

        List<Joke> jokes = observableResults(mChuckNorrisServiceApi.getJokes());
        Joke actual = jokes.get(0);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.joke, actual.joke);
    }

    private List<Joke> observableResults(Observable<List<Joke>> observable)
    {
        observable.subscribe(mTestSubscriber);
        return mTestSubscriber.getOnNextEvents().get(0);
    }


}
