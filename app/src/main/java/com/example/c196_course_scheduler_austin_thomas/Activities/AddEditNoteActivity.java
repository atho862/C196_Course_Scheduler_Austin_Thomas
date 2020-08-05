package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.NoteViewModel;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_NOTE_ID";
    public static final String EXTRA_COURSE_ID =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_COURSE_ID";
    public static final String EXTRA_NOTE_TEXT =
            "com.example.c196_course_scheduler_austin_thomas.Activities.EXTRA_NOTE TEXT";
    private EditText editTextNote;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        courseId = getIntent().getIntExtra(EXTRA_COURSE_ID, 0);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        editTextNote = findViewById(R.id.edit_text_note);

        if (getIntent().getIntExtra(EXTRA_NOTE_ID, 0) != 0){
            setTitle("Edit Note");
            editTextNote.setText(getIntent().getStringExtra(EXTRA_NOTE_TEXT));
        }
        else {
            setTitle("Add Note");
        }
    }

    public void saveNote(){
        Intent intent = getIntent();
        String noteText = editTextNote.getText().toString();
        int courseId = intent.getIntExtra(EXTRA_COURSE_ID, 0);

        if (noteText.trim().isEmpty()){
            Toast.makeText(this, "Please enter at least one character in your note", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NOTE_TEXT, noteText);
        data.putExtra(EXTRA_COURSE_ID, courseId);

        if (intent.getIntExtra(EXTRA_NOTE_ID, 0) != 0){
            data.putExtra(EXTRA_NOTE_ID, intent.getIntExtra(EXTRA_NOTE_ID, 0));
        }

        setResult(RESULT_OK, data);
        finish();
    }

    public void shareNote(){
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_TITLE, "Share Notes");
        intent.putExtra(Intent.EXTRA_TEXT, editTextNote.getText().toString());
        intent.setType("text/plain");

        Intent shareIntent = intent.createChooser(intent, null);
        startActivity(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.save_note_menu_item:
                saveNote();
                return true;
            case R.id.add_note_share_note:
                shareNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}