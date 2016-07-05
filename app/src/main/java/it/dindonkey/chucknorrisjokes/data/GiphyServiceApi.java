package it.dindonkey.chucknorrisjokes.data;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GiphyServiceApi
{
    @GET("/v1/gifs/random")
    Observable<GiphyGif> getRandomGif(@Query("api_key") String apiKey, @Query("tag") String tag);
}
