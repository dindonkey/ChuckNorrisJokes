package it.dindonkey.chucknorrisjokes.navigator;

import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.jokedetail.JokeDetailActivity;

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
        Intent intent = new Intent(mContext, JokeDetailActivity.class);
        intent.putExtra(JokeDetailActivity.JOKE_EXTRA_KEY, Parcels.wrap(joke));
        mContext.startActivity(intent);
    }
}
