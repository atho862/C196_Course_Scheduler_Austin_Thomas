package com.example.c196_course_scheduler_austin_thomas.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = "term_table")
public class Term {
    @PrimaryKey(autoGenerate = true)
    private int termId;
    private String termTitle;
    private Date termStartDate;
    private Date termEndDate;

    public Term(String termTitle, Date termStartDate, Date termEndDate) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public int getTermId() {
        return termId;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public Date getTermStartDate() {
        return termStartDate;
    }

    public Date getTermEndDate() {
        return termEndDate;
    }
}
