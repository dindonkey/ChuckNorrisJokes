package it.dindonkey.chucknorrisjokes.repository;

import rx.Subscriber;

public interface JokesRepository
{
    void getJokes(Subscriber subscriber);

    void clearCache();
}
