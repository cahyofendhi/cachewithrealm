package com.cachewithrealm;

import android.app.Application;

import com.cachewithrealm.di.WeatherServiceModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Bekti on 05/07/2017.
 */

public class App extends Application {

    public WeatherComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

        component = DaggerWeatherComponent
                .builder()
                .weatherServiceModule(new WeatherServiceModule(getString(R.string.base_url)))
                .build();
    }

}
