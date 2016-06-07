package it.dindonkey.chucknorrisjokes.jokes;

import java.util.ArrayList;

import it.dindonkey.chucknorrisjokes.data.Joke;

public class JokesPresenter implements JokesContract.UserActionsListener
{
    private JokesContract.View mView;

    @Override
    public void bindView(JokesContract.View view)
    {
        mView = view;
    }

    @Override
    public void loadJokes()
    {
        mView.showJokes(new ArrayList<Joke>(0));
    }
}
