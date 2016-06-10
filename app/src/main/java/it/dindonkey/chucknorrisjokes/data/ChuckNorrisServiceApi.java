package it.dindonkey.chucknorrisjokes.data;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface ChuckNorrisServiceApi
{
    @GET("/jokes/")
    Observable<List<Joke>> getJokes();
}
