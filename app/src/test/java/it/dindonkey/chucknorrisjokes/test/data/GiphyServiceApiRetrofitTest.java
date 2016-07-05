package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.GiphyGif;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public class GiphyServiceApiRetrofitTest extends SharedTestCase
{
    private TestSubscriber<GiphyGif> mTestSubscriber;
    private GiphyServiceApi mGiphyServiceApi;

    @Before
    public void setUp() throws Exception
    {
        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        mTestSubscriber = new TestSubscriber<>();
        mGiphyServiceApi = GiphyServiceApiRetrofit.createService(mMockWebServer.url("/"));
    }

    @Test
    public void should_call_giphy_api_path() throws Exception
    {
        mGiphyServiceApi.getRandomGif("API_KEY", "TAG").subscribe(mTestSubscriber);

        RecordedRequest request = mMockWebServer.takeRequest();
        assertEquals("/v1/gifs/random?api_key=API_KEY&tag=TAG", request.getPath());
    }

    @Test
    public void should_get_gif_url_from_json_response() throws IOException
    {
        enqueueJsonHttpResponse("giphy.json");
        GiphyGif expected = new GiphyGif("http://media0.giphy.com/media/EjTG4LTO1Zqik/200_d.gif");

        mGiphyServiceApi.getRandomGif("foo","bar").subscribe(mTestSubscriber);
        GiphyGif actual = mTestSubscriber.getOnNextEvents().get(0);

        assertEquals(expected.url, actual.url);
    }

    @Test
    public void testxxx() throws Exception
    {
        Observable<List<Joke>> jokes = Observable.
                just(Arrays.asList(new Joke(1, "a"), new Joke(2,"b")))
                .map(new Func1<List<Joke>, List<Joke>>()
                {
                    @Override
                    public List<Joke> call(List<Joke> jokes)
                    {
                        for(Joke joke : jokes)
                        {
                            joke.gifUrl = "ciao";
                        }
                        return jokes;
                    }
                });
        jokes
                .subscribe(new Subscriber<List<Joke>>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(List<Joke> jokes)
            {
                for(Joke joke : jokes)
                {
                    System.out.println(joke.gifUrl);
                }
            }
        });


    }
}
