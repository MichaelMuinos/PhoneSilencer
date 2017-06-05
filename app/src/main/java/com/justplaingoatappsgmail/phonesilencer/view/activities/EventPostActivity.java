package com.justplaingoatappsgmail.phonesilencer.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.justplaingoatappsgmail.phonesilencer.PhoneSilencerApplication;
import com.justplaingoatappsgmail.phonesilencer.R;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class EventPostActivity extends AppCompatActivity implements EventPostContract.View {

    @BindView(R.id.event_post_name) EditText eventName;
    @BindViews({R.id.event_post_monday_text_view,
                R.id.event_post_tuesday_text_view,
                R.id.event_post_wednesday_text_view,
                R.id.event_post_thursday_text_view,
                R.id.event_post_friday_text_view,
                R.id.event_post_saturday_text_view,
                R.id.event_post_sunday_text_view})
    List<TextView> days;

    @Inject EventPostContract.Presenter presenter;
    @Inject Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_post);
        // bind our views
        ButterKnife.bind(this);
        // allow injection of presenter
        ((PhoneSilencerApplication) getApplication()).getComponent().inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // set view to null to avoid memory leak
        presenter.setView(null);
        // close our realm instance
        presenter.closeRealm();
    }

    @Override
    public void showEventNameError() {

    }

    @Override
    public void showEventNameDuplicateError() {

    }

    @Override
    public void showStartEndTimeConflictError() {

    }

    @Override
    public void showNoDaysSelectedError() {

    }

    @Override
    public void returnToEventListActivity() {
        Intent intent = new Intent(EventPostActivity.this, EventListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.event_post_start_time)
    public void onStartTimeClick() {

    }

    @OnClick(R.id.event_post_end_time)
    public void onEndTimeClick() {

    }

    /**
     * On click for the monday - sunday text views. When clicked, they will transition into a different drawable.
     * The method will check to see if the current drawable in the view is equal to the circle_green drawable.
     * This is my workaround currently because the getColor method part of GradientDrawable is only available in API 24
     * and above.
     * https://stackoverflow.com/questions/9125229/comparing-two-drawables-in-android
     * @param view
     */
    @OnClick({R.id.event_post_monday_text_view, R.id.event_post_tuesday_text_view, R.id.event_post_wednesday_text_view, R.id.event_post_thursday_text_view, R.id.event_post_friday_text_view, R.id.event_post_saturday_text_view, R.id.event_post_sunday_text_view})
    public void onDayClick(View view) {
        Drawable.ConstantState circleGreenDrawable = ContextCompat.getDrawable(context, R.drawable.circle_green).getConstantState();
        int drawable = view.getBackground().getConstantState().equals(circleGreenDrawable) ? R.drawable.circle_red : R.drawable.circle_green;
        view.setBackgroundResource(drawable);
    }

    /**
     * On Check Changed method for the all days check box.
     * This will determine whether we need to color all of the days
     * with the red or green drawable.
     * @param isChecked
     */
    @OnCheckedChanged(R.id.event_post_check_box)
    public void onCheckBoxCheckedChanged(boolean isChecked) {
        if(isChecked) setDrawableForAllDays(ContextCompat.getDrawable(context, R.drawable.circle_red));
        else setDrawableForAllDays(ContextCompat.getDrawable(context, R.drawable.circle_green));
    }

    /**
     * On click method for save button to save the Event
     */
    @OnClick(R.id.event_post_save_button)
    public void onSaveButtonClick() {
        presenter.saveEvent(eventName.getText().toString());
    }

    /**
     * Method will set all day text views to the same drawable if they
     * are not already containing that drawable
     * @param drawable
     */
    private void setDrawableForAllDays(Drawable drawable) {
        for(TextView textView : days) {
            if(!textView.getBackground().getConstantState().equals(drawable.getConstantState())) {
                textView.setBackground(drawable);
            }
        }
    }

}
