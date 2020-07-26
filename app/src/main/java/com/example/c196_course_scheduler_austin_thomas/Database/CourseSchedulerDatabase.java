package com.example.c196_course_scheduler_austin_thomas.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.c196_course_scheduler_austin_thomas.Daos.TermDao;
import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;
import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.TypeConverters.Converters;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Database(entities = {Term.class, Course.class, Assessment.class, CourseMentor.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CourseSchedulerDatabase extends RoomDatabase {
    private static CourseSchedulerDatabase instance;

    public abstract TermDao termDao();

    public static synchronized CourseSchedulerDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CourseSchedulerDatabase.class, "coursescheduler_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallbakc)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallbakc = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;

        private PopulateDbAsyncTask(CourseSchedulerDatabase database){
            this.termDao = database.termDao();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            termDao.insert(new Term("Fall 2020", new GregorianCalendar(2020, Calendar.SEPTEMBER, 1).getTime(), new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime()));
            termDao.insert(new Term("Spring 2021", new GregorianCalendar(2021, Calendar.MARCH, 1).getTime(), new GregorianCalendar(2021, Calendar.AUGUST, 31).getTime()));
            return null;
        }
    }
}
