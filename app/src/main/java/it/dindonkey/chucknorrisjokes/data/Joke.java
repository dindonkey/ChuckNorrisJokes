package it.dindonkey.chucknorrisjokes.data;

public class Joke
{
    public final int id;
    public final String joke;

    @SuppressWarnings("SameParameterValue")
    public Joke(int id, String joke)
    {
        this.id = id;
        this.joke = joke;
    }

}
