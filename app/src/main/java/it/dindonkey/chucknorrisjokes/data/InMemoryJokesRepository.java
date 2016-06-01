package it.dindonkey.chucknorrisjokes.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

public class InMemoryJokesRepository implements JokesRepository
{
    private List<Joke> mCachedJokes;
    private final IcndbApiService mIcndbApiService;
    private final SchedulerManager mSchedulerManager;
    private ConnectableObservable<List<Joke>> mCachedObservable;
    private Subscription mSubscription;

    public InMemoryJokesRepository(IcndbApiService icndbApiService,
                                   SchedulerManager schedulerManager)
    {
        mIcndbApiService = icndbApiService;
        mSchedulerManager = schedulerManager;
    }

    @Override
    public void getJokes(Subscriber<List<Joke>> subscriber)
    {
        if (null == mCachedJokes)
        {
            if (null == mCachedObservable)
            {
                mCachedObservable = mIcndbApiService.jokes()
                        .doOnNext(saveJokes())
                        .subscribeOn(mSchedulerManager.computation())
                        .observeOn(mSchedulerManager.mainThread())
                        .replay();
                mCachedObservable.connect();
            }
            mSubscription = mCachedObservable.subscribe(subscriber);
        } else
        {
            subscriber.onNext(mCachedJokes);
            subscriber.onCompleted();
        }
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
