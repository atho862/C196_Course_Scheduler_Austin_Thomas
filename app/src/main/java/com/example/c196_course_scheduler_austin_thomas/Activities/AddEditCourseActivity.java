package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.example.c196_course_scheduler_austin_thomas.Daos.CourseDao;
import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.AssessmentViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.CourseMentorViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.CourseViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.NoteViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEditCourseActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ID";
    public static final String EXTRA_COURSE_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_TITLE";
    public static final String EXTRA_COURSE_START_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_START_DATE";
    public static final String EXTRA_COURSE_ANTICIPATED_END_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ANTICIPATED_END_DATE";
    public static final String EXTRA_COURSE_STATUS =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_TERM_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_TERM_TITLE";

    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;
    private NoteViewModel noteViewModel;
    private CourseMentorViewModel courseMentorViewModel;
    private EditText textCourseTitle;
    private DatePicker datePickerStartDate;
    private DatePicker datePickerAnticipatedEndDate;
    private ArrayList<String> courseStatus = new ArrayList<String>();
    private Spinner courseStatusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        RecyclerView notesRecyclerView = findViewById(R.id.notes_recycler_view);
        RecyclerView assessmentsRecyclerView = findViewById(R.id.recycler_view_assessments);
        RecyclerView courseMentorsRecyclerView = findViewById(R.id.course_mentor_recycler_view);

        textCourseTitle = findViewById(R.id.edit_text_course_title);
        datePickerStartDate = findViewById(R.id.date_picker_course_start_date);
        datePickerAnticipatedEndDate = findViewById(R.id.date_picker_course_end_date);

        Intent intent = getIntent();
        if (intent.getIntExtra(EXTRA_COURSE_ID, 0)!= 0){

            courseStatus.add("Plan To Take");
            courseStatus.add("In Progress");
            courseStatus.add("Completed");
            courseStatus.add("Dropped");
            courseStatusSpinner = findViewById(R.id.spinner_course_status);
            courseStatusSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, courseStatus));
            int currentStatusPosition = courseStatus.indexOf(intent.getStringExtra(EXTRA_COURSE_STATUS));
            courseStatusSpinner.setSelection(currentStatusPosition);
        }

        courseStatus.add("Plan To Take");
        courseStatus.add("In Progress");
        courseStatus.add("Completed");
        courseStatus.add("Dropped");
        courseStatusSpinner = findViewById(R.id.spinner_course_status);
        courseStatusSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, courseStatus));
        courseStatusSpinner.setSelection(1);
    }

    public void saveCourse(){
        Calendar startDateCalendar = new GregorianCalendar();
        Calendar endDateCalendar = new GregorianCalendar();
        String courseTitle = textCourseTitle.getText().toString();
        int courseStatusSelectedPosition = courseStatusSpinner.getSelectedItemPosition();
        String selectedStatus = courseStatus.get(courseStatusSelectedPosition);

        startDateCalendar.set(datePickerStartDate.getYear(), datePickerStartDate.getMonth(), datePickerStartDate.getDayOfMonth());
        endDateCalendar.set(datePickerAnticipatedEndDate.getYear(), datePickerAnticipatedEndDate.getMonth(), datePickerAnticipatedEndDate.getDayOfMonth());
        Date startDate = startDateCalendar.getTime();
        Date endDate = endDateCalendar.getTime();

        if (courseTitle.trim().isEmpty()){
            Toast.makeText(this, "Please enter a course title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.before(startDate)){
            Toast.makeText(this, "Your start date must be before your anticipated end date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        Intent intent = getIntent();
        data.putExtra(EXTRA_COURSE_TITLE, courseTitle);
        data.putExtra(EXTRA_COURSE_START_DATE, startDate);
        data.putExtra(EXTRA_COURSE_ANTICIPATED_END_DATE, endDate);
        data.putExtra(EXTRA_COURSE_STATUS, selectedStatus);
        data.putExtra(EXTRA_COURSE_TERM_TITLE, intent.getStringExtra(EXTRA_COURSE_TERM_TITLE));

        int id = data.getIntExtra(EXTRA_COURSE_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_COURSE_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        Intent intent = getIntent();
        if (intent.getIntExtra(EXTRA_COURSE_ID, 0) != 0){
            //inflate the edit menu
        }
        else {
            inflater.inflate(R.menu.add_course_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.save_course_menu_item:
                saveCourse();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}