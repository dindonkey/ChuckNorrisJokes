package it.dindonkey.chucknorrisjokes.repository;

import java.util.List;

import it.dindonkey.chucknorrisjokes.model.Joke;
import retrofit2.http.GET;
import rx.Observable;

public interface IcndbApiService
{
    @GET("/")
    Observable<List<Joke>> jokes();
}
