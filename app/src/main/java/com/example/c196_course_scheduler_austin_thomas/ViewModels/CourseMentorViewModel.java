package com.example.c196_course_scheduler_austin_thomas.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;
import com.example.c196_course_scheduler_austin_thomas.Repository.CourseMentorRepository;

import java.util.List;

public class CourseMentorViewModel extends AndroidViewModel {
    private CourseMentorRepository repository;
    private LiveData<List<CourseMentor>> allCourseMentors;

    public CourseMentorViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseMentorRepository(application);
        allCourseMentors = repository.getAllCourseMentors();
    }

    public void insertCourseMentor(CourseMentor courseMentor){
        repository.insertCourseMentor(courseMentor);
    }

    public void updateCourseMentor(CourseMentor courseMentor){
        repository.updateCourseMentor(courseMentor);
    }

    public void deleteCourseMentor(CourseMentor courseMentor){
        repository.deleteCourseMentor(courseMentor);
    }

    public LiveData<List<CourseMentor>> getAllCourseMentors(){
        return allCourseMentors;
    }

    public List<CourseMentor> getCourseMentorsForCourse(int courseId){
        return repository.getCourseMentorsForCourse(courseId);
    }
}
