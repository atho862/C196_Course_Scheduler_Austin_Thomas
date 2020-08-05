package com.example.c196_course_scheduler_austin_thomas.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import com.example.c196_course_scheduler_austin_thomas.Daos.NoteDao;
import com.example.c196_course_scheduler_austin_thomas.Database.CourseSchedulerDatabase;
import com.example.c196_course_scheduler_austin_thomas.Entities.Note;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        CourseSchedulerDatabase database = CourseSchedulerDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insertNote(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void updateNote(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteNote(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }

    public LiveData<List<Note>> getNotesForCourse(int courseId) {
        try {
            return new GetNotesForCourseAsyncTask(noteDao).execute(courseId).get();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insertNote(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.updateNote(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteNote(notes[0]);
            return null;
        }
    }

    private static class GetNotesForCourseAsyncTask extends AsyncTask<Integer, Void, LiveData<List<Note>>> {
        private NoteDao noteDao;

        private GetNotesForCourseAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected LiveData<List<Note>> doInBackground(Integer... integers) {
            LiveData<List<Note>> notesforCourse = noteDao.getNotesForCourse(integers[0]);
            return notesforCourse;
        }
    }

}
