package it.dindonkey.chucknorrisjokes.jokes;

import java.util.List;

import it.dindonkey.chucknorrisjokes.data.Joke;

@SuppressWarnings("unused")
public interface JokesContract
{
    interface View
    {
        void showJokes(List<Joke> jokes);

        void showLoading();

        void hideLoading();

        void showError();

        void showJokeDetail(Joke joke);
    }

    interface UserActionsListener
    {
        void bindView(View view);

        void unBindView();

        void loadJokes(boolean refreshData);

        void openJokeDetail(Joke joke);
    }
}
