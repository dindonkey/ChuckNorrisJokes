package it.dindonkey.chucknorrisjokes.data;

import java.util.List;

import rx.Subscriber;

interface JokesRepository
{
    void getJokes(Subscriber<List<Joke>> subscriber);

    void clearCache();

    void clearSubscription();
}
