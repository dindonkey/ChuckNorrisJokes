package it.dindonkey.chucknorrisjokes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import it.dindonkey.chucknorrisjokes.model.Joke;
import it.dindonkey.chucknorrisjokes.model.JokesResponse;
import it.dindonkey.chucknorrisjokes.repository.IcndbApiService;
import it.dindonkey.chucknorrisjokes.repository.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.repository.SchedulerManager;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InMemoryJokesRepositoryTest
{
    private InMemoryJokesRepository mInMemoryJokesRepository;
    private TestSubscriber mTestSubscriber;

    @Mock
    IcndbApiService mIcndbApiServiceMock;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        when(mIcndbApiServiceMock.jokes()).thenReturn(jokesReponseWith(new Joke(1,"test joke")));

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

    private Observable<JokesResponse> jokesReponseWith(Joke joke)
    {
        JokesResponse jokesResponse = new JokesResponse();
        jokesResponse.value = Collections.singletonList(joke);

        return Observable.just(jokesResponse);
    }
}
