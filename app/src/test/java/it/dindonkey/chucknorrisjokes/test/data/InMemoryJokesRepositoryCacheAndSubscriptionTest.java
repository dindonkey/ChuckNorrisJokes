package it.dindonkey.chucknorrisjokes.test.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryJokesRepositoryCacheAndSubscriptionTest
{
    private InMemoryJokesRepository mInMemoryJokesRepository;
    private TestSubscriber<List<Joke>> mTestSubscriber;
    private TestScheduler mTestScheduler;

    @Mock
    ChuckNorrisServiceApi mChuckNorrisServiceApiMock;
    @Mock
    GiphyServiceApi mGiphyServiceApi;
    @Mock
    DummyHttpClient mDummyHttpClientMock;

    @Before
    public void setUp()
    {
        when(mChuckNorrisServiceApiMock.getJokes()).thenReturn(observableWithHttpMock());

        mTestSubscriber = new TestSubscriber<>();
        mTestScheduler = new TestScheduler();

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.immediate(),
                Schedulers.immediate());
        mInMemoryJokesRepository = new InMemoryJokesRepository(mChuckNorrisServiceApiMock,
                mGiphyServiceApi,
                schedulerManager);
    }

    @Test
    public void should_do_network_request_to_get_jokes()
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_cache_result_if_previous_request_was_completed()
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_redo_request_if_cache_is_cleared()
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mInMemoryJokesRepository.clearCache();
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock, times(2)).doRequest();
    }

    @Test
    public void should_not_do_new_request_if_a_request_is_in_progress()
    {
        when(mChuckNorrisServiceApiMock.getJokes())
                .thenReturn(observableWithHttpMockAndDelay(5, mTestScheduler));

        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_unsubsribe_observer_while_request_is_running_if_requested()
    {
        when(mChuckNorrisServiceApiMock.getJokes())
                .thenReturn(observableWithHttpMockAndDelay(5, mTestScheduler));

        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.clearSubscription();

        mTestSubscriber.assertUnsubscribed();
    }

    @SuppressWarnings("SameParameterValue")
    private Observable<List<Joke>> observableWithHttpMockAndDelay(int seconds, Scheduler scheduler)
    {
        return Observable.create((OnSubscribe<List<Joke>>) s -> mDummyHttpClientMock
                .doRequest()).delay(seconds, TimeUnit.SECONDS, scheduler);
    }

    private Observable<List<Joke>> observableWithHttpMock()
    {
        return Observable.create(s -> mDummyHttpClientMock.doRequest());
    }

    class DummyHttpClient
    {
        @SuppressWarnings("EmptyMethod")
        public void doRequest()
        {
            //It does nothing. It's just to track network request
        }
    }

}
