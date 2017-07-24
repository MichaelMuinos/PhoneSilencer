package com.justplaingoatappsgmail.phonesilencer.presenter;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPostPresenter implements EventPostContract.Presenter {

    private EventPostContract.View view;
    private RealmService realmService;
    private static Map<String,Integer> dayMap;

    static {
        dayMap = new HashMap<>();
        dayMap.put("MO", Calendar.MONDAY);
        dayMap.put("TU", Calendar.TUESDAY);
        dayMap.put("WE", Calendar.WEDNESDAY);
        dayMap.put("TH", Calendar.THURSDAY);
        dayMap.put("FR", Calendar.FRIDAY);
        dayMap.put("SA", Calendar.SATURDAY);
        dayMap.put("SU", Calendar.SUNDAY);
    }

    public EventPostPresenter(RealmService realmService) {
        this.realmService = realmService;
    }

    @Override
    public void setView(EventPostContract.View view) {
        this.view = view;
    }

    @Override
    public void saveEvent(String id, String title, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, int ringerMode, List<Integer> days, String repeat, boolean update) {
        if(isValidName(title, update) && isValidTimeInterval(startTimeHour, startTimeMinute, endTimeHour, endTimeMinute) && hasAtLeastOneDaySelected(days)) {
            realmService.addEvent(id, title, startTimeHour, startTimeMinute, endTimeHour, endTimeMinute, ringerMode, days, repeat, false, update);
        }
    }

    @Override
    public void deleteRequestCodes(Event event) {
        realmService.deleteRealmPendingIntent(event);
    }

    @Override
    public void updateEvent(Event event) {
        realmService.updateEventEnabled(event.getId(), false);
    }

    @Override
    public String convertTimeToString(int hourOfDay, int minute) {
        String partOfDay = hourOfDay < 12 ? "AM" : "PM";
        int hour = hourOfDay % 12;
        String hourToString = hour == 0 ? "12" : (hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour));
        String minuteToString = minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute);
        return hourToString + ":" + minuteToString + " " + partOfDay;
    }

    @Override
    public int getDay(String day) {
        return dayMap.get(day);
    }

    @Override
    public void closeRealm() {
        realmService.closeRealmInstance();
    }

    /**
     * Method checks to see if the name entered for the event is valid or not. If the name is empty or a duplicate,
     * we return an error message; however, if we are updating an event, we should ignore the duplicate name error.
     * @param title
     * @param update
     * @return
     */
    private boolean isValidName(String title, boolean update) {
        // if the name is not valid, show our name error message
        if(title.isEmpty() || title.trim().length() == 0) {
            view.showEventNameError();
            return false;
        // if the name is a duplicate, show our name dup error message
        } else if(realmService.containsName(title) && !update) {
            view.showEventNameDuplicateError();
            return false;
        // otherwise, it must be a valid name
        } else {
            return true;
        }
    }

    /**
     * Method checks to see if the time.png.png.png.png interval is the exact same. If so, it is invalid.
     * @return
     */
    private boolean isValidTimeInterval(int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute) {
        if(startTimeHour == endTimeHour && startTimeMinute == endTimeMinute) {
            view.showStartEndTimeConflictError();
            return false;
        }
        return true;
    }

    private boolean hasAtLeastOneDaySelected(List<Integer> days) {
        if(days.size() == 0) {
            view.showNoDaysSelectedError();
            return false;
        }
        return true;
    }

}
