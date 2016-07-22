package it.dindonkey.chucknorrisjokes.test.jokes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import it.dindonkey.chucknorrisjokes.navigator.Navigator;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;
import rx.Observer;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JokesPresenterTest extends SharedTestCase
{
    private JokesPresenter mJokesPresenter;

    @Mock
    JokesContract.View mViewMock;
    @Mock
    JokesRepository mJokesRepositoryMock;
    @Mock
    Navigator mNavigatorMock;

    @Captor
    ArgumentCaptor<Observer<List<Joke>>> mArgumentCaptor;

    @Before
    public void setUp()
    {
        mJokesPresenter = new JokesPresenter(mJokesRepositoryMock, mNavigatorMock);
        mJokesPresenter.bindView(mViewMock);
    }

    @Test
    public void shold_show_loading_while_fetching_data() throws Exception
    {
        mJokesPresenter.loadJokes(false);

        verify(mViewMock).showLoading();
    }

    @Test
    public void should_hide_loading_when_data_is_loaded() throws Exception
    {
        mJokesPresenter.loadJokes(false);

        verify(mJokesRepositoryMock).getJokes(mArgumentCaptor.capture());
        mArgumentCaptor.getValue().onNext(TEST_JOKES);

        verify(mViewMock).hideLoading();
    }

    @Test
    public void should_load_jokes_from_repository_and_refresh_view()
    {
        mJokesPresenter.loadJokes(false);

        verify(mJokesRepositoryMock).getJokes(mArgumentCaptor.capture());
        mArgumentCaptor.getValue().onNext(TEST_JOKES);

        verify(mViewMock).showJokes(TEST_JOKES);
    }

    @Test
    public void should_show_error_if_an_error_occours() throws Exception
    {
        mJokesPresenter.loadJokes(false);

        verify(mJokesRepositoryMock).getJokes(mArgumentCaptor.capture());
        mArgumentCaptor.getValue().onError(new Exception());

        verify(mViewMock).showError();
    }

    @Test
    public void should_clear_cache_if_requested() throws Exception
    {
        mJokesPresenter.loadJokes(true);

        verify(mJokesRepositoryMock).clearCache();
    }

    @Test
    public void should_navigate_to_joke_detail() throws Exception
    {
        mJokesPresenter.openJokeDetail(TEST_JOKES.get(0));

        verify(mNavigatorMock).navigateToJokeDetail(TEST_JOKES.get(0));
    }

    @Test
    public void should_cancel_subscription() throws Exception
    {
        mJokesPresenter.clearSubscription();

        verify(mJokesRepositoryMock).clearSubscription();
    }
}
