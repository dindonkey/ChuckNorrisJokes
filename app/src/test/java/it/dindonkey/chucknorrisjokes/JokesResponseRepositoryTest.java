package it.dindonkey.chucknorrisjokes;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import it.dindonkey.chucknorrisjokes.model.Joke;
import it.dindonkey.chucknorrisjokes.model.JokesResponse;
import it.dindonkey.chucknorrisjokes.repository.JokesRepository;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

public class JokesResponseRepositoryTest
{

    private MockWebServer mockWebServer;
    private JokesRepository jokesRepository;
    private TestSubscriber<JokesResponse> testSubscriber;

    @Before
    public void setUp() throws Exception
    {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        jokesRepository = retrofit.create(JokesRepository.class);
        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void should_get_jokes_from_repository() throws IOException
    {
        mockJsonHttpResponse("jokes.json");
        Joke expected = new Joke(1,"Chuck Norris uses ribbed condoms inside out, so he gets the pleasure.");

        Joke actual = itemsFromObservable(jokesRepository.jokes()).value.get(0);

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

    private JokesResponse itemsFromObservable(Observable<JokesResponse> observable)
    {
        observable.subscribe(testSubscriber);
        return testSubscriber.getOnNextEvents().get(0);
    }

    private String getStringFromFile(String path) throws IOException
    {
        return IOUtils.toString(getClass().getClassLoader().getResource(path), "UTF-8");
    }
}
