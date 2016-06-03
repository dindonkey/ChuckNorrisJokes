package it.dindonkey.chucknorrisjokes.jokes;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import it.dindonkey.chucknorrisjokes.R;

public class JokesActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);

        if(null == savedInstanceState)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, JokesFragment.newInstance());
            transaction.commit();
        }
    }
}
