package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyGif;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryJokesRepositoryWithGiphyTest
{
    private InMemoryJokesRepository mInMemoryJokesRepository;
    private TestSubscriber<List<Joke>> mTestSubscriber;

    @Mock
    ChuckNorrisServiceApi mChuckNorrisServiceApiMock;
    @Mock
    GiphyServiceApi mGiphyServiceApi;
    private static final String TEST_GIF_URL = "http://gifurl";

    @Before
    public void setUp()
    {
        when(mChuckNorrisServiceApiMock.getJokes()).thenReturn(testObservableJokes());
        when(mGiphyServiceApi.getRandomGif(anyString(),
                anyString())).thenReturn(testObservableGiphy());

        mTestSubscriber = new TestSubscriber<>();

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.immediate(),
                Schedulers.immediate());
        mInMemoryJokesRepository = new InMemoryJokesRepository(mChuckNorrisServiceApiMock,
                mGiphyServiceApi,
                schedulerManager);
    }

    @Test
    public void should_get_jokes_with_gif() throws Exception
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        List<Joke> jokes = mTestSubscriber.getOnNextEvents().get(0);

        assertEquals(TEST_GIF_URL, jokes.get(0).gifUrl);
    }

    @Test
    public void should_return_empty_gif_if_an_error_occours() throws Exception
    {
        when(mGiphyServiceApi.getRandomGif(anyString(),
                anyString())).thenReturn(testObservableGiphyError());

        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        List<Joke> jokes = mTestSubscriber.getOnNextEvents().get(0);

        assertEquals("", jokes.get(0).gifUrl);

    }

    private Observable<GiphyGif> testObservableGiphy()
    {
        return Observable.just(new GiphyGif(TEST_GIF_URL));
    }

    private Observable<List<Joke>> testObservableJokes()
    {
        return Observable.just(Arrays.asList(new Joke(1, "joke a"), new Joke(2, "b")));
    }

    private Observable<GiphyGif> testObservableGiphyError()
    {
        return Observable.create(s -> {
            s.onError(new Exception());
        });
    }
}
