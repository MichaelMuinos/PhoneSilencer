package com.justplaingoatappsgmail.phonesilencer.contracts;

import com.justplaingoatappsgmail.phonesilencer.BasePresenter;
import com.justplaingoatappsgmail.phonesilencer.BaseView;
import com.justplaingoatappsgmail.phonesilencer.model.Event;

import java.util.List;

public interface EventPostContract {

    interface View extends BaseView {
        void showEventNameError();
        void showEventNameDuplicateError();
        void showStartEndTimeConflictError();
        void showNoDaysSelectedError();
    }

    interface Presenter extends BasePresenter<View> {
        void saveEvent(String id, String title, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, int ringerMode, List<Integer> days, String repeat, boolean update);
        void deleteRequestCodes(Event event);
        void updateEvent(Event event);
        String convertTimeToString(int hourOfDay, int minute);
        int getDay(String day);
    }

}
