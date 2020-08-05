package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.PrimaryKey;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.Receivers.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class AddEditTermActivity extends AppCompatActivity {
    public static final int ADD_NOTIFICATION_REQUEST = 1;
    public static final String EXTRA_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_END_DATE";
    public static final String EXTRA_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ID";
    public static final String EXTRA_TERM_ACTION =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_TERM_ACTION";

    private EditText editTextTermTitle;
    private DatePicker datePickerTermStartDate;
    private DatePicker datePickerTermEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        editTextTermTitle = findViewById(R.id.edit_text_title);
        datePickerTermStartDate = findViewById(R.id.start_date_picker);
        datePickerTermEndDate = findViewById(R.id.end_date_picker);

        datePickerTermStartDate.setMinDate(new Date().getTime());
        datePickerTermEndDate.setMinDate(new Date().getTime());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Term");

            Date startDate = new Date(intent.getLongExtra(EXTRA_START_DATE, 0));
            Date endDate = new Date(intent.getLongExtra(EXTRA_END_DATE, 0));
            Calendar startDateCalendar = Calendar.getInstance();
            Calendar endDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            endDateCalendar.setTime(endDate);

            editTextTermTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            datePickerTermStartDate.updateDate(startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DATE));
            datePickerTermEndDate.updateDate(endDateCalendar.get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DATE));
        }
        else {
            setTitle("Add Term");
        }
    }

    private void saveTerm(String action){
        Calendar calendarForStartDate = Calendar.getInstance();
        Calendar calendarForEndDate = Calendar.getInstance();
        calendarForStartDate.set(datePickerTermStartDate.getYear(), datePickerTermStartDate.getMonth(), datePickerTermStartDate.getDayOfMonth());

        String termTitle = editTextTermTitle.getText().toString();
        Date termStartDate = calendarForStartDate.getTime();

        calendarForEndDate.set(datePickerTermEndDate.getYear(), datePickerTermEndDate.getMonth(), datePickerTermEndDate.getDayOfMonth());
        Date termEndDate = calendarForEndDate.getTime();

        if (termTitle.trim().isEmpty()){
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (termStartDate.after(termEndDate)) {
            Toast.makeText(this, "Your term start date must be before your term end date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, termTitle);
        data.putExtra(EXTRA_START_DATE, termStartDate.getTime());
        data.putExtra(EXTRA_END_DATE, termEndDate.getTime());
        data.putExtra(EXTRA_TERM_ACTION, action);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        if (getIntent().hasExtra(EXTRA_ID)){
            menuInflater.inflate(R.menu.edit_term_menu, menu);
        }
        else {
            menuInflater.inflate(R.menu.add_term_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_term:
                saveTerm("ADD");
                return true;
            case R.id.edit_term_save_term_menu_item:
                saveTerm("EDIT");
                return true;
            case R.id.edit_term_delete_term_menu_item:
                saveTerm("DELETE");
                return true;
            case R.id.edit_term_notification_term_start_date:
                addNotification("START_DATE");
                return true;
            case R.id.edit_term_notification_term_end_date:
                addNotification("END_DATE");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNotification(String type){
        String notificationTitle;
        String notificationDescription;
        String notificationType = "TERM";
        Calendar calendar = Calendar.getInstance();
        Intent notificationIntent = new Intent(AddEditTermActivity.this, NotificationReceiver.class);

        if (type.equals("START_DATE")){
            notificationTitle = "Your " + editTextTermTitle.getText().toString() + " term starts today!";
            notificationDescription = "Just a friendly reminder that your " + editTextTermTitle.getText().toString() + " term starts today. Good luck!";
            calendar.setTime(new Date(getIntent().getLongExtra(EXTRA_START_DATE, 0)));
        }
        else {
            notificationTitle = "Your " + editTextTermTitle.getText().toString() + " term ends today!";
            notificationDescription = "Just a friendly reminder that your " + editTextTermTitle.getText().toString() + " term is scheduled to end today. Make sure your have completed all scheduled courses!";
            calendar.setTime(new Date(getIntent().getLongExtra(EXTRA_END_DATE, 0)));
        }

        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TITLE, notificationTitle);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_DATA, notificationDescription);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TYPE, notificationType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditTermActivity.this, ADD_NOTIFICATION_REQUEST, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}