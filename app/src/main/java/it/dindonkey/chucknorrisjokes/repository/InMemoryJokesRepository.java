package it.dindonkey.chucknorrisjokes.repository;

import android.support.annotation.NonNull;

import java.util.List;

import it.dindonkey.chucknorrisjokes.model.Joke;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;

public class InMemoryJokesRepository implements JokesRepository
{
    private List<Joke> mCachedJokes;
    private IcndbApiService mIcndbApiService;
    private SchedulerManager mSchedulerManager;
    private ConnectableObservable mCachedObservable;

    public InMemoryJokesRepository(IcndbApiService icndbApiService,
                                   SchedulerManager schedulerManager)
    {
        mIcndbApiService = icndbApiService;
        mSchedulerManager = schedulerManager;
    }

    @Override
    public void getJokes(Subscriber subscriber)
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
            mCachedObservable.subscribe(subscriber);
        } else
        {
            subscriber.onNext(mCachedJokes);
            subscriber.onCompleted();
        }
    }

//    @NonNull
//    private Action0 clearObservableCache()
//    {
//        return new Action0()
//        {
//            @Override
//            public void call()
//            {
//                mCachedObservable = null;
//            }
//        };
//    }

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
