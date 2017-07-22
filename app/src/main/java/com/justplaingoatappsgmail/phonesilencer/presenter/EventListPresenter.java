package com.justplaingoatappsgmail.phonesilencer.presenter;

import android.util.Log;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EventListPresenter implements EventListContract.Presenter {

    private EventListContract.View view;
    private RealmService realmService;
    private AtomicInteger atomicInteger;

    public EventListPresenter(RealmService realmService, AtomicInteger atomicInteger) {
        this.realmService = realmService;
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void setView(EventListContract.View view) {
        this.view = view;
        // call getEvents from our view and set the list to our adapter
        // in the activity
        view.showEvents();
    }

    @Override
    public List<Event> getEvents() {
        List<Event> results = realmService.getAllEvents();
        // if we don't have any events, show message to user to create some
        if(results.size() == 0) view.showSnackBarNoEventsMessage();
        return results;
    }

    @Override
    public List<Integer> getEventRequestCodes(Event event) {
        return realmService.getRealmPendingIntentRequestCodes(event);
    }

    @Override
    public void addRequestCodes(String eventName, List<Integer> requestCodes) {
        realmService.addPendingIntentRequestCodes(eventName, requestCodes);
        view.showEventEnabledMessage(eventName);
    }

    @Override
    public void updateEvent(Event event, boolean update) {
        realmService.updateEventEnabled(event.getId(), update);
    }

    @Override
    public int getIncrementedRequestCode() {
        if(atomicInteger.get() == Integer.MAX_VALUE) atomicInteger.set(0);
        return atomicInteger.incrementAndGet();
    }

    @Override
    public Calendar setCalendar(int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Log.d("Test", "Calender Time: " + calendar.getTime().toString());

        return calendar;
    }

    @Override
    public void deleteEvent(Event event) {
        realmService.deleteEvent(event);
    }

    @Override
    public void deleteRequestCodes(Event event, boolean showMessage) {
        realmService.deleteRealmPendingIntent(event);
        if(showMessage) view.showEventDisabledMessage(event.getEventName());
    }

    @Override
    public void closeRealm() {
        realmService.closeRealmInstance();
    }

}
