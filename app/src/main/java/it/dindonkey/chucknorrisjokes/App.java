package it.dindonkey.chucknorrisjokes;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApi;
import it.dindonkey.chucknorrisjokes.data.ChuckNorrisServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApi;
import it.dindonkey.chucknorrisjokes.data.GiphyServiceApiRetrofit;
import it.dindonkey.chucknorrisjokes.data.InMemoryJokesRepository;
import it.dindonkey.chucknorrisjokes.data.JokesRepository;
import it.dindonkey.chucknorrisjokes.data.SchedulerManager;
import it.dindonkey.chucknorrisjokes.events.RxBus;
import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;
import it.dindonkey.chucknorrisjokes.navigator.Navigator;
import okhttp3.HttpUrl;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class App extends Application
{
    private JokesContract.UserActionsListener mJokesUserActionsListener;
    private RxBus mRxBus;

    @Override
    public void onCreate()
    {
        super.onCreate();
        SchedulerManager schedulerManager = new SchedulerManager(Schedulers.io(),
                AndroidSchedulers.mainThread());
        ChuckNorrisServiceApi chuckNorrisServiceApi = ChuckNorrisServiceApiRetrofit.createService(
                HttpUrl.parse(getString(R.string.chucknorrisapi_base_url)));
        GiphyServiceApi giphyServiceApi = GiphyServiceApiRetrofit.createService(HttpUrl.parse(
                getString(R.string.giphyapi_base_url)));
        JokesRepository jokesRepository = new InMemoryJokesRepository(chuckNorrisServiceApi,
                giphyServiceApi,
                schedulerManager);
        Navigator navigator = new Navigator();

        mJokesUserActionsListener = new JokesPresenter(jokesRepository, navigator);
        mRxBus = new RxBus();

        if (BuildConfig.DEBUG)
        {
            LeakCanary.install(this);
        }
    }

    public JokesContract.UserActionsListener getJokesUserActionsListener()
    {
        return mJokesUserActionsListener;
    }

    public void setJokesUserActionsListener(JokesContract.UserActionsListener jokesUserActionsListener)
    {
        mJokesUserActionsListener = jokesUserActionsListener;
    }

    public RxBus getRxBus()
    {
        return mRxBus;
    }

    public void setRxBus(RxBus rxBus)
    {
        mRxBus = rxBus;
    }
}
