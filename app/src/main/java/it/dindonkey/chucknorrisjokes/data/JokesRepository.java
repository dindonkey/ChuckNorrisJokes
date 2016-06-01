package it.dindonkey.chucknorrisjokes.data;

import rx.Subscriber;

public interface JokesRepository
{
    void getJokes(Subscriber subscriber);

    void clearCache();
}
