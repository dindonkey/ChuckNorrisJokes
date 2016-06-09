package it.dindonkey.chucknorrisjokes.androidtest;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.v7.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.jokes.JokesFragment;

public class ActivityTestCase
{
    protected static final List<Joke> TEST_JOKES = Collections.singletonList(new Joke(1,
            "test joke"));

    protected App getApplication()
    {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        return (App) instrumentation.getTargetContext().getApplicationContext();
    }

    protected JokesFragment getCurrentFragment(AppCompatActivity activity)
    {
        return (JokesFragment) activity
                .getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
    }
}
