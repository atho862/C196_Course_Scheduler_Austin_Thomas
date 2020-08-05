package com.example.c196_course_scheduler_austin_thomas.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;
import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;
import com.example.c196_course_scheduler_austin_thomas.Repository.AssessmentRepository;

import java.util.ArrayList;
import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public void insertAssessment(Assessment assessment){
        repository.insertAssessment(assessment);
    }

    public void updateAssessment(Assessment assessment){
        repository.updateAssessment(assessment);
    }

    public void deleteAssessment(Assessment assessment){
        repository.deleteAssessment(assessment);
    }

    public List<Assessment> getAssessmentsForCourse(int courseId){
        List<Assessment> assessmentsForCourse = new ArrayList<>();

        for (Assessment assessment : allAssessments.getValue()
             ) {
            if (assessment.getCourseId() == courseId){
                assessmentsForCourse.add(assessment);
            }
        }

        return assessmentsForCourse;
    }

    public LiveData<List<Assessment>> getAllAssessments(){
        return allAssessments;
    }

    public int getAssessmentCountsForCourse(int courseId){
        return repository.getAssessmentCountForCourse(courseId);
    }
}
