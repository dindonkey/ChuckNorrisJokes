package it.dindonkey.chucknorrisjokes.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.data.IcndbApiService;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import rx.Observable;
import rx.Scheduler;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InMemoryJokesRepositoryTest
{
    private InMemoryJokesRepository mInMemoryJokesRepository;
    private TestSubscriber mTestSubscriber;
    private TestScheduler mTestScheduler;

    @Mock
    IcndbApiService mIcndbApiServiceMock;
    @Mock
    DummyHttpClient mDummyHttpClientMock;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        when(mIcndbApiServiceMock.jokes()).thenReturn(observableWithHttpMock());

        mTestSubscriber = new TestSubscriber();
        mTestScheduler = new TestScheduler();

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.immediate(),
                Schedulers.immediate());
        mInMemoryJokesRepository = new InMemoryJokesRepository(mIcndbApiServiceMock,
                schedulerManager);
    }

    @Test
    public void should_do_network_request_to_get_jokes() throws Exception
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_cache_result_if_previous_request_was_completed() throws Exception
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_not_do_new_request_if_a_request_is_in_progress() throws Exception
    {
        when(mIcndbApiServiceMock.jokes())
                .thenReturn(observableWithHttpMockAndDelay(5, mTestScheduler));

        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock).doRequest();
    }

    @Test
    public void should_redo_request_if_cache_is_cleared() throws Exception
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mInMemoryJokesRepository.clearCache();
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mDummyHttpClientMock, times(2)).doRequest();
    }

    private Observable observableWithHttpMockAndDelay(int seconds, Scheduler scheduler)
    {
        return Observable.create(new Observable.OnSubscribe()
        {
            @Override
            public void call(Object o)
            {
                mDummyHttpClientMock.doRequest();
            }
        }).delay(seconds, TimeUnit.SECONDS, scheduler);
    }

    private Observable observableWithHttpMock()
    {
        return Observable.create(new Observable.OnSubscribe()
        {
            @Override
            public void call(Object o)
            {
                mDummyHttpClientMock.doRequest();
            }
        });
    }

    class DummyHttpClient
    {
        public void doRequest()
        {
            //It does nothing. It's just to track network request
        }
    }

}
