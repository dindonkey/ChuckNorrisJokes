package it.dindonkey.chucknorrisjokes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.model.Joke;
import it.dindonkey.chucknorrisjokes.model.JokesResponse;
import it.dindonkey.chucknorrisjokes.repository.IcndbApiService;
import it.dindonkey.chucknorrisjokes.repository.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.repository.SchedulerManager;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InMemoryJokesRepositoryTest
{
    public static final Joke TEST_JOKE = new Joke(1, "test joke");
    private InMemoryJokesRepository mInMemoryJokesRepository;
    private TestSubscriber mTestSubscriber;

    @Mock
    IcndbApiService mIcndbApiServiceMock;
    @Mock
    HttpURLConnection mHttpUrlConnectionMock;
    private TestScheduler mTestScheduler;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        when(mIcndbApiServiceMock.jokes()).thenReturn(jokesReponseWith(TEST_JOKE));

        mTestSubscriber = new TestSubscriber();
        mTestScheduler = new TestScheduler();

        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.immediate(),
                Schedulers.immediate());
        mInMemoryJokesRepository = new InMemoryJokesRepository(mIcndbApiServiceMock,
                schedulerManager);
    }

    @Test
    public void should_get_jokes_from_service() throws Exception
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mIcndbApiServiceMock).jokes();
    }

    @Test
    public void should_cache_jokes() throws Exception
    {
        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mIcndbApiServiceMock).jokes();
    }

    @Test
    public void should_not_make_new_request_while_previous_request_is_in_progress() throws Exception
    {
        when(mIcndbApiServiceMock.jokes())
                .thenReturn(observableWithDelay(mHttpUrlConnectionMock,5, mTestScheduler));

        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        mTestScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mHttpUrlConnectionMock).connect();
    }

    private Observable<JokesResponse> jokesReponseWith(Joke joke)
    {
        JokesResponse jokesResponse = new JokesResponse();
        jokesResponse.value = Collections.singletonList(joke);

        return Observable.just(jokesResponse);
    }

    private Observable observableWithDelay(final HttpURLConnection connection,
                                           int seconds,
                                           Scheduler scheduler)
    {
        return Observable.create(new Observable.OnSubscribe<Void>()
        {
            @Override
            public void call(Subscriber<? super Void> subscriber)
            {
                try
                {
                    connection.connect();
                } catch (IOException e)
                {
                }
            }
        }).delay(seconds, TimeUnit.SECONDS, scheduler);
    }

}
