package com.lucas.signpost;

import com.lucas.signpost.di.MessagesModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(
        modules = {MessagesModule.class}
)
@Singleton
public interface ApplicationComponent {

    void inject(MapsActivity mapsActivity);

}
