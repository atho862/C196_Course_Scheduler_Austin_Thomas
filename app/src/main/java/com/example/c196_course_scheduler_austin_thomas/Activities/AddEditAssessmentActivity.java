package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.Receivers.NotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddEditAssessmentActivity extends AppCompatActivity {
    public static final int ADD_NOTIFICATION_REQUEST = 1;
    public static final String EXTRA_ASSESSMENT_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_COURSE_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_COURSE_ID";
    public static final String EXTRA_ASSESSMENT_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_TITLE";
    public static final String EXTRA_ASSESSMENT_TYPE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_TYPE";
    public static final String EXTRA_ASSESSMENT_DUE_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_DUE_DATE";
    public static final String EXTRA_ASSESSMENT_GOAL_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_GOAL_DATE";
    public static final String EXTRA_ASSESSMENT_ACTION =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ASSESSMENT_ACTION";

    private EditText editTextAssessmentTitle;
    private Spinner spinnerAssessmentType;
    private DatePicker datePickerAssessmentDueDate;
    private DatePicker datePickerAssessmentGoalDate;
    private int courseId;
    private List<String> assessmentTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_assessment);

        editTextAssessmentTitle = findViewById(R.id.edit_text_assessment_title);
        spinnerAssessmentType = findViewById(R.id.assessment_type_spinner);
        datePickerAssessmentDueDate = findViewById(R.id.date_picker_assessment_due_date);
        datePickerAssessmentGoalDate = findViewById(R.id.date_picker_assessment_goal_date);
        courseId = getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, 0);

        if (courseId == 0){
            setTitle("Add Assessment");
            initAssessmentTypeSpinner(false);
        }
        else {
            initAssessmentTypeSpinner(true);
            editTextAssessmentTitle.setText(getIntent().getStringExtra(EXTRA_ASSESSMENT_TITLE));
            initDueDateDatePicker();
            initGoalDateDatePicker();
        }
    }

    public void saveAssessment(String action){
        String assessmentTitle = editTextAssessmentTitle.getText().toString();
        String assessmentType = assessmentTypes.get(spinnerAssessmentType.getSelectedItemPosition());
        Date dueDate = getDateFromDueDatePicker();
        Date goalDate = getDateFromGoalDatePicker();

        if (assessmentTitle.trim().isEmpty()){
            Toast.makeText(this, "Please enter a title for the assessment", Toast.LENGTH_LONG).show();
            return;
        }

        if (goalDate.after(dueDate)){
            Toast.makeText(this, "Your goal date must be before the assessment due date", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_ASSESSMENT_TITLE, assessmentTitle);
        data.putExtra(EXTRA_ASSESSMENT_TYPE, assessmentType);
        data.putExtra(EXTRA_ASSESSMENT_DUE_DATE, dueDate.getTime());
        data.putExtra(EXTRA_ASSESSMENT_GOAL_DATE, goalDate.getTime());
        data.putExtra(EXTRA_ASSESSMENT_COURSE_ID, courseId);
        data.putExtra(EXTRA_ASSESSMENT_ACTION, action);

        if (getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, 0) != 0) {
            data.putExtra(EXTRA_ASSESSMENT_ID, getIntent().getIntExtra(EXTRA_ASSESSMENT_ID, 0));
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if (courseId == 0){
            inflater.inflate(R.menu.add_assessment_menu, menu);
        }
        else {
            inflater.inflate(R.menu.edit_assessment_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_assessment_save_menu_item:
                saveAssessment("ADD");
                return true;
            case R.id.edit_assessment_save_menu_item:
                saveAssessment("EDIT");
                return true;
            case R.id.edit_assessment_delete_menu_item:
                saveAssessment("DELETE");
                return true;
            case R.id.add_notification_assessment_goal_date:
                scheduleNotification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initAssessmentTypeSpinner(boolean isEdit){
        assessmentTypes.add("Objective Assessment");
        assessmentTypes.add("Performance Assessment");
        spinnerAssessmentType.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, assessmentTypes));
        if (isEdit){
            int currentAssessmentTypePosition = assessmentTypes.indexOf(getIntent().getStringExtra(EXTRA_ASSESSMENT_TYPE));
            spinnerAssessmentType.setSelection(currentAssessmentTypePosition);
        }
        else {
            spinnerAssessmentType.setSelection(0);
        }
    }

    private void initDueDateDatePicker(){
        Calendar dueDateCalendar = new GregorianCalendar();
        Date dueDate = new Date(getIntent().getLongExtra(EXTRA_ASSESSMENT_DUE_DATE, 0));
        dueDateCalendar.setTime(dueDate);
        datePickerAssessmentDueDate.updateDate(dueDateCalendar.get(Calendar.YEAR), dueDateCalendar.get(Calendar.MONTH), dueDateCalendar.get(Calendar.DATE));
    }

    private void initGoalDateDatePicker(){
        Calendar goalDateCalendar = new GregorianCalendar();
        Date goalDate = new Date(getIntent().getLongExtra(EXTRA_ASSESSMENT_GOAL_DATE, 0));
        goalDateCalendar.setTime(goalDate);
        datePickerAssessmentGoalDate.updateDate(goalDateCalendar.get(Calendar.YEAR), goalDateCalendar.get(Calendar.MONTH), goalDateCalendar.get(Calendar.DATE));
    }

    private Date getDateFromDueDatePicker(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(datePickerAssessmentDueDate.getYear(), datePickerAssessmentDueDate.getMonth(), datePickerAssessmentDueDate.getDayOfMonth());
        return calendar.getTime();
    }

    private Date getDateFromGoalDatePicker(){
        Calendar calendar = new GregorianCalendar();
        calendar.set(datePickerAssessmentGoalDate.getYear(), datePickerAssessmentGoalDate.getMonth(), datePickerAssessmentGoalDate.getDayOfMonth());
        return calendar.getTime();
    }

    private void scheduleNotification(){
        String notificationTitle = "Today's the day!";
        String notificationData = "Today is your goal date for " + editTextAssessmentTitle.getText().toString() + ". Good luck!";
        String notificationType = "ASSESSMENT";
        Intent notificationIntent = new Intent(AddEditAssessmentActivity.this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TITLE, notificationTitle);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_DATA, notificationData);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TYPE, notificationType);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getIntent().getLongExtra(EXTRA_ASSESSMENT_GOAL_DATE, 0)));
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditAssessmentActivity.this, ADD_NOTIFICATION_REQUEST, notificationIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}