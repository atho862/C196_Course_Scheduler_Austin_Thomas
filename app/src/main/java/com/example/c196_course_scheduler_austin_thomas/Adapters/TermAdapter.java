package com.example.c196_course_scheduler_austin_thomas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196_course_scheduler_austin_thomas.Entities.Term;
import com.example.c196_course_scheduler_austin_thomas.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {
    private List<Term> terms = new ArrayList<>();
    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.term_item, parent, false);
        return new TermHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {
        Term currentTerm = terms.get(position);
        holder.textViewTitle.setText(currentTerm.getTermTitle());
        holder.textViewDate.setText(format.format(currentTerm.getTermStartDate()) + " - " + format.format(currentTerm.getTermEndDate()));

    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
        notifyDataSetChanged();
    }

    class TermHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDate;

        public TermHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_term_title);
            textViewDate = itemView.findViewById(R.id.text_term_date);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(terms.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Term term);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }
}
