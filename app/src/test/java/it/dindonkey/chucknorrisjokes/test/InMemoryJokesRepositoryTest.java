package it.dindonkey.chucknorrisjokes.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.data.IcndbApiService;
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
    IcndbApiService mIcndbApiServiceMock;
    @Mock
    DummyHttpClient mDummyHttpClientMock;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        when(mIcndbApiServiceMock.jokes()).thenReturn(observableWithHttpMock());

        mTestSubscriber = new TestSubscriber<>();
        mTestScheduler = new TestScheduler();

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.immediate(),
                Schedulers.immediate());
        mInMemoryJokesRepository = new InMemoryJokesRepository(mIcndbApiServiceMock,
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
    public void should_not_do_new_request_if_a_request_is_in_progress()
    {
        when(mIcndbApiServiceMock.jokes())
                .thenReturn(observableWithHttpMockAndDelay(5, mTestScheduler));

        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
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
