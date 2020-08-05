package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

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

    public void deleteCourse(Course course) {
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public LiveData<List<Course>> getAllCourses(){
        return allCourses;
    }

    public int getCourseCountByTermId(int termId){
        try {
            return new GetCourseCountByTermIdAsyncTask(courseDao).execute(termId).get();
        }
        catch (Exception e){
            System.out.println(e);
            return 0;
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

    private static class DeleteCourseAsyncTask extends AsyncTask<Course, Void, Void>{
        private CourseDao courseDao;

        private DeleteCourseAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Course... courses) {
            courseDao.deleteCourse(courses[0]);
            return null;
        }
    }

    private static class GetCourseCountByTermIdAsyncTask extends AsyncTask<Integer, Void, Integer>{
        private CourseDao courseDao;

        private GetCourseCountByTermIdAsyncTask(CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return courseDao.getCountOfCoursesByTermId(integers[0]);
        }
    }
}
