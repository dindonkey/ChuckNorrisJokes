package it.dindonkey.chucknorrisjokes.model;

import java.util.List;

public class JokesResponse
{
    public List<Joke> value;

    public JokesResponse(List<Joke> value)
    {
        this.value = value;
    }
}
