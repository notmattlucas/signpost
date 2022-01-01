package com.lucas.signpost;

import android.app.Application;

public class MainApplication extends Application {

    ApplicationComponent applicationComponent = DaggerApplicationComponent.create();

}
