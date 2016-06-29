package it.dindonkey.chucknorrisjokes.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

public class InMemoryJokesRepository implements JokesRepository
{
    private List<Joke> mCachedJokes;
    private final ChuckNorrisServiceApi mChuckNorrisServiceApi;
    private final SchedulerManager mSchedulerManager;
    private ConnectableObservable<List<Joke>> mCachedObservable;
    private Subscription mSubscription;

    public InMemoryJokesRepository(ChuckNorrisServiceApi chuckNorrisServiceApi,
                                   SchedulerManager schedulerManager)
    {
        mChuckNorrisServiceApi = chuckNorrisServiceApi;
        mSchedulerManager = schedulerManager;
    }

    @Override
    public void getJokes(Observer<List<Joke>> observer)
    {
        if (null == mCachedJokes)
        {
            if (null == mCachedObservable)
            {
                mCachedObservable = mChuckNorrisServiceApi.getJokes()
                        .doOnNext(saveJokes())
                        .doOnError(clearCacheOnError())
                        .subscribeOn(mSchedulerManager.computation())
                        .observeOn(mSchedulerManager.mainThread())
                        .replay();
                mCachedObservable.connect();
            }
            mSubscription = mCachedObservable.subscribe(observer);
        } else
        {
            observer.onNext(mCachedJokes);
            observer.onCompleted();
        }
    }

    @NonNull
    private Action1<Throwable> clearCacheOnError()
    {
        return new Action1<Throwable>()
        {
            @Override
            public void call(Throwable throwable)
            {
                clearCache();
            }
        };
    }

    @Override
    public void clearCache()
    {
        mCachedJokes = null;
        mCachedObservable = null;
    }

    @Override
    public void clearSubscription()
    {
        if(null != mSubscription && !mSubscription.isUnsubscribed())
        {
            mSubscription.unsubscribe();
        }
    }

    @NonNull
    private Action1<List<Joke>> saveJokes()
    {
        return new Action1<List<Joke>>()
        {
            @Override
            public void call(List<Joke> jokes)
            {
                mCachedJokes = jokes;
            }
        };
    }
}
