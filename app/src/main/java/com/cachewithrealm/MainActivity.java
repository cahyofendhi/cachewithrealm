package com.cachewithrealm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cachewithrealm.api.WeatherService;
import com.cachewithrealm.api.model.WeatherResponse;
import com.cachewithrealm.realm.WeatherRealm;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Inject
    public WeatherService service;

    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_temp)
    TextView textTemp;

    private Realm realmUI;
    private Disposable subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).component.inject(this);

        requestData();
    }

    private void requestData() {
        realmUI = Realm.getDefaultInstance();
        String name = "Jakarta";

        Observable<WeatherRealm> observable = service.getWeather(name, getString(R.string.api_key))
                .delay(1L, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .map(this::writeToRealm)
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::readFromRealm);
        WeatherRealm cacheWeather   = readFromRealm(name);
        if (cacheWeather !=null){
            observable  = observable.mergeWith(Observable.just(cacheWeather));
        }
        subscription    = observable.subscribe(this::display, this::processError);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.dispose();
        realmUI.close();
    }

    private WeatherRealm readFromRealm(String name) {
        return findInRealm(realmUI, name);
    }

    private String writeToRealm(WeatherResponse weatherResponse){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            WeatherRealm weatherRealm   = findInRealm(realm1, weatherResponse.getName());
            if (weatherRealm == null)
                weatherRealm    = realm1.createObject(WeatherRealm.class, weatherResponse.getName());
            weatherRealm.setTemp(weatherResponse.getMain().getTemp());
        });
        realm.close();
        return weatherResponse.getName();
    }

    private WeatherRealm findInRealm(Realm realm, String name){
        return realm.where(WeatherRealm.class).equalTo("name", name).findFirst();
    }

    private void processError(Throwable e) {
        Log.e(TAG, e.getLocalizedMessage(), e);
    }

    private void display(WeatherRealm weatherRealm) {
        Log.d(TAG, "City: " + weatherRealm.getName() + ", Temp: " + weatherRealm.getTemp());
        textName.setText(weatherRealm.getName());
        textTemp.setText(String.valueOf(weatherRealm.getTemp()));
    }

}
