package com.justplaingoatappsgmail.phonesilencer.view.adapters;

import android.media.AudioManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.customlisteners.EventListListener;
import com.justplaingoatappsgmail.phonesilencer.model.Event;
import com.veinhorn.tagview.TagView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder> {

    private List<Event> eventList;
    private EventListListener listener;
    private static Map<Integer,String> dayMap;

    static {
        dayMap = new HashMap<>();
        dayMap.put(1, "SU");
        dayMap.put(2, "M");
        dayMap.put(3, "T");
        dayMap.put(4, "W");
        dayMap.put(5, "TH");
        dayMap.put(6, "F");
        dayMap.put(7, "SA");
    }

    public EventListAdapter(EventListListener listener) {
        this.listener = listener;
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_post_item, parent, false);
        return new EventListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
        // grab event object from list
        Event event = eventList.get(position);
        // set event name
        holder.eventName.setText(event.getEventName());
        // create days string
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < event.getDays().size(); i++) {
            sb.append(dayMap.get(event.getDays().get(i).getRealmInt()));
            if(i != event.getDays().size() - 1) sb.append("-");
        }
        // set days tag
        holder.daysTag.setText(sb.toString());
        // create start time string
        String startTime = (event.getStartTimeHour() < 10 ? "0" + String.valueOf(event.getStartTimeHour()) : String.valueOf(event.getStartTimeHour()))
                + ":" + (event.getStartTimeMinute() < 10 ? "0" + String.valueOf(event.getStartTimeMinute()) : String.valueOf(event.getStartTimeMinute()))
                + (event.getStartTimeAmOrPm() == Calendar.AM ? "AM" : "PM");
        // create end time string
        String endTime = (event.getEndTimeHour() < 10 ? "0" + String.valueOf(event.getEndTimeHour()) : String.valueOf(event.getEndTimeHour()))
                + ":" + (event.getEndTimeMinute() < 10 ? "0" + String.valueOf(event.getEndTimeMinute()) : String.valueOf(event.getEndTimeMinute()))
                + (event.getEndTimeAmOrPm() == Calendar.AM ? "AM" : "PM");
        // set time tag
        holder.timeTag.setText(startTime + " to " + endTime);
        // set ringer tag
        holder.ringerTag.setText(event.getRingerMode() == AudioManager.RINGER_MODE_SILENT ? "Silent" : "Vibrate");
        // set repeat tag
        holder.repeatTag.setText(event.getRepeat().toString());
        // set position tag
        holder.positionTag.setText(holder.switchEvent.getText());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public static class EventListViewHolder extends RecyclerView.ViewHolder {

        private EventListListener listener;

        @BindView(R.id.event_post_item_title) TextView eventName;
        @BindView(R.id.event_post_item_days_tag) TagView daysTag;
        @BindView(R.id.event_post_item_time_tag) TagView timeTag;
        @BindView(R.id.event_post_item_ringer_tag) TagView ringerTag;
        @BindView(R.id.event_post_item_repeat_tag) TagView repeatTag;
        @BindView(R.id.event_post_item_position_tag) TagView positionTag;
        @BindView(R.id.event_post_item_switch) Switch switchEvent;

        public EventListViewHolder(View itemView, EventListListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.event_card_view)
        public void onEventCardViewClick() {
            listener.onEventListCardViewClick(this.getLayoutPosition());
        }

        @OnCheckedChanged(R.id.event_post_item_switch)
        public void onSwitchCheckedChanged(boolean isChecked) {
            listener.onEventListSwitchCheckedChanged(this.getLayoutPosition(), switchEvent, positionTag, isChecked);
        }

        @OnClick(R.id.event_post_item_close_button)
        public void onCloseButtonClick() {
            listener.onEventListDeleteClickListener(this.getLayoutPosition());
        }

    }

}
