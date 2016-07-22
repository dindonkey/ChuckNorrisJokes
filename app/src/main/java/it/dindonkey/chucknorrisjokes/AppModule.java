package it.dindonkey.chucknorrisjokes;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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
import it.dindonkey.chucknorrisjokes.navigator.ContextNavigator;
import it.dindonkey.chucknorrisjokes.navigator.Navigator;
import okhttp3.HttpUrl;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule
{
    private final App application;

    public AppModule(App application)
    {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext()
    {
        return application.getApplicationContext();
    }


    @Provides
    @Singleton
    SchedulerManager provideSchedulerManager()
    {
        return new SchedulerManager(Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Provides
    @Singleton
    ChuckNorrisServiceApi providesChuckNorrisServiceApi(Context context)
    {
        return ChuckNorrisServiceApiRetrofit.createService(
                HttpUrl.parse(context.getString(R.string.chucknorrisapi_base_url)));
    }

    @Provides
    @Singleton
    GiphyServiceApi provideGiphyServiceApi(Context context)
    {
        return GiphyServiceApiRetrofit.createService(HttpUrl.parse(
                context.getString(R.string.giphyapi_base_url)));
    }

    @Provides
    @Singleton
    Navigator provideNavigator(Context context)
    {
        return new ContextNavigator(context);
    }

    @Provides
    @Singleton
    JokesRepository provideJokeRepository(ChuckNorrisServiceApi chuckNorrisServiceApi,
                                          GiphyServiceApi giphyServiceApi,
                                          SchedulerManager schedulerManager)
    {
        return new InMemoryJokesRepository(chuckNorrisServiceApi,
                giphyServiceApi,
                schedulerManager);
    }

    @Provides
    @Singleton
    JokesContract.UserActionsListener provideJokeContractUserActions(JokesRepository jokesRepository,
                                                                     Navigator navigator)
    {
        return new JokesPresenter(jokesRepository, navigator);
    }

    @Provides
    @Singleton
    RxBus provideRxBus()
    {
        return new RxBus();
    }

}
