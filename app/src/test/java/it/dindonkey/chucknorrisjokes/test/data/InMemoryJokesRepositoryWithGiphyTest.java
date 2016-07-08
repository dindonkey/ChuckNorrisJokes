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

import static org.junit.Assert.assertNotNull;
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

        assertNotNull(jokes.get(0).gifUrl);

    }

    private Observable<GiphyGif> testObservableGiphy()
    {
        return Observable.just(new GiphyGif("http://gifurl"));
    }

    private Observable<List<Joke>> testObservableJokes()
    {
        return Observable.just(Arrays.asList(new Joke(1, "joke a"), new Joke(2, "b")));
    }
}
