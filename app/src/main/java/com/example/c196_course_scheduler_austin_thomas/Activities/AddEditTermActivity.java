package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.PrimaryKey;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.R;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class AddEditTermActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_END_DATE";
    public static final String EXTRA_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_ID";

    private EditText editTextTermTitle;
    private DatePicker datePickerTermStartDate;
    private DatePicker datePickerTermEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        editTextTermTitle = findViewById(R.id.edit_text_title);
        datePickerTermStartDate = findViewById(R.id.start_date_picker);
        datePickerTermEndDate = findViewById(R.id.end_date_picker);

        datePickerTermStartDate.setMinDate(new Date().getTime());
        datePickerTermEndDate.setMinDate(new Date().getTime());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Term");

            Date startDate = new Date(intent.getLongExtra(EXTRA_START_DATE, 0));
            Date endDate = new Date(intent.getLongExtra(EXTRA_END_DATE, 0));
            Calendar startDateCalendar = Calendar.getInstance();
            Calendar endDateCalendar = Calendar.getInstance();
            startDateCalendar.setTime(startDate);
            endDateCalendar.setTime(endDate);

            editTextTermTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            datePickerTermStartDate.updateDate(startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DATE));
            datePickerTermEndDate.updateDate(endDateCalendar.get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DATE));
        }
        else {
            setTitle("Add Term");
        }
    }

    private void saveTerm(){
        Calendar calendarForStartDate = Calendar.getInstance();
        Calendar calendarForEndDate = Calendar.getInstance();
        calendarForStartDate.set(datePickerTermStartDate.getYear(), datePickerTermStartDate.getMonth(), datePickerTermStartDate.getDayOfMonth());

        String termTitle = editTextTermTitle.getText().toString();
        Date termStartDate = calendarForStartDate.getTime();

        calendarForEndDate.set(datePickerTermEndDate.getYear(), datePickerTermEndDate.getMonth(), datePickerTermEndDate.getDayOfMonth());
        Date termEndDate = calendarForEndDate.getTime();

        if (termTitle.trim().isEmpty()){
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (termStartDate.after(termEndDate)) {
            Toast.makeText(this, "Your term start date must be before your term end date", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, termTitle);
        data.putExtra(EXTRA_START_DATE, termStartDate.getTime());
        data.putExtra(EXTRA_END_DATE, termEndDate.getTime());

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_term_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_term:
                saveTerm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}