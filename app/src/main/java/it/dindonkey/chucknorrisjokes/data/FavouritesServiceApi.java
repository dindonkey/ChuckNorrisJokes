package it.dindonkey.chucknorrisjokes.data;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;


public interface FavouritesServiceApi
{
    @GET("/it/favourites")
    Observable<List<FavouriteOffer>> getFavourites();

    @POST("/it/favourites")
    Observable<FavouriteOffer> addFavourite(@Body FavouriteOffer favouriteOffer);
}
