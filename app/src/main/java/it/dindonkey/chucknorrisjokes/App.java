package it.dindonkey.chucknorrisjokes;

import android.app.Application;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import okhttp3.HttpUrl;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class App extends Application
{
    private JokesContract.UserActionsListener mJokesUserActionsListener;
    private SchedulerManager mSchedulerManager;
    private ChuckNorrisServiceApi mChuckNorrisServiceApi;
    private JokesRepository mJokesRepository;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mSchedulerManager = new SchedulerManager(Schedulers.io(), AndroidSchedulers.mainThread());
        mChuckNorrisServiceApi = ChuckNorrisServiceApiRetrofit.createService(HttpUrl.parse("tbd"));
        mJokesRepository = new InMemoryJokesRepository(mChuckNorrisServiceApi, mSchedulerManager);
        mJokesUserActionsListener = new JokesPresenter(mJokesRepository);
    }

    public JokesContract.UserActionsListener getJokesUserActionsListener()
    {
        return mJokesUserActionsListener;
    }

    public void setJokesUserActionsListener(JokesContract.UserActionsListener jokesUserActionsListener)
    {
        this.mJokesUserActionsListener = jokesUserActionsListener;
    }
}
