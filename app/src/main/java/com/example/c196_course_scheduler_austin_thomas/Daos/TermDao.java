package com.example.c196_course_scheduler_austin_thomas.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.c196_course_scheduler_austin_thomas.Entities.Term;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TermDao {
    @Insert
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("DELETE FROM term_table")
    void deleteAllTerms();

    @Query("SELECT * FROM term_table ORDER BY termStartDate ASC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT termTitle FROM term_table ORDER BY termStartDate ASC")
    List<String> getAllTermTitles();

    @Query("SELECT termId FROM term_table WHERE termTitle = :termTitle")
    int getTermIdByTermTitle(String termTitle);
}
