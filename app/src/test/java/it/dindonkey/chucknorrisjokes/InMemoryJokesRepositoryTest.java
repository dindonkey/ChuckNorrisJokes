package it.dindonkey.chucknorrisjokes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.model.Joke;
import it.dindonkey.chucknorrisjokes.model.JokesResponse;
import it.dindonkey.chucknorrisjokes.repository.IcndbApiService;
import it.dindonkey.chucknorrisjokes.repository.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.repository.SchedulerManager;
import rx.Observable;
import rx.Scheduler;
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

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        when(mIcndbApiServiceMock.jokes()).thenReturn(jokesReponseWith(TEST_JOKE));

        mTestSubscriber = new TestSubscriber();
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
    public void should_not_get_jokes_while_previous_request_is_in_progress() throws Exception
    {
        TestScheduler testScheduler = new TestScheduler();
        when(mIcndbApiServiceMock.jokes())
                .thenReturn(jokesReponseWith(TEST_JOKE).delay(5, TimeUnit.SECONDS, testScheduler));

        mInMemoryJokesRepository.getJokes(mTestSubscriber);
        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS);
        mInMemoryJokesRepository.getJokes(mTestSubscriber);

        verify(mIcndbApiServiceMock).jokes();
    }

    private Observable<JokesResponse> jokesReponseWith(Joke joke)
    {
        JokesResponse jokesResponse = new JokesResponse();
        jokesResponse.value = Collections.singletonList(joke);

        return Observable.just(jokesResponse);
    }

}
