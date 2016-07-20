package it.dindonkey.chucknorrisjokes.data;

import org.parceler.Parcel;

@Parcel
public class Joke
{
    public int id;
    public String joke;
    public String gifUrl;

    public Joke()
    {
    }

    public Joke(int id, String joke)
    {
        this.id = id;
        this.joke = joke;
    }

}
