package com.justplaingoatappsgmail.phonesilencer;

import android.app.Application;
import com.justplaingoatappsgmail.phonesilencer.dagger.ApplicationComponent;
import com.justplaingoatappsgmail.phonesilencer.dagger.DaggerApplicationComponent;
import com.justplaingoatappsgmail.phonesilencer.dagger.modules.ApplicationModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class PhoneSilencerApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        component = initDagger(this);
    }

    protected void initRealm() {
        Realm.init(this);
        // configure realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("timers.realm")
                .build();
        // Make this realm the default
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    protected ApplicationComponent initDagger(PhoneSilencerApplication application) {
        // DaggerApplicationComponent is generated by Dagger 2
        // Part of component processor in which was created when we did
        // "make project" on our application.
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }

}
