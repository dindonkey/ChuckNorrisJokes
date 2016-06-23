package it.dindonkey.chucknorrisjokes.data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.mock.BehaviorDelegate;
import rx.Observable;

public class ChuckNorrisServiceApiMock implements ChuckNorrisServiceApi
{

    private final BehaviorDelegate<ChuckNorrisServiceApi> mDelegate;
    private final List<Joke> jokes;

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
