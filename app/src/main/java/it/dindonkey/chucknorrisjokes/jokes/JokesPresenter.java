package it.dindonkey.chucknorrisjokes.jokes;

import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import rx.Subscriber;

public class JokesPresenter implements JokesContract.UserActionsListener
{
    private JokesContract.View mView;
    private final JokesRepository mJokesRepository;

    public JokesPresenter(JokesRepository jokesRepository)
    {
        mJokesRepository = jokesRepository;
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
        } else
        {
            mView.showLoading();
        }
        mJokesRepository.getJokes(new Subscriber<List<Joke>>()
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
        });

    }
}
