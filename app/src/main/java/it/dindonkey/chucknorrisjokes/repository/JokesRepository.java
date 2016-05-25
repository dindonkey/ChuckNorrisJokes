package it.dindonkey.chucknorrisjokes.repository;

import it.dindonkey.chucknorrisjokes.model.JokesResponse;
import retrofit2.http.GET;
import rx.Observable;

public interface JokesRepository
{
    @GET("/")
    Observable<JokesResponse> jokes();
}
