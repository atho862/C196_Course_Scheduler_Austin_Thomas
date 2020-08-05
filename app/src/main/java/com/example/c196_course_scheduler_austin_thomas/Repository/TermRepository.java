package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.example.c196_course_scheduler_austin_thomas.Daos.TermDao;
import com.example.c196_course_scheduler_austin_thomas.Database.CourseSchedulerDatabase;
import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    public LiveData<List<Term>> getAllTerms(){
        return allTerms;
    }

    public List<String> getAllTermTitles() {
        try {
            return new GetTermTitlesAsyncTask(termDao).execute().get();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    public int getTermIdByTermTitle(String termTitle){
        try {
            return new GetTermIdByTermTitleAsyncTask(termDao).execute(termTitle).get();
        }
        catch (Exception e) {
            System.out.println(e);
            return 0;
        }
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

    private static class DeleteTermAsyncTask extends AsyncTask<Term, Void, Void>{
        private TermDao termDao;

        private DeleteTermAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Void doInBackground(Term... terms) {
            termDao.delete(terms[0]);
            return null;
        }
    }

    private static class GetTermTitlesAsyncTask extends AsyncTask<Void, Void, List<String>>{
        private TermDao termDao;

        private GetTermTitlesAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> termTitles = termDao.getAllTermTitles();
            return termTitles;
        }
    }

    private static class GetTermIdByTermTitleAsyncTask extends AsyncTask<String, Void, Integer>{
        private TermDao termDao;

        private GetTermIdByTermTitleAsyncTask(TermDao termDao){
            this.termDao = termDao;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return termDao.getTermIdByTermTitle(strings[0]);
        }
    }
}
