package com.aryopraset.woapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.models.Exercise;

import java.util.List;

public class HomeExerciseAdapter extends RecyclerView.Adapter<HomeExerciseAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> exerciseList;

    public HomeExerciseAdapter(Context context, List<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_homepage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        String setString = exercise.getNumber_of_sets()+ " sets";
        String repString = exercise.getTarget_reps_min() + "-" + exercise.getTarget_reps_max() +" reps";
        holder.exerciseName.setText(exercise.getExercise_name());
        holder.exerciseRest.setText(String.format("%02d:%02d",exercise.getRest_time_minutes(), exercise.getRest_time_seconds()));
        holder.exerciseSets.setText(setString);
        holder.exerciseReps.setText(repString);

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseName;
        TextView exerciseSets;
        TextView exerciseReps;
        TextView exerciseRest;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exNameTv);
            exerciseReps = itemView.findViewById(R.id.repsTv);
            exerciseSets = itemView.findViewById(R.id.setTv);
            exerciseRest = itemView.findViewById(R.id.restTv);
        }
    }

}
