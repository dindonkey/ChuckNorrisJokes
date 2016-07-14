package it.dindonkey.chucknorrisjokes.jokes;

import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import rx.Observer;

public class JokesPresenter implements JokesContract.UserActionsListener
{
    private JokesContract.View mView;
    private final JokesRepository mJokesRepository;
    private final GetJokesSubscriber mGetJokesSubscriber;

    public JokesPresenter(JokesRepository jokesRepository)
    {
        mJokesRepository = jokesRepository;
        mGetJokesSubscriber = new GetJokesSubscriber();
    }

    @Override
    public void bindView(JokesContract.View view)
    {
        mView = view;
    }

    @Override
    public void unBindView()
    {
        mView = null;
    }

    @Override
    public void loadJokes(boolean refreshData)
    {
        if (refreshData)
        {
            mJokesRepository.clearCache();
        }
        mView.showLoading();
        mJokesRepository.getJokes(mGetJokesSubscriber);
    }

    @Override
    public void openJokeDetail(Joke joke)
    {
        mView.showJokeDetail(joke);
    }

    class GetJokesSubscriber implements Observer<List<Joke>>
    {
        @Override
        public void onCompleted()
        {

        }

        @Override
        public void onError(Throwable e)
        {
            mView.showError();
        }

        @Override
        public void onNext(List<Joke> jokes)
        {
            mView.showJokes(jokes);
            mView.hideLoading();
        }
    }
}
