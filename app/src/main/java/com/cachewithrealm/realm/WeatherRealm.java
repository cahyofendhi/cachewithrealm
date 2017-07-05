package com.cachewithrealm.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Bekti on 05/07/2017.
 */

public class WeatherRealm extends RealmObject {
    @PrimaryKey
    private String name;
    private Double temp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}