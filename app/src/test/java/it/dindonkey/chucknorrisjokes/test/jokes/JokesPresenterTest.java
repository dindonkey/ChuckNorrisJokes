package it.dindonkey.chucknorrisjokes.test.jokes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import rx.Subscriber;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JokesPresenterTest
{
    private static final List<Joke> TEST_JOKES = Collections.singletonList(new Joke(1,
            "test joke"));
    private JokesPresenter jokesPresenter;

    @Mock
    JokesContract.View viewMock;
    @Mock
    JokesRepository jokesRepository;

    @Captor
    ArgumentCaptor<Subscriber<List<Joke>>> mArgumentCaptor;

    @Before
    public void setUp() throws Exception
    {
        jokesPresenter = new JokesPresenter(jokesRepository);
        jokesPresenter.bindView(viewMock);
    }

    @Test
    public void should_load_jokes_from_repository_and_refresh_view() throws Exception
    {
        jokesPresenter.loadJokes();

        verify(jokesRepository).getJokes(mArgumentCaptor.capture());
        mArgumentCaptor.getValue().onNext(TEST_JOKES);

        verify(viewMock).showJokes(TEST_JOKES);
    }
}
