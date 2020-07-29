package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Daos.TermDao;
import com.example.c196_course_scheduler_austin_thomas.Database.CourseSchedulerDatabase;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;

import java.util.List;

public class TermRepository {
    private TermDao termDao;
    private LiveData<List<Term>> allTerms;

    public TermRepository(Application application) {
        CourseSchedulerDatabase database = CourseSchedulerDatabase.getInstance(application);
        termDao = database.termDao();
        allTerms = termDao.getAllTerms();
    }

    public void insert(Term term){
        new InsertTermAsyncTask(termDao).execute(term);
    }

    public void update(Term term){
        new UpdateTermAsyncTask(termDao).execute(term);
    }

    public void delete(Term term){

    }

    public void deleteAllNotes(){

    }

    public LiveData<List<Term>> getAllTerms(){
        return allTerms;
    }

    private static class InsertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao termDao;

        private InsertTermAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.insert(terms[0]);
            return null;
        }
    }

    private static class UpdateTermAsyncTask extends AsyncTask<Term, Void, Void>{
        private TermDao termDao;

        private UpdateTermAsyncTask(TermDao termDao) {
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.update(terms[0]);
            return null;
        }
    }
}
