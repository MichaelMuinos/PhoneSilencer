package com.justplaingoatappsgmail.phonesilencer.presenter;

import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;

public class SetNormalServicePresenter {

    private RealmService realmService;

    public SetNormalServicePresenter(RealmService realmService) {
        this.realmService = realmService;
    }

    public void closeRealm() {
        realmService.closeRealmInstance();
    }

    public int retrieveNotificationIdAndDelete(Event event) {
        return realmService.retrieveAndDeleteNotification(event);
    }

}
