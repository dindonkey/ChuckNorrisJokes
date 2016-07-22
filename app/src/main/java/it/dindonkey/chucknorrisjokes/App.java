package it.dindonkey.chucknorrisjokes;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class App extends Application
{
    private AppComponent component;

    @Override
    public void onCreate()
    {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG)
        {
            LeakCanary.install(this);
        }
    }

    public AppComponent getComponent()
    {
        return component;
    }

}
