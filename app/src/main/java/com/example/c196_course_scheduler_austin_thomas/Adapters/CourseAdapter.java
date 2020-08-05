package com.example.c196_course_scheduler_austin_thomas.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.c196_course_scheduler_austin_thomas.Entities.Course;
import com.example.c196_course_scheduler_austin_thomas.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private List<Course> courses = new ArrayList<Course>();
    private CourseAdapter.OnItemClickListener listener;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseAdapter.CourseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Course currentCourse = courses.get(position);
        holder.textCourseTitle.setText(currentCourse.getCourseTitle());
        holder.textCourseDate.setText(formatter.format(currentCourse.getCourseStartDate()) + "-" + formatter.format(currentCourse.getAnticipatedEndDate()));
        holder.textCourseStatus.setText(currentCourse.getCourseStatus());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<Course> courses){
        this.courses = courses;
        notifyDataSetChanged();
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        private TextView textCourseTitle;
        private TextView textCourseDate;
        private TextView textCourseStatus;


        public CourseHolder(@NonNull View itemView) {
            super(itemView);
            textCourseTitle = itemView.findViewById(R.id.text_course_title);
            textCourseDate = itemView.findViewById(R.id.text_course_date);
            textCourseStatus = itemView.findViewById(R.id.text_course_progress);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClicked(courses.get(position));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClicked(Course course);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
