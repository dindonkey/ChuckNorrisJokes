package it.dindonkey.chucknorrisjokes.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by simonecaldon on 08/12/2016.
 */
public class FavouritesApiRetrofitFactory
{
    public static FavouritesServiceApi createService(HttpUrl baseUrl)
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(FavouritesServiceApi.class);
    }

    private static class ItemTypeAdapterFactory implements TypeAdapterFactory
    {

        public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type)
        {

            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

            return new TypeAdapter<T>()
            {

                public void write(JsonWriter out, T value) throws IOException
                {

//                    delegate.write(out, value);
                    FavouriteOffer favouriteOffer = (FavouriteOffer)value;
                    out.beginObject();
                    out.name("uuid").value("ciccio@ciccio.it");
                    out.name("favouriteid").value(favouriteOffer.favouriteId);
                    out.name("offerid").value(favouriteOffer.offer.id);
                    out.endObject();
                }

                public T read(JsonReader in) throws IOException
                {

                    JsonElement jsonElement = elementAdapter.read(in);
                    if (jsonElement.isJsonObject())
                    {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.has("result"))
                        {
                            jsonElement = jsonObject.get("result");

                            if (jsonElement.getAsJsonObject().has("favourites"))
                            {
                                jsonElement = jsonElement.getAsJsonObject().get("favourites");
                            }
                        }
                    }

                    return delegate.fromJsonTree(jsonElement);
                }
            }.nullSafe();
        }
    }
}
