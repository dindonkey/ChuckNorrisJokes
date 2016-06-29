package it.dindonkey.chucknorrisjokes.data;

import java.util.List;

import rx.Observer;

public interface JokesRepository
{
    void getJokes(Observer<List<Joke>> observer);

    void clearCache();

    void clearSubscription();
}
