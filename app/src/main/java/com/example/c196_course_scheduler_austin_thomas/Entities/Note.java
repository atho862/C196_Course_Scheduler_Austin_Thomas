package com.example.c196_course_scheduler_austin_thomas.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int noteId;
    private int courseId;
    private String note;

    public Note(int courseId, String note) {
        this.courseId = courseId;
        this.note = note;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return noteId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getNote() {
        return note;
    }


}
