package it.dindonkey.chucknorrisjokes.navigator;

import android.content.Context;

import it.dindonkey.chucknorrisjokes.data.Joke;

public class ContextNavigator implements Navigator
{
    private Context mContext;

    public ContextNavigator(Context context)
    {
        mContext = context;
    }

    @Override
    public void navigateToJokeDetail(Joke joke)
    {
        //TODO: use mContext
    }
}
