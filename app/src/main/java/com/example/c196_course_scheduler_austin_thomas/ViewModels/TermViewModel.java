package com.example.c196_course_scheduler_austin_thomas.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.Repository.TermRepository;

import java.util.List;

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
}
