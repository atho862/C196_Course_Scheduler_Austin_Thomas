package com.example.c196_course_scheduler_austin_thomas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.c196_course_scheduler_austin_thomas.Entities.Assessment;
import com.example.c196_course_scheduler_austin_thomas.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    private List<Assessment> assessments = new ArrayList<>();
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    private OnItemClickListener listener;

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        Assessment assessment = assessments.get(position);
        holder.assessmentTitle.setText(assessment.getAssessmentTitle());
        holder.assessmentDueDate.setText(formatter.format(assessment.getDueDate()));
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public void setAssessments(List<Assessment> assessments){
        this.assessments = assessments;
        notifyDataSetChanged();
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder{
        private TextView assessmentTitle;
        private TextView assessmentDueDate;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            assessmentTitle = itemView.findViewById(R.id.text_assessment_title);
            assessmentDueDate = itemView.findViewById(R.id.text_assessment_due_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Assessment assessment);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
