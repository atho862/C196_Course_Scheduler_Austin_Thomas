package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Daos.CourseDao;
import com.example.c196_course_scheduler_austin_thomas.Database.CourseSchedulerDatabase;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;

import java.util.List;

public class CourseRepository {
    private CourseDao courseDao;
    private LiveData<List<Course>> allCourses;

    public CourseRepository(Application application){
        CourseSchedulerDatabase database = CourseSchedulerDatabase.getInstance(application);
        courseDao = database.courseDao();
        allCourses = courseDao.getAllCourses();
    }

    public void insertCourse(Course course){
        courseDao.insertCourse(course);
    }

    public void updateCourse(Course course){
        courseDao.updateCourse(course);
    }

    public void deleteCourse(Course course){
        courseDao.deleteCourse(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }
}
