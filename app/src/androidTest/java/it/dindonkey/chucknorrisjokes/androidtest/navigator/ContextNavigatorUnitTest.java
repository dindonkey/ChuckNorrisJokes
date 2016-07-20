package it.dindonkey.chucknorrisjokes.androidtest.navigator;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.parceler.Parcels;

import it.dindonkey.chucknorrisjokes.data.Joke;
import it.dindonkey.chucknorrisjokes.jokedetail.JokeDetailActivity;
import it.dindonkey.chucknorrisjokes.navigator.ContextNavigator;
import it.dindonkey.chucknorrisjokes.sharedtest.SharedTestCase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ContextNavigatorUnitTest extends SharedTestCase
{

    @Mock
    Context mContextMock;
    private ContextNavigator mContextNavigator;

    @Before
    public void setUp() throws Exception
    {
        mContextNavigator = new ContextNavigator(mContextMock);
    }

    @Test
    public void should_start_joke_detail_activity() throws Exception
    {
        mContextNavigator.navigateToJokeDetail(TEST_JOKES.get(0));

        ArgumentCaptor<Intent> argumentCaptor = ArgumentCaptor.forClass(Intent.class);
        verify(mContextMock).startActivity(argumentCaptor.capture());

        assertEquals(JokeDetailActivity.class.getName(),
                argumentCaptor.getValue().getComponent().getClassName());

        Joke actualJoke = Parcels.unwrap(argumentCaptor
                .getValue()
                .getParcelableExtra(JokeDetailActivity.JOKE_EXTRA_KEY)
        );
        assertEquals(TEST_JOKES.get(0), actualJoke);
    }
}
