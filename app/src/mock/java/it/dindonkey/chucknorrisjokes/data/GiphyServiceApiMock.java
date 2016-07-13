package it.dindonkey.chucknorrisjokes.data;

import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;
import rx.Observable;

public class GiphyServiceApiMock implements GiphyServiceApi
{
    private BehaviorDelegate<GiphyServiceApi> mDelegate;
    private GiphyGif mGiphyGif;

    public GiphyServiceApiMock(BehaviorDelegate<GiphyServiceApi> delegate)
    {
        mDelegate = delegate;
        mGiphyGif = new GiphyGif("https://media2.giphy.com/media/pbXhMwguvmxZS/200_d.gif");
    }

    @Override
    public Observable<GiphyGif> getRandomGif(@Query("api_key") String apiKey,
                                             @Query("tag") String tag)
    {
        return mDelegate.returningResponse(mGiphyGif).getRandomGif("anyKey", "anyTag");
    }
}
