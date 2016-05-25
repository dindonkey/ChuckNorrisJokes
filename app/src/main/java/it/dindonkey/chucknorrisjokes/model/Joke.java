package it.dindonkey.chucknorrisjokes.model;

import java.util.List;

public class Joke
{
    public int id;
    public String joke;
    public List<String> categories;

    public Joke(int id, String joke, List<String> categories)
    {
        this.id = id;
        this.joke = joke;
        this.categories = categories;
    }

}
