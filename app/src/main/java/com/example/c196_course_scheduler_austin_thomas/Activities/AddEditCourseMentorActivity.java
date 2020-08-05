package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.R;

public class AddEditCourseMentorActivity extends AppCompatActivity {
    public static final String EXTRA_COURSE_MENTOR_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_MENTOR_ID";
    public static final String EXTRA_COURSE_MENTOR_NAME =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_MENTOR_NAME";
    public static final String EXTRA_COURSE_MENTOR_EMAIL =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_MENTOR_EMAIL";
    public static final String EXTRA_COURSE_MENTOR_PHONE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_MENTOR_PHONE";
    public static final String EXTRA_COURSE_MENTOR_COURSE_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_MENTOR_COURSE_ID";
    public static final String EXTRA_COURSE_MENTOR_ACTION =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_MENTOR_ACTION";

    private EditText editTextCourseMentorName;
    private EditText editTextCourseMentorEmail;
    private EditText editTextCourseMentorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course_mentor);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        editTextCourseMentorEmail = findViewById(R.id.edit_text_course_mentor_email);
        editTextCourseMentorName = findViewById(R.id.edit_text_course_mentor_name);
        editTextCourseMentorPhone = findViewById(R.id.edit_text_course_mentor_phone);

        if (getIntent().getIntExtra(EXTRA_COURSE_MENTOR_ID, 0) != 0){
            setTitle("Edit Course Mentor");
            editTextCourseMentorName.setText(getIntent().getStringExtra(EXTRA_COURSE_MENTOR_NAME));
            editTextCourseMentorPhone.setText(getIntent().getStringExtra(EXTRA_COURSE_MENTOR_PHONE));
            editTextCourseMentorEmail.setText(getIntent().getStringExtra(EXTRA_COURSE_MENTOR_EMAIL));

        }
        else {
            setTitle("Add Course Mentor");
        }
    }

    public void saveCourseMentor(String action){
        String courseMentorName = editTextCourseMentorName.getText().toString();
        String courseMentorEmail = editTextCourseMentorEmail.getText().toString();
        String courseMentorPhone = editTextCourseMentorPhone.getText().toString();

        if (courseMentorName.trim().isEmpty()){
            Toast.makeText(this, "Please enter the course mentor's name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseMentorEmail.trim().isEmpty()){
            Toast.makeText(this, "Please enter an email address for the course mentor", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseMentorPhone.trim().isEmpty()){
            Toast.makeText(this, "Please enter a phone number for the course mentor", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_COURSE_MENTOR_NAME, courseMentorName);
        data.putExtra(EXTRA_COURSE_MENTOR_EMAIL, courseMentorEmail);
        data.putExtra(EXTRA_COURSE_MENTOR_PHONE, courseMentorPhone);
        data.putExtra(EXTRA_COURSE_MENTOR_COURSE_ID, getIntent().getIntExtra(EXTRA_COURSE_MENTOR_COURSE_ID, 0));
        data.putExtra(EXTRA_COURSE_MENTOR_ACTION, action);
        if (getIntent().getIntExtra(EXTRA_COURSE_MENTOR_ID, 0) != 0){
            data.putExtra(EXTRA_COURSE_MENTOR_ID, getIntent().getIntExtra(EXTRA_COURSE_MENTOR_ID, 0));
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        if (getIntent().getIntExtra(EXTRA_COURSE_MENTOR_ID, 0) != 0){
            inflater.inflate(R.menu.edit_course_mentor_menu, menu);
        }
        else {
            inflater.inflate(R.menu.add_course_mentor_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.add_course_mentor_save_item:
                saveCourseMentor("ADD");
            case R.id.edit_course_mentor_save_item:
                saveCourseMentor("EDIT");
            case R.id.edit_course_mentor_delete_item:
                saveCourseMentor("DELETE");
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}