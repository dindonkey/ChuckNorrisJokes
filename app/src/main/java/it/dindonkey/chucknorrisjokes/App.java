package it.dindonkey.chucknorrisjokes;

import android.app.Application;

import it.dindonkey.chucknorrisjokes.jokes.JokesContract;
import it.dindonkey.chucknorrisjokes.jokes.JokesPresenter;

public class App extends Application
{
    private JokesContract.UserActionsListener mJokesUserActionsListener;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mJokesUserActionsListener = new JokesPresenter();
    }

    public JokesContract.UserActionsListener getJokesUserActionsListener()
    {
        return mJokesUserActionsListener;
    }

    public void setJokesUserActionsListener(JokesContract.UserActionsListener jokesUserActionsListener)
    {
        this.mJokesUserActionsListener = jokesUserActionsListener;
    }
}
