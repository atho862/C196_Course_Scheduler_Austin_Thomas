package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.c196_course_scheduler_austin_thomas.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddEditCourseActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_START_DATE";
    public static final String EXTRA_ANTICIPATED_END_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ANTICIPATED_END_DATE";
    private EditText textCourseTitle;
    private DatePicker datePickerStartDate;
    private DatePicker datePickerAnticipatedEndDate;
    private ArrayList<String> courseStatus = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        courseStatus.add("Plan To Take");
        courseStatus.add("In Progress");
        courseStatus.add("Completed");
        courseStatus.add("Dropped");
        Spinner courseStatusSpinner = findViewById(R.id.spinner_course_status);
        courseStatusSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, courseStatus));
        courseStatusSpinner.setSelection(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        return true;
    }
}