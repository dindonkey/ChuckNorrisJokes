package it.dindonkey.chucknorrisjokes.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisApiService;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InMemoryJokesRepositoryTest
{
    private InMemoryJokesRepository mInMemoryJokesRepository;
    private TestSubscriber<List<Joke>> mTestSubscriber;
    private TestScheduler mTestScheduler;

    @Mock
    ChuckNorrisApiService mChuckNorrisApiServiceMock;
    @Mock
    DummyHttpClient mDummyHttpClientMock;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(mChuckNorrisApiServiceMock.jokes()).thenReturn(observableWithHttpMock());

        mTestSubscriber = new TestSubscriber<>();
        mTestScheduler = new TestScheduler();

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.immediate(),
                Schedulers.immediate());
        mInMemoryJokesRepository = new InMemoryJokesRepository(mChuckNorrisApiServiceMock,
                schedulerManager);
    }

    @Test
    public void should_do_network_request_to_get_jokes()
    {
        mInMemoryJokesRepository.jokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_cache_result_if_previous_request_was_completed()
    {
        mInMemoryJokesRepository.jokes(mTestSubscriber);
        mInMemoryJokesRepository.jokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_redo_request_if_cache_is_cleared()
    {
        mInMemoryJokesRepository.jokes(mTestSubscriber);
        mInMemoryJokesRepository.clearCache();
        mInMemoryJokesRepository.jokes(mTestSubscriber);

        verify(mDummyHttpClientMock, times(2)).doRequest();
    }

    @Test
    public void should_not_do_new_request_if_a_request_is_in_progress()
    {
        when(mChuckNorrisApiServiceMock.jokes())
                .thenReturn(observableWithHttpMockAndDelay(5, mTestScheduler));

        mInMemoryJokesRepository.jokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.jokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_unsubsribe_observer_while_request_is_running_if_requested()
    {
        when(mChuckNorrisApiServiceMock.jokes())
                .thenReturn(observableWithHttpMockAndDelay(5, mTestScheduler));

        mInMemoryJokesRepository.jokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.clearSubscription();

        mTestSubscriber.assertUnsubscribed();
    }

    @SuppressWarnings("SameParameterValue")
    private Observable<List<Joke>> observableWithHttpMockAndDelay(int seconds, Scheduler scheduler)
    {
        return Observable.create(new Observable.OnSubscribe<List<Joke>>()
        {
            @Override
            public void call(Subscriber<? super List<Joke>> subscriber)
            {
                mDummyHttpClientMock.doRequest();
            }
        }).delay(seconds, TimeUnit.SECONDS, scheduler);
    }

    private Observable<List<Joke>> observableWithHttpMock()
    {
        return Observable.create(new Observable.OnSubscribe<List<Joke>>()
        {
            @Override
            public void call(Subscriber<? super List<Joke>> subscriber)
            {
                mDummyHttpClientMock.doRequest();
            }
        });
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
