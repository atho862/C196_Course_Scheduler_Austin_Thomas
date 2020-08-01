package com.example.c196_course_scheduler_austin_thomas.ViewModels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;
import com.example.c196_course_scheduler_austin_thomas.Repository.CourseRepository;
import java.util.ArrayList;
import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private CourseRepository repository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(Application application){
        super(application);
        repository = new CourseRepository(application);
        allCourses = repository.getAllCourses();
    }

    public void insertCourse(Course course){
        repository.insertCourse(course);
    }

    public void updateCourse(Course course){
        repository.updateCourse(course);
    }

    public void deleteCourse(Course course){
        repository.deleteCourse(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public List<Course> getCoursesForTerm(int termId){

        List<Course> coursesForTerm = repository.getCoursesByTermId(termId);
        return coursesForTerm;
    }

}
