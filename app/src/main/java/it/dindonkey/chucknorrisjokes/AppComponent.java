package it.dindonkey.chucknorrisjokes;

import javax.inject.Singleton;

import dagger.Component;
import it.dindonkey.chucknorrisjokes.jokes.ErrorFragment;
import it.dindonkey.chucknorrisjokes.jokes.JokesFragment;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent
{
    void inject(JokesFragment fragment);

    void inject(ErrorFragment errorFragment);
}
