package com.cachewithrealm;

import com.cachewithrealm.di.WeatherServiceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Bekti on 05/07/2017.
 */

@Singleton
@Component(modules = {
        WeatherServiceModule.class
})
public interface WeatherComponent {
    void inject(MainActivity activity);
}

