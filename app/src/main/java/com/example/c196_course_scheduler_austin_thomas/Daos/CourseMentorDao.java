package com.example.c196_course_scheduler_austin_thomas.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;

import java.util.List;

@Dao
public interface CourseMentorDao {
    @Insert
    void insertCourseMentor(CourseMentor courseMentor);

    @Update
    void updateCourseMentor(CourseMentor courseMentor);

    @Delete
    void deleteCourseMentor(CourseMentor courseMentor);

    @Query("SELECT * FROM course_mentor_table")
    LiveData<List<CourseMentor>> getAllCourseMentors();

    @Query("SELECT * FROM course_mentor_table WHERE courseId = :courseId")
    List<CourseMentor> getCourseMentorsByCourseId(int courseId);
}
