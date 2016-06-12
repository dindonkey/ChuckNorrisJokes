package it.dindonkey.chucknorrisjokes.data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.mock.BehaviorDelegate;
import rx.Observable;

/**
 * Created by simonecaldon on 12/06/16.
 */
public class ChuckNorrisServiceApiMock implements ChuckNorrisServiceApi
{

    private BehaviorDelegate<ChuckNorrisServiceApi> mDelegate;
    private List<Joke> jokes;

    public ChuckNorrisServiceApiMock(BehaviorDelegate<ChuckNorrisServiceApi> delegate)
    {
        mDelegate = delegate;
        jokes = new ArrayList<>(1);
        jokes.add(new Joke(1,"mock joke description"));
    }

    @Override
    public Observable<List<Joke>> getJokes()
    {
        return mDelegate.returningResponse(jokes).getJokes();
    }
}
