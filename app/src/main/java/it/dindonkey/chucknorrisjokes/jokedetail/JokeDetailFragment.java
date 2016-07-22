package it.dindonkey.chucknorrisjokes.jokedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.Joke;

public class JokeDetailFragment extends Fragment
{
    private static Joke mJoke;

    public static JokeDetailFragment newInstance(Joke joke)
    {
        mJoke = joke;
        return new JokeDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_joke_detail, container, false);
        TextView jokeTextView = (TextView) rootView.findViewById(R.id.joke_text);
        ImageView jokeImageView = (ImageView) rootView.findViewById(R.id.joke_image);

        jokeTextView.setText(mJoke.joke);
        Glide
                .with(JokeDetailFragment.this)
                .load(mJoke.gifUrl)
                .asGif()
                .into(jokeImageView);
        return rootView;
    }
}
