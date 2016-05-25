package it.dindonkey.chucknorrisjokes.model;

import java.util.List;

public class JokesResponse
{
    public String type;
    public List<Joke> value;

    public JokesResponse(String type, List<Joke> value)
    {
        this.type = type;
        this.value = value;
    }
}
