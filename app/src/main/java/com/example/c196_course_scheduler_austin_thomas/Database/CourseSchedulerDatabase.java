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

import com.example.c196_course_scheduler_austin_thomas.Daos.AssessmentDao;
import com.example.c196_course_scheduler_austin_thomas.Daos.CourseDao;
import com.example.c196_course_scheduler_austin_thomas.Daos.CourseMentorDao;
import com.example.c196_course_scheduler_austin_thomas.Daos.NoteDao;
import com.example.c196_course_scheduler_austin_thomas.Daos.TermDao;
import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;
import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;
import com.example.c196_course_scheduler_austin_thomas.Entities.Note;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.TypeConverters.Converters;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Database(entities = {Term.class, Course.class, Assessment.class, CourseMentor.class, Note.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class CourseSchedulerDatabase extends RoomDatabase {
    private static CourseSchedulerDatabase instance;

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract NoteDao noteDao();
    public abstract AssessmentDao assessmentDao();
    public abstract CourseMentorDao courseMentorDao();

    public static synchronized CourseSchedulerDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CourseSchedulerDatabase.class, "coursescheduler_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private CourseDao courseDao;
        private NoteDao noteDao;
        private CourseMentorDao courseMentorDao;
        private AssessmentDao assessmentDao;

        private PopulateDbAsyncTask(CourseSchedulerDatabase database){
            this.termDao = database.termDao();
            this.courseDao = database.courseDao();
            this.noteDao = database.noteDao();
            this.courseMentorDao = database.courseMentorDao();
            this.assessmentDao = database.assessmentDao();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            termDao.insert(new Term("Fall 2020", new GregorianCalendar(2020, Calendar.SEPTEMBER, 1).getTime(), new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime()));
            termDao.insert(new Term("Spring 2021", new GregorianCalendar(2021, Calendar.MARCH, 1).getTime(), new GregorianCalendar(2021, Calendar.AUGUST, 31).getTime()));
            courseDao.insertCourse(new Course(1, "Operating Systems for Programmers", "Plan To Take", new GregorianCalendar(2020, Calendar.SEPTEMBER, 1).getTime(), new GregorianCalendar(2020, Calendar.OCTOBER, 1).getTime()));
            courseDao.insertCourse(new Course(1, "Advanced Java Concepts", "Plan To Take", new GregorianCalendar(2020, Calendar.OCTOBER, 1).getTime(), new GregorianCalendar(2020, Calendar.NOVEMBER, 1).getTime()));
            courseDao.insertCourse(new Course(2, "Mobile Application Development", "Plan To Take", new GregorianCalendar(2021, Calendar.MARCH, 1).getTime(), new GregorianCalendar(2021, Calendar.MARCH, 31).getTime()));
            courseMentorDao.insertCourseMentor(new CourseMentor(2, "Malcolm Wabara", "555-12345", "malcolm.wabara@wgu.edu"));
            courseMentorDao.insertCourseMentor(new CourseMentor(3, "Carolyn Sher-DeCusatis", "555-1234", "carolyn.sher-decusatis@wgu.edu"));
            noteDao.insertNote(new Note(1, "Make sure to use the abridged textbook when studying for this course"));
            noteDao.insertNote(new Note(1, "Plan on using the Barbara Hecker lecture series"));
            noteDao.insertNote(new Note(3, "Make sure you read the rubric carefully for this class!"));
            assessmentDao.insertAssessment(new Assessment(1, "Operating Systems for Programmers - Objective Assessment", new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime(), "Objective Assessment", new GregorianCalendar(2020, Calendar.SEPTEMBER, 30).getTime()));
            assessmentDao.insertAssessment(new Assessment(2, "Appointment Scheduler", new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime(), "Performance Assessment", new GregorianCalendar(2020, Calendar.OCTOBER, 31).getTime()));
            return null;
        }
    }
}
