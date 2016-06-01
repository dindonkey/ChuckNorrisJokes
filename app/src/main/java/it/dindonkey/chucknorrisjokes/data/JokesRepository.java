package it.dindonkey.chucknorrisjokes.data;

import java.util.List;

import rx.Subscriber;

public interface JokesRepository
{
    void getJokes(Subscriber<List<Joke>> subscriber);

    void clearCache();
}
