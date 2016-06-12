package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiMock;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
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
        mockJsonHttpResponse("jokes.json");
        Joke expected = new Joke(1, "Chuck Norris uses ribbed condoms inside out, so he gets the pleasure.");

        List<Joke> jokes = observableResults(mChuckNorrisServiceApi.getJokes());
        Joke actual = jokes.get(0);

        assertEquals(expected.id, actual.id);
        assertEquals(expected.joke, actual.joke);
    }

    @Test
    public void testxxx() throws Exception
    {
        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setFailurePercent(100);

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(new Retrofit
                .Builder()
                .baseUrl("http://example.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build())
                .networkBehavior(behavior)
                .build();

        BehaviorDelegate<ChuckNorrisServiceApi> delegate = mockRetrofit.create(ChuckNorrisServiceApi.class);

        ChuckNorrisServiceApiMock chuckNorrisServiceApiMock = new ChuckNorrisServiceApiMock(delegate);
        List<Joke> jokes = observableResults(chuckNorrisServiceApiMock.getJokes());

        assertEquals("mock joke description", jokes.get(0).joke);


    }

    private List<Joke> observableResults(Observable<List<Joke>> observable)
    {
        observable.subscribe(mTestSubscriber);
        return mTestSubscriber.getOnNextEvents().get(0);
    }


}
