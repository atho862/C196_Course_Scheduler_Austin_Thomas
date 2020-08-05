package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.Adapters.AssessmentAdapter;
import com.example.c196_course_scheduler_austin_thomas.Adapters.CourseMentorAdapter;
import com.example.c196_course_scheduler_austin_thomas.Adapters.NoteAdapter;
import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;
import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;
import com.example.c196_course_scheduler_austin_thomas.Entities.Note;
import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.Receivers.NotificationReceiver;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.AssessmentViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.CourseMentorViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.CourseViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.NoteViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddEditCourseActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final int ADD_COURSE_MENTOR_REQUEST = 3;
    public static final int EDIT_COURSE_MENTOR_REQUEST = 4;
    public static final int EDIT_ASSESSMENT_REQUEST = 5;
    public static final int ADD_ASSESSMENT_REQUEST = 6;
    public static final int ADD_NOTIFICATION_REQUEST = 7;

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
    public static final String EXTRA_COURSE_TERM_ID = "" +
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_TERM_ID";
    public static final String EXTRA_ACTION =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ACTION";

    private int courseId;
    private AssessmentViewModel assessmentViewModel;
    private NoteViewModel noteViewModel;
    private CourseMentorViewModel courseMentorViewModel;
    private EditText textCourseTitle;
    private DatePicker datePickerStartDate;
    private DatePicker datePickerAnticipatedEndDate;
    private ArrayList<String> courseStatus = new ArrayList<String>();
    private Spinner courseStatusSpinner;
    private ImageButton imageButtonAddNote;
    private ImageButton imageButtonAddCourseMentor;
    private ImageButton imageButtonAddAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        final Intent intent = getIntent();

        textCourseTitle = findViewById(R.id.edit_text_course_title);
        datePickerStartDate = findViewById(R.id.date_picker_course_start_date);
        datePickerAnticipatedEndDate = findViewById(R.id.date_picker_course_end_date);
        imageButtonAddAssessment = findViewById(R.id.button_add_assessment);
        imageButtonAddCourseMentor = findViewById(R.id.button_add_course_mentor);
        imageButtonAddNote = findViewById(R.id.button_add_note);

        if (intent.getIntExtra(EXTRA_COURSE_ID, 0)!= 0){
            courseId = intent.getIntExtra(EXTRA_COURSE_ID, 0);
            setTitle("Edit Course");

            initNotesRecyclerView();
            initCourseMentorRecyclerView();
            initAssessmentRecyclerView();
            initCourseStatusSpinner(true);
            initAssessmentButton(true);
            initNoteButton(true);
            initCourseMentorButton(true);

            textCourseTitle.setText(intent.getStringExtra(EXTRA_COURSE_TITLE));
            Date startDate = new Date(intent.getLongExtra(EXTRA_COURSE_START_DATE, 0));
            Date endDate = new Date(intent.getLongExtra(EXTRA_COURSE_ANTICIPATED_END_DATE, 0));
            Calendar startDateCalendar = new GregorianCalendar();
            Calendar endDateCalendar = new GregorianCalendar();
            endDateCalendar.setTime(endDate);
            startDateCalendar.setTime(startDate);
            datePickerStartDate.updateDate(startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DATE));
            datePickerAnticipatedEndDate.updateDate(endDateCalendar.get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DATE));
        }
        else {
            setTitle("Add Course");
            initAssessmentButton(false);
            initCourseMentorButton(false);
            initNoteButton(false);
            initCourseStatusSpinner(false);
        }
    }

    public void saveCourse(String action){
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
        data.putExtra(EXTRA_COURSE_START_DATE, startDate.getTime());
        data.putExtra(EXTRA_COURSE_ANTICIPATED_END_DATE, endDate.getTime());
        data.putExtra(EXTRA_COURSE_STATUS, selectedStatus);
        data.putExtra(EXTRA_ACTION, action);

        int id = intent.getIntExtra(EXTRA_COURSE_ID, -1);
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
            inflater.inflate(R.menu.edit_course_menu, menu);
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
                saveCourse("ADD");
                return true;
            case R.id.edit_course_save_menu_item:
                saveCourse("EDIT");
                return true;
            case R.id.edit_course_delete_course_menu_item:
                saveCourse("DELETE");
                return true;
            case R.id.add_note_menu_item:
                Intent noteIntent = new Intent(this, AddEditNoteActivity.class);
                noteIntent.putExtra(AddEditNoteActivity.EXTRA_COURSE_ID, courseId);
                startActivityForResult(noteIntent, ADD_NOTE_REQUEST);
                return true;
            case R.id.edit_course_add_course_mentor_menu_item:
                Intent mentorIntent = new Intent(this, AddEditCourseMentorActivity.class);
                mentorIntent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_COURSE_ID, courseId);
                startActivityForResult(mentorIntent, ADD_COURSE_MENTOR_REQUEST);
                return true;
            case R.id.add_course_start_date_notification:
                scheduleNotification("START_DATE");
                return true;
            case R.id.add_course_end_date_notification:
                scheduleNotification("END_DATE");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String noteText = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_TEXT);
            int courseId = data.getIntExtra(AddEditNoteActivity.EXTRA_COURSE_ID, 0);

            Note note = new Note(courseId, noteText);
            noteViewModel.insertNote(note);

            Toast.makeText(this, "Note successfully added!", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            String noteText = data.getStringExtra(AddEditNoteActivity.EXTRA_NOTE_TEXT);
            int courseId = data.getIntExtra(AddEditNoteActivity.EXTRA_COURSE_ID, 0);
            int noteId = data.getIntExtra(AddEditNoteActivity.EXTRA_NOTE_ID, 0);

            Note note = new Note(courseId, noteText);
            note.setNoteId(noteId);
            noteViewModel.updateNote(note);

            Toast.makeText(this, "Note successfully updated", Toast.LENGTH_SHORT);
        }
        else if (requestCode == ADD_COURSE_MENTOR_REQUEST && resultCode == RESULT_OK){
            String courseMentorName = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_NAME);
            String courseMentorPhone = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_PHONE);
            String courseMentorEmail = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_EMAIL);
            int courseId = data.getIntExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_COURSE_ID, 0);
            CourseMentor courseMentor = new CourseMentor(courseId, courseMentorName, courseMentorPhone, courseMentorEmail);
            courseMentorViewModel.insertCourseMentor(courseMentor);

            Toast.makeText(this, courseMentorName + " successfully added as a course mentor", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_COURSE_MENTOR_REQUEST && resultCode == RESULT_OK){
            if (data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_ACTION).equals("EDIT")){
                String courseMentorName = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_NAME);
                String courseMentorPhone = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_PHONE);
                String courseMentorEmail = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_EMAIL);
                int courseId = data.getIntExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_COURSE_ID, 0);
                CourseMentor courseMentor = new CourseMentor(courseId, courseMentorName, courseMentorPhone, courseMentorEmail);
                courseMentor.setMentorId(data.getIntExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_ID, 0));

                courseMentorViewModel.updateCourseMentor(courseMentor);

                Toast.makeText(this, "Successfully updated information for " + courseMentorName, Toast.LENGTH_SHORT).show();
            }
            else {
                String courseMentorName = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_NAME);
                String courseMentorPhone = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_PHONE);
                String courseMentorEmail = data.getStringExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_EMAIL);
                int courseId = data.getIntExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_COURSE_ID, 0);
                CourseMentor courseMentor = new CourseMentor(courseId, courseMentorName, courseMentorPhone, courseMentorEmail);
                courseMentor.setMentorId(data.getIntExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_ID, 0));

                courseMentorViewModel.deleteCourseMentor(courseMentor);

                Toast.makeText(this, "Successfully deleted " + courseMentorName, Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == ADD_ASSESSMENT_REQUEST && resultCode == RESULT_OK){
            int assessmentCount = assessmentViewModel.getAssessmentCountsForCourse(courseId);
            if (assessmentCount > 5){
                Toast.makeText(this, "You may only add 5 assessments to a course", Toast.LENGTH_LONG).show();
                return;
            }
            String assessmentTitle = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE);
            String assessmentType = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE);
            Date dueDate = new Date(data.getLongExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE, 0));
            Date goalDate = new Date(data.getLongExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_GOAL_DATE, 0));
            Assessment assessment = new Assessment(courseId, assessmentTitle, dueDate, assessmentType, goalDate);
            assessmentViewModel.insertAssessment(assessment);

            Toast.makeText(this, assessmentTitle + " successfully added!", Toast.LENGTH_LONG).show();
        }
        else if (requestCode == EDIT_ASSESSMENT_REQUEST && resultCode == RESULT_OK){
            if (data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_ACTION).equals("EDIT")){
                String assessmentTitle = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE);
                String assessmentType = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE);
                Date dueDate = new Date(data.getLongExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE, 0));
                Date goalDate = new Date(data.getLongExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_GOAL_DATE, 0));
                Assessment assessment = new Assessment(courseId, assessmentTitle, dueDate, assessmentType, goalDate);
                assessment.setAssessmentId(data.getIntExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_ID, -1));
                assessmentViewModel.updateAssessment(assessment);

                Toast.makeText(this, assessmentTitle + " successfully updated!", Toast.LENGTH_LONG).show();
            }
            else {
                String assessmentTitle = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE);
                String assessmentType = data.getStringExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE);
                Date dueDate = new Date(data.getLongExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE, 0));
                Date goalDate = new Date(data.getLongExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_GOAL_DATE, 0));
                Assessment assessment = new Assessment(courseId, assessmentTitle, dueDate, assessmentType, goalDate);
                assessment.setAssessmentId(data.getIntExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_ID, 0));
                assessmentViewModel.deleteAssessment(assessment);

                Toast.makeText(this, assessmentTitle + " successfully removed", Toast.LENGTH_LONG).show();
            }
        }
        else {

        }
    }

    private void initNotesRecyclerView(){
        RecyclerView notesRecyclerView = findViewById(R.id.notes_recycler_view);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setHasFixedSize(true);
        final NoteAdapter noteAdapter = new NoteAdapter();
        notesRecyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                Intent intent = getIntent();
                List<Note> notesForCourse = noteViewModel.getNotesForCourse(intent.getIntExtra(EXTRA_COURSE_ID, 0));
                noteAdapter.setNotes(notesForCourse);
            }
        });

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(AddEditCourseActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_ID, note.getNoteId());
                intent.putExtra(AddEditNoteActivity.EXTRA_COURSE_ID, note.getCourseId());
                intent.putExtra(AddEditNoteActivity.EXTRA_NOTE_TEXT, note.getNote());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    private void initCourseMentorRecyclerView(){
        RecyclerView courseMentorsRecyclerView = findViewById(R.id.course_mentor_recycler_view);
        courseMentorsRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        courseMentorsRecyclerView.setHasFixedSize(true);
        final CourseMentorAdapter courseMentorAdapter = new CourseMentorAdapter();
        courseMentorsRecyclerView.setAdapter(courseMentorAdapter);

        courseMentorViewModel = ViewModelProviders.of(this).get(CourseMentorViewModel.class);
        courseMentorViewModel.getAllCourseMentors().observe(this, new Observer<List<CourseMentor>>() {
            @Override
            public void onChanged(List<CourseMentor> courseMentors) {
                Intent intent = getIntent();
                List<CourseMentor> courseMentorsForCourse = courseMentorViewModel.getCourseMentorsForCourse(intent.getIntExtra(EXTRA_COURSE_ID, 0));
                courseMentorAdapter.setCourseMentors(courseMentorsForCourse);
            }
        });

        courseMentorAdapter.setOnItemClickListener(new CourseMentorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CourseMentor courseMentor) {
                Intent intent = new Intent(AddEditCourseActivity.this, AddEditCourseMentorActivity.class);
                intent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_ID, courseMentor.getMentorId());
                intent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_COURSE_ID, courseMentor.getCourseId());
                intent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_NAME, courseMentor.getMentorName());
                intent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_EMAIL, courseMentor.getMentorEmailAddress());
                intent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_PHONE, courseMentor.getMentorPhone());

                startActivityForResult(intent, EDIT_COURSE_MENTOR_REQUEST);
            }
        });
    }

    private void initAssessmentRecyclerView(){
        RecyclerView assessmentsRecyclerView = findViewById(R.id.recycler_view_assessments);
        assessmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentsRecyclerView.setHasFixedSize(true);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        assessmentsRecyclerView.setAdapter(assessmentAdapter);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                List<Assessment> assessmentsForCourse = assessmentViewModel.getAssessmentsForCourse(courseId);
                assessmentAdapter.setAssessments(assessmentsForCourse);
            }
        });

        assessmentAdapter.setOnItemClickListener(new AssessmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Intent intent = new Intent(AddEditCourseActivity.this, AddEditAssessmentActivity.class);
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_ID, assessment.getAssessmentId());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TITLE, assessment.getAssessmentTitle());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_TYPE, assessment.getAssessmentType());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_DUE_DATE, assessment.getDueDate().getTime());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_GOAL_DATE, assessment.getGoalDate().getTime());
                intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_COURSE_ID, assessment.getCourseId());
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
            }
        });
    }

    private void initCourseStatusSpinner(boolean isEdit){
        courseStatus.add("Plan To Take");
        courseStatus.add("In Progress");
        courseStatus.add("Completed");
        courseStatus.add("Dropped");
        if (isEdit){
            courseStatusSpinner = findViewById(R.id.spinner_course_status);
            courseStatusSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, courseStatus));
            int currentStatusPosition = courseStatus.indexOf(getIntent().getStringExtra(EXTRA_COURSE_STATUS));
            courseStatusSpinner.setSelection(currentStatusPosition);
        }
        else {
            courseStatusSpinner = findViewById(R.id.spinner_course_status);
            courseStatusSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, courseStatus));
            courseStatusSpinner.setSelection(1);
        }
    }

    private void initAssessmentButton(boolean isEdit){
        if (isEdit){
            imageButtonAddAssessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AddEditCourseActivity.this, AddEditAssessmentActivity.class);
                    intent.putExtra(AddEditAssessmentActivity.EXTRA_ASSESSMENT_COURSE_ID, courseId);
                    startActivityForResult(intent, ADD_ASSESSMENT_REQUEST);
                }
            });
        }
        else {
            imageButtonAddAssessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddEditCourseActivity.this, "You must save the course before adding any assessments", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initNoteButton(boolean isEdit){
        if (isEdit){
            imageButtonAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent noteIntent = new Intent(AddEditCourseActivity.this, AddEditNoteActivity.class);
                    noteIntent.putExtra(AddEditNoteActivity.EXTRA_COURSE_ID, courseId);
                    startActivityForResult(noteIntent, ADD_NOTE_REQUEST);
                }
            });
        }
        else {
            imageButtonAddNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddEditCourseActivity.this, "You must save the course before adding any notes", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void initCourseMentorButton(boolean isEdit){
        if (isEdit){
            imageButtonAddCourseMentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mentorIntent = new Intent(AddEditCourseActivity.this, AddEditCourseMentorActivity.class);
                    mentorIntent.putExtra(AddEditCourseMentorActivity.EXTRA_COURSE_MENTOR_COURSE_ID, courseId);
                    startActivityForResult(mentorIntent, ADD_COURSE_MENTOR_REQUEST);
                }
            });
        }
        else {
            imageButtonAddCourseMentor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(AddEditCourseActivity.this, "You must save the course before adding any course mentors", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void scheduleNotification(String type){
        String courseNotificationTitle;
        String courseNotificationData;
        String notificationType = "COURSE";
        Calendar calendar = Calendar.getInstance();

        if (type.equals("START_DATE")){
            courseNotificationTitle = textCourseTitle.getText().toString() + " starts today!";
            courseNotificationData = "A friendly reminder that your " + textCourseTitle.getText().toString() + " is scheduled to start today";
            calendar.setTime(new Date(getIntent().getLongExtra(EXTRA_COURSE_START_DATE, 0)));
        }
        else {
            courseNotificationTitle = textCourseTitle.getText().toString() + " ends today!";
            courseNotificationData = "A friendly reminder that your " + textCourseTitle.getText().toString() + " is scheduled to end today. Make sure you have completed all of the necessary assessments!";
            calendar.setTime(new Date(getIntent().getLongExtra(EXTRA_COURSE_ANTICIPATED_END_DATE, 0)));
        }

        Intent notificationIntent = new Intent(AddEditCourseActivity.this, NotificationReceiver.class);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TITLE, courseNotificationTitle);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_DATA, courseNotificationData);
        notificationIntent.putExtra(NotificationReceiver.EXTRA_NOTIFICATION_TYPE, notificationType);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddEditCourseActivity.this, ADD_NOTIFICATION_REQUEST, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}