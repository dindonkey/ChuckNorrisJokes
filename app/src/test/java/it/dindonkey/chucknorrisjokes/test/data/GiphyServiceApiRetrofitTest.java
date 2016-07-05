package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;

import it.dindonkey.chucknorrisjokes.data.GiphyGif;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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
}
