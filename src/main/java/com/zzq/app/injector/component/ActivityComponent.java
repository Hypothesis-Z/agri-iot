package com.zzq.app.injector.component;

import android.app.Activity;

import com.zzq.app.injector.module.ActivityModule;
import com.zzq.app.injector.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)//可以依赖多个Component
public interface ActivityComponent {
    Activity getActivity();
}
