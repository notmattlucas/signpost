package com.lucas.signpost;

import com.lucas.signpost.di.MessagesModule;
import com.lucas.signpost.ui.LoginActivity;
import com.lucas.signpost.ui.MapsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {MessagesModule.class}
)
@Singleton
public interface ApplicationComponent {

    void injectMaps(MapsActivity mapsActivity);

    void injectLogin(LoginActivity loginActivity);

}
