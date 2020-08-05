package com.example.c196_course_scheduler_austin_thomas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196_course_scheduler_austin_thomas.Entities.CourseMentor;
import com.example.c196_course_scheduler_austin_thomas.R;

import java.util.ArrayList;
import java.util.List;

public class CourseMentorAdapter extends RecyclerView.Adapter<CourseMentorAdapter.CourseMentorHolder> {
    private List<CourseMentor> courseMentors = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public CourseMentorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_mentor_item, parent, false);
        return new CourseMentorAdapter.CourseMentorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseMentorHolder holder, int position) {
        CourseMentor courseMentor = courseMentors.get(position);
        holder.courseMentorName.setText(courseMentor.getMentorName());
    }

    @Override
    public int getItemCount() {
        return courseMentors.size();
    }

    public void setCourseMentors(List<CourseMentor> courseMentors){
        this.courseMentors = courseMentors;
        notifyDataSetChanged();
    }

    class CourseMentorHolder extends RecyclerView.ViewHolder {
        private TextView courseMentorName;

        public CourseMentorHolder(@NonNull View itemView) {
            super(itemView);
            courseMentorName = itemView.findViewById(R.id.text_course_mentor_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(courseMentors.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CourseMentor courseMentor);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
