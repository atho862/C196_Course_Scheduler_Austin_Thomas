package com.example.c196_course_scheduler_austin_thomas.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.c196_course_scheduler_austin_thomas.Adapters.TermAdapter;
import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.R;
import com.example.c196_course_scheduler_austin_thomas.ViewModels.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class TermListActivity extends AppCompatActivity {
    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;
    private TermViewModel termViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddTerm = findViewById(R.id.button_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermListActivity.this, AddEditTermActivity.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                adapter.setTerms(terms);
            }
        });

        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Term term) {
                Intent intent = new Intent(TermListActivity.this, AddEditTermActivity.class);
                intent.putExtra(AddEditTermActivity.EXTRA_ID, term.getTermId());
                intent.putExtra(AddEditTermActivity.EXTRA_TITLE, term.getTermTitle());
                intent.putExtra(AddEditTermActivity.EXTRA_START_DATE, term.getTermStartDate().getTime());
                intent.putExtra(AddEditTermActivity.EXTRA_END_DATE, term.getTermEndDate().getTime());

                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK){
            String termTitle = data.getStringExtra(AddEditTermActivity.EXTRA_TITLE);
            Date termStartDate = new Date(data.getLongExtra(AddEditTermActivity.EXTRA_START_DATE, 0));
            Date termEndDate = new Date(data.getLongExtra(AddEditTermActivity.EXTRA_END_DATE, 0));

            Term term = new Term(termTitle, termStartDate, termEndDate);
            termViewModel.insertTerm(term);

            Toast.makeText(this, String.format("%s term saved successfully!", termTitle), Toast.LENGTH_SHORT).show();
        }
        else if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditTermActivity.EXTRA_ID, -1);

            if (id == -1){
                Toast.makeText(this, "Term can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String termTitle = data.getStringExtra(AddEditTermActivity.EXTRA_TITLE);
            Date termStartDate = new Date(data.getLongExtra(AddEditTermActivity.EXTRA_START_DATE, 0));
            Date termEndDate = new Date(data.getLongExtra(AddEditTermActivity.EXTRA_END_DATE, 0));
            Term term = new Term(termTitle, termStartDate, termEndDate);
            term.setTermId(id);
            termViewModel.updateTerm(term);

            Toast.makeText(this, String.format("%s term successfully updated!", termTitle), Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }
}