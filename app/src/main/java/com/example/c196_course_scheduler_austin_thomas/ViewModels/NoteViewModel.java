package com.example.c196_course_scheduler_austin_thomas.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196_course_scheduler_austin_thomas.Entities.Note;
import com.example.c196_course_scheduler_austin_thomas.Repository.NoteRepository;

import java.util.ArrayList;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insertNote(Note note){
        repository.insertNote(note);
    }

    public void updateNote(Note note){
        repository.updateNote(note);
    }

    public void deleteNote(Note note){
        repository.deleteNote(note);
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public List<Note> getNotesForCourse(int courseId){
        List<Note> notesForCourse = new ArrayList<>();
        for (Note note : allNotes.getValue()
             ) {
            if (note.getCourseId() == courseId){
                notesForCourse.add(note);
            }
        }

        return notesForCourse;
    }
}
