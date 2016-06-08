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

    @Override
    public void onCreate()
    {
        super.onCreate();
        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.io(),
                AndroidSchedulers.mainThread());
        ChuckNorrisServiceApi chuckNorrisServiceApi = ChuckNorrisServiceApiRetrofit.createService(
                HttpUrl.parse("http://tbd"));
        JokesRepository jokesRepository = new InMemoryJokesRepository(chuckNorrisServiceApi,
                schedulerManager);
        mJokesUserActionsListener = new JokesPresenter(jokesRepository);
    }

    public JokesContract.UserActionsListener getJokesUserActionsListener()
    {
        return mJokesUserActionsListener;
    }

    public void setJokesUserActionsListener(JokesContract.UserActionsListener jokesUserActionsListener)
    {
        mJokesUserActionsListener = jokesUserActionsListener;
    }
}
