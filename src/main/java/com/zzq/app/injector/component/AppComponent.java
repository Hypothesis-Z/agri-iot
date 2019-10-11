package com.zzq.app.injector.component;

import com.zzq.app.app.App;
import com.zzq.app.injector.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    App getContext();
}
