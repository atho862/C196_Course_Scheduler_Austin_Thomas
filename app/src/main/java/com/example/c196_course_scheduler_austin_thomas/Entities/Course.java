package com.example.c196_course_scheduler_austin_thomas.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "course_table")
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int courseId;
    private int termId;
    private String courseTitle;
    private String courseStatus;
    private Date courseStartDate;
    private Date anticipatedEndDate;

    public Course(int termId, String courseTitle, String courseStatus, Date courseStartDate, Date anticipatedEndDate) {
        this.termId = termId;
        this.courseTitle = courseTitle;
        this.courseStatus = courseStatus;
        this.courseStartDate = courseStartDate;
        this.anticipatedEndDate = anticipatedEndDate;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getTermId() {
        return termId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public Date getCourseStartDate() {
        return courseStartDate;
    }

    public Date getAnticipatedEndDate() {
        return anticipatedEndDate;
    }
}
