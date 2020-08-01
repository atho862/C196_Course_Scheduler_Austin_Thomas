package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import com.example.c196_course_scheduler_austin_thomas.Activities.CourseListActivity;
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
        new InsertCourseAsyncTask(courseDao).execute(course);
    }

    public void updateCourse(Course course){
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }

    public void deleteCourse(Course course){
        courseDao.deleteCourse(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public List<Course> getCoursesByTermId(int termId){
        try {
            return new GetCoursesByTermIdAsyncTask(courseDao).execute(termId).get();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    private static class InsertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao courseDao;

        private InsertCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.insertCourse(courses[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        private UpdateCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.updateCourse(courses[0]);
            return null;
        }
    }

    private static class GetCoursesByTermIdAsyncTask extends AsyncTask<Integer, Void, List<Course>>{
        private CourseDao courseDao;

        private GetCoursesByTermIdAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected List<Course> doInBackground(Integer... integers) {
            return courseDao.getCoursesByTermId(integers[0]);
        }
    }
}
