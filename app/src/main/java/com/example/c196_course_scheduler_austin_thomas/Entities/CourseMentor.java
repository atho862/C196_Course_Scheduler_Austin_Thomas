package com.example.c196_course_scheduler_austin_thomas.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_mentor_table")
public class CourseMentor {
    @PrimaryKey(autoGenerate = true)
    private int mentorId;
    private int courseId;
    private String mentorName;
    private String mentorPhone;
    private String mentorEmailAddress;

    public CourseMentor(int courseId, String mentorName, String mentorPhone, String mentorEmailAddress) {
        this.courseId = courseId;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmailAddress = mentorEmailAddress;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public String getMentorEmailAddress() {
        return mentorEmailAddress;
    }
}
