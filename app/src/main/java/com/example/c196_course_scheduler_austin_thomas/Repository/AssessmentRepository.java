package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Daos.AssessmentDao;
import com.example.c196_course_scheduler_austin_thomas.Database.CourseSchedulerDatabase;
import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;

import java.util.List;

public class AssessmentRepository {
    private AssessmentDao assessmentDao;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentRepository(Application application){
        CourseSchedulerDatabase database = CourseSchedulerDatabase.getInstance(application);
        assessmentDao = database.assessmentDao();
        allAssessments = assessmentDao.getAllAssessments();
    }

    public void insertAssessment(Assessment assessment){
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void updateAssessment(Assessment assessment){
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public void deleteAssessment(Assessment assessment){
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    public LiveData<List<Assessment>> getAllAssessments(){
        return allAssessments;
    }

    public List<Assessment> getAssessmentsForCourse(int courseId){
        try {
            return new GetAssessmentsForCourseAsyncTask(assessmentDao).execute(courseId).get();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public int getAssessmentCountForCourse(int courseId){
        try {
            return new GetAssessmentCountForCourseAsyncTask(assessmentDao).execute(courseId).get();
        }
        catch (Exception e){
            System.out.println(e);
            return 0;
        }

    }

    private static class InsertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private InsertAssessmentAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.insertAssessment(assessments[0]);
            return null;
        }
    }

    private static class UpdateAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private UpdateAssessmentAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.updateAssessment(assessments[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao assessmentDao;

        private DeleteAssessmentAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }


        @Override
        protected Void doInBackground(Assessment... assessments) {
            assessmentDao.deleteAssessment(assessments[0]);
            return null;
        }
    }

    private static class GetAssessmentsForCourseAsyncTask extends AsyncTask<Integer, List<Assessment>, List<Assessment>> {
        private AssessmentDao assessmentDao;

        private GetAssessmentsForCourseAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected List<Assessment> doInBackground(Integer ...integers) {
            List<Assessment> assessmentsForCourse = assessmentDao.getAssessmentsForCourseId(integers[0]);
            return assessmentsForCourse;
        }
    }

    private static class GetAssessmentCountForCourseAsyncTask extends AsyncTask<Integer, Void, Integer> {
        private AssessmentDao assessmentDao;

        private GetAssessmentCountForCourseAsyncTask(AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return assessmentDao.getAssessmentCountForCourse(integers[0]);
        }
    }

}
