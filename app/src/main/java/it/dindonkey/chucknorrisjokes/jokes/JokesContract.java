package it.dindonkey.chucknorrisjokes.jokes;

import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;

public interface JokesContract
{
    interface View
    {
        void showJokes(List<Joke> jokes);
    }

    interface UserActionsListener
    {
        void bindView(View view);

        void loadJokes();
    }
}
