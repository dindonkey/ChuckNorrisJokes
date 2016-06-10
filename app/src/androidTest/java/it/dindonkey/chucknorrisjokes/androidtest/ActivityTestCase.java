package it.dindonkey.chucknorrisjokes.androidtest;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.v7.app.AppCompatActivity;

import it.dindonkey.chucknorrisjokes.App;
import it.dindonkey.chucknorrisjokes.R;
import it.dindonkey.chucknorrisjokes.jokes.JokesFragment;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;

public class ActivityTestCase extends SharedTestCase
{
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
