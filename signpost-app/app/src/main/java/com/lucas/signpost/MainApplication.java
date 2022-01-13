package com.lucas.signpost;

import android.app.Application;

import io.realm.Realm;

public class MainApplication extends Application {

    public ApplicationComponent applicationComponent = DaggerApplicationComponent.create();

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }

}
