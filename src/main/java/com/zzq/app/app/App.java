package com.zzq.app.app;

import android.app.Application;

import com.zzq.app.injector.component.AppComponent;
import com.zzq.app.injector.component.DaggerAppComponent;
import com.zzq.app.injector.module.AppModule;

public class App extends Application {
    private static App instance;
    public static AppComponent appComponent;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
    }

    public static AppComponent getAppComponent(){
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .build();
        }
        return appComponent;
    }
}
