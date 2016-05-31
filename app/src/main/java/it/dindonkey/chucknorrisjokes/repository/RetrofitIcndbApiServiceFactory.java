package it.dindonkey.chucknorrisjokes.repository;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitIcndbApiServiceFactory
{
    private static IcndbApiService mInstance;

    public static IcndbApiService createService(HttpUrl baseUrl)
    {
        if (null == mInstance)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            mInstance = retrofit.create(IcndbApiService.class);
        }
        return mInstance;
    }
}
