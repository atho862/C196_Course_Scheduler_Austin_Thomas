package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.c196_course_scheduler_austin_thomas.Adapters.CourseAdapter;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.CourseViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.TermViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseListActivity extends AppCompatActivity {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;
    public static final int ADD_COURSE_MENTOR_REQUEST = 3;
    public static final int ADD_ASSESSMENT_REQUEST = 4;
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private ArrayList<String> terms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list2);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        RecyclerView recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        try {
            List<String> termTitles = termViewModel.getAllTermTitles();
            Spinner termSpinner = findViewById(R.id.spinner_term);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, termTitles);
            termSpinner.setAdapter(arrayAdapter);
            termSpinner.setSelection(0);
        }
        catch (ExecutionException e){
            System.out.println(e);
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter.setCourses(courses);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.add_course_menu_item:
                Intent intent = new Intent(CourseListActivity.this, AddEditCourseActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
                return true;
            case R.id.view_assessments_menu_option:
                //do assessment stuff
                return true;
            case R.id.view_course_mentor_menu_item:
                //do course mentor stuff
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}