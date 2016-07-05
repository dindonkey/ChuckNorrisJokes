package it.dindonkey.chucknorrisjokes.data;

import com.google.gson.annotations.SerializedName;

public class GiphyGif
{
    @SerializedName("fixed_height_downsampled_url")
    public final String url;

    public GiphyGif(String url)
    {
        this.url = url;
    }
}
