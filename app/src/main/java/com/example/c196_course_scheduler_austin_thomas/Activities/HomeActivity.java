package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.c196_course_scheduler_austin_thomas.R;

public class HomeActivity extends AppCompatActivity {
    public static final int VIEW_TERMS_REQUEST = 1;
    public static final int VIEW_COURSES_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_home);
        super.onCreate(savedInstanceState);

        Button buttonViewCourses = findViewById(R.id.button_view_courses);
        Button buttonViewTerms = findViewById(R.id.button_view_terms);
        buttonViewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TermListActivity.class);
                startActivityForResult(intent, VIEW_TERMS_REQUEST);
            }
        });
        buttonViewCourses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CourseListActivity.class);
                startActivityForResult(intent, VIEW_COURSES_REQUEST);
            }
        });



    }
}