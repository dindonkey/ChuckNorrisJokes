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
import java.util.concurrent.TimeUnit;

import it.dindonkey.chucknorrisjokes.BuildConfig;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public class ChuckNorrisServiceApiRetrofit
{

    public static ChuckNorrisServiceApi createService(HttpUrl baseUrl)
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        NetworkBehavior behavior = NetworkBehavior.create();
        behavior.setDelay(BuildConfig.CHUCK_API_NETWORK_BEHAVIOUR_DELAY, TimeUnit.SECONDS);
        behavior.setFailurePercent(BuildConfig.CHUCK_API_NETWORK_BEHAVIOUR_FAILURE_PERCENT);
        behavior.setVariancePercent(BuildConfig.CHUCK_API_NETWORK_BEHAVIOUR_VARIANCE_PERCENT);

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(behavior)
                .build();

        BehaviorDelegate<ChuckNorrisServiceApi> delegate = mockRetrofit.create(ChuckNorrisServiceApi.class);

        return new ChuckNorrisServiceApiMock(delegate);
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
                    delegate.write(out, value);
                }

                public T read(JsonReader in) throws IOException
                {

                    JsonElement jsonElement = elementAdapter.read(in);
                    if (jsonElement.isJsonObject())
                    {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.has("value"))
                        {
                            jsonElement = jsonObject.get("value");
                        }
                    }

                    return delegate.fromJsonTree(jsonElement);
                }
            }.nullSafe();
        }
    }
}
