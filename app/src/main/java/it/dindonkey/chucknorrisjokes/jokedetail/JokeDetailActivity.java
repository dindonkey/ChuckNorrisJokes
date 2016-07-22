package it.dindonkey.chucknorrisjokes.jokedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.Joke;

public class JokeDetailActivity extends AppCompatActivity
{
    public static final String JOKE_EXTRA_KEY = "joke";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_detail);

        Joke joke = getJokeFromIntentExtras();
        if (null == savedInstanceState && null != joke)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, JokeDetailFragment.newInstance(joke));
            transaction.commit();
        }
    }

    private Joke getJokeFromIntentExtras()
    {
        return Parcels.unwrap(getIntent().getParcelableExtra(JOKE_EXTRA_KEY));
    }
}
