package com.example.c196_course_scheduler_austin_thomas.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.Repository.TermRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TermViewModel extends AndroidViewModel {
    private TermRepository repository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public void insertTerm(Term term){
        repository.insert(term);
    }

    public void updateTerm(Term term){
        repository.update(term);
    }

    public void deleteTerm(Term term){
        repository.delete(term);
    }

    public List<String> getAllTermTitles() {
        return repository.getAllTermTitles();
    }

    public int getTermIdBytTermTitle(String termTitle){
        return repository.getTermIdByTermTitle(termTitle);
    }
}
