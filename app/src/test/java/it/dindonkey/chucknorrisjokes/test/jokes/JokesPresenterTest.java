package it.dindonkey.chucknorrisjokes.test.jokes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JokesPresenterTest
{
    private JokesPresenter jokesPresenter;

    @Mock
    JokesContract.View viewMock;

    @Before
    public void setUp() throws Exception
    {
        jokesPresenter = new JokesPresenter();
        jokesPresenter.bindView(viewMock);
    }

    @Test
    public void should_load_jokes_from_report_and_refresh_view() throws Exception
    {
        jokesPresenter.loadJokes();

        verify(viewMock).showJokes(anyList());
    }
}
