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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.Adapters.CourseAdapter;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.CourseViewModel;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.TermViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CourseListActivity extends AppCompatActivity {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;
    public static final int ADD_COURSE_MENTOR_REQUEST = 3;
    public static final int ADD_ASSESSMENT_REQUEST = 4;
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private List<String> terms;
    private Spinner termSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list2);
        setTitle("Courses");

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        RecyclerView recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(Course course) {
                Intent intent = new Intent(CourseListActivity.this, AddEditCourseActivity.class);
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ID, course.getCourseId());
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE, course.getCourseTitle());
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, course.getCourseStartDate().getTime());
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_ANTICIPATED_END_DATE, course.getAnticipatedEndDate().getTime());
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS, course.getCourseStatus());
                intent.putExtra(AddEditCourseActivity.EXTRA_COURSE_TERM_ID, course.getTermId());

                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);

        terms = termViewModel.getAllTermTitles();
        termSpinner = findViewById(R.id.spinner_term);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, terms);
        termSpinner.setAdapter(arrayAdapter);
        termSpinner.setSelection(0);
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String termTitle = terms.get(i);
                final int termId = termViewModel.getTermIdBytTermTitle(termTitle);
                courseViewModel.getAllCourses().observe(CourseListActivity.this, new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        adapter.setCourses(courseViewModel.getCoursesForTerm(termId));
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK){

            String courseTitle = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE);
            Date startDate = new Date(data.getLongExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, 0));
            Date anticipatedEndDate = new Date(data.getLongExtra(AddEditCourseActivity.EXTRA_COURSE_ANTICIPATED_END_DATE, 0));
            String courseStatus = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS);
            String termTitle = terms.get(termSpinner.getSelectedItemPosition());
            int termId = termViewModel.getTermIdBytTermTitle(termTitle);
            Course course = new Course(termId, courseTitle, courseStatus, startDate, anticipatedEndDate);
            courseViewModel.insertCourse(course);

            Toast.makeText(this,  "Successfully saved " + courseTitle + " in term " + termTitle, Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK){
            if (data.getStringExtra(AddEditCourseActivity.EXTRA_ACTION).equals("EDIT")){
                String courseTitle = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE);
                Date startDate = new Date(data.getLongExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, 0));
                Date anticipatedEndDate = new Date(data.getLongExtra(AddEditCourseActivity.EXTRA_COURSE_ANTICIPATED_END_DATE, 0));
                String courseStatus = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS);
                String termTitle = terms.get(termSpinner.getSelectedItemPosition());
                int termId = termViewModel.getTermIdBytTermTitle(termTitle);
                Course course = new Course(termId, courseTitle, courseStatus, startDate, anticipatedEndDate);

                int courseId = data.getIntExtra(AddEditCourseActivity.EXTRA_COURSE_ID, -1);
                if (courseId == -1){
                    Toast.makeText(this,"This course was not able to be updated. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                course.setCourseId(courseId);
                courseViewModel.updateCourse(course);
                Toast.makeText(this, courseTitle + " was successfully updated", Toast.LENGTH_SHORT).show();
            }
            else if (data.getStringExtra(AddEditCourseActivity.EXTRA_ACTION).equals("DELETE")){
                String courseTitle = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_TITLE);
                Date startDate = new Date(data.getLongExtra(AddEditCourseActivity.EXTRA_COURSE_START_DATE, 0));
                Date anticipatedEndDate = new Date(data.getLongExtra(AddEditCourseActivity.EXTRA_COURSE_ANTICIPATED_END_DATE, 0));
                String courseStatus = data.getStringExtra(AddEditCourseActivity.EXTRA_COURSE_STATUS);
                String termTitle = terms.get(termSpinner.getSelectedItemPosition());
                int termId = termViewModel.getTermIdBytTermTitle(termTitle);
                Course course = new Course(termId, courseTitle, courseStatus, startDate, anticipatedEndDate);

                int courseId = data.getIntExtra(AddEditCourseActivity.EXTRA_COURSE_ID, -1);
                if (courseId == -1){
                    Toast.makeText(this,"This course was not able to be deleted. Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }
                course.setCourseId(courseId);
                courseViewModel.deleteCourse(course);
                Toast.makeText(this, courseTitle + " successfully deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}