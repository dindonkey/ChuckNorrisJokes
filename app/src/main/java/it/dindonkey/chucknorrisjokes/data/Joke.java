package it.dindonkey.chucknorrisjokes.data;

public class Joke
{
    public int id;
    public String joke;

    @SuppressWarnings("SameParameterValue")
    public Joke(int id, String joke)
    {
        this.id = id;
        this.joke = joke;
    }

}
