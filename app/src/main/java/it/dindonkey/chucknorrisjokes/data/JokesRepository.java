package it.dindonkey.chucknorrisjokes.data;

import java.util.List;

import rx.Subscriber;

interface JokesRepository
{
    void jokes(Subscriber<List<Joke>> subscriber);

    void clearCache();

    void clearSubscription();
}
