package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.example.c196_course_scheduler_austin_thomas.Daos.CourseMentorDao;
import com.example.c196_course_scheduler_austin_thomas.Database.CourseSchedulerDatabase;
import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;

import java.util.List;

public class CourseMentorRepository {
    private CourseMentorDao courseMentorDao;
    private LiveData<List<CourseMentor>> allCourseMentors;

    public CourseMentorRepository(Application application){
        CourseSchedulerDatabase database = CourseSchedulerDatabase.getInstance(application);
        courseMentorDao = database.courseMentorDao();
        allCourseMentors = courseMentorDao.getAllCourseMentors();
    }

    public void insertCourseMentor(CourseMentor courseMentor){
        new InsertCourseMentorAsyncTask(courseMentorDao).execute(courseMentor);
    }

    public void updateCourseMentor(CourseMentor courseMentor){
        new UpdateCourseMentorAsyncTask(courseMentorDao).execute(courseMentor);
    }

    public void deleteCourseMentor(CourseMentor courseMentor){
        new DeleteCourseMentorAsyncTask(courseMentorDao).execute(courseMentor);
    }

    public LiveData<List<CourseMentor>> getAllCourseMentors(){
        return allCourseMentors;
    }

    public List<CourseMentor> getCourseMentorsForCourse(int courseId){
        try {
            List<CourseMentor> courseMentorsForCourse = new GetCourseMentorsForCourseAsyncTask(courseMentorDao).execute(courseId).get();
            return courseMentorsForCourse;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    private static class InsertCourseMentorAsyncTask extends AsyncTask<CourseMentor, Void, Void> {
        private CourseMentorDao courseMentorDao;

        private InsertCourseMentorAsyncTask(CourseMentorDao courseMentorDao){
            this.courseMentorDao = courseMentorDao;
        }

        @Override
        protected Void doInBackground(CourseMentor... courseMentors) {
            courseMentorDao.insertCourseMentor(courseMentors[0]);
            return null;
        }
    }

    private static class UpdateCourseMentorAsyncTask extends AsyncTask<CourseMentor, Void, Void> {
        private CourseMentorDao courseMentorDao;

        private UpdateCourseMentorAsyncTask(CourseMentorDao courseMentorDao){
            this.courseMentorDao = courseMentorDao;
        }

        @Override
        protected Void doInBackground(CourseMentor... courseMentors) {
            courseMentorDao.updateCourseMentor(courseMentors[0]);
            return null;
        }
    }

    private static class DeleteCourseMentorAsyncTask extends AsyncTask<CourseMentor, Void, Void> {
        private CourseMentorDao courseMentorDao;

        private DeleteCourseMentorAsyncTask(CourseMentorDao courseMentorDao){
            this.courseMentorDao = courseMentorDao;
        }

        @Override
        protected Void doInBackground(CourseMentor... courseMentors) {
            courseMentorDao.deleteCourseMentor(courseMentors[0]);
            return null;
        }
    }

    private static class GetCourseMentorsForCourseAsyncTask extends AsyncTask<Integer, Void, List<CourseMentor>>{
        private CourseMentorDao courseMentorDao;

        private GetCourseMentorsForCourseAsyncTask(CourseMentorDao courseMentorDao){
            this.courseMentorDao = courseMentorDao;
        }

        @Override
        protected List<CourseMentor> doInBackground(Integer... integers) {
            List<CourseMentor> courseMentorsForCourse = courseMentorDao.getCourseMentorsByCourseId(integers[0]);
            return courseMentorsForCourse;
        }
    }
}
