package com.aryopraset.woapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.activities.MainActivity;
import com.aryopraset.woapp.models.Workout;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {
    private Context context;
    private List<Workout> workouts;
    private OnWorkoutClickListener onWorkoutClickListener;

    public WorkoutAdapter(Context context, OnWorkoutClickListener onWorkoutClickListener, List<Workout> workouts) {
        this.context = context;
        this.onWorkoutClickListener = onWorkoutClickListener;
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_workout, parent, false);
        return new ViewHolder(view, onWorkoutClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ViewHolder holder, int position) {
//        for(Workout workout: workouts){
//            Log.d("TAGGING", "onCreate: " + workout.getWorkout_name() + " : " + workout.getDay_of_week());
//        }
        Workout workout = workouts.get(position);
        Log.d("TAGGING", "onBindViewHolder: " + workout.getWorkout_name() + position);
        holder.woNameTv.setText(workout.getWorkout_name());
        if(workout.getDay_of_week() == -1){
            holder.dayTv.setText("Day: Not chosen");
            holder.changeBtn.setVisibility(View.GONE);
            holder.chooseBtn.setVisibility(View.VISIBLE);
        }else{
            holder.dayTv.setText(String.format("Day: %s", MainActivity.dayNames[workout.getDay_of_week()-1]));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("TAGGING", "WorkoutAdapter: " + workouts.size());
        return workouts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView woNameTv;
        TextView dayTv;
        Button changeBtn;
        Button chooseBtn;
        public ViewHolder(@NonNull View itemView, OnWorkoutClickListener onWorkoutClickListener) {
            super(itemView);
            woNameTv = itemView.findViewById(R.id.woNameTv);
            dayTv = itemView.findViewById(R.id.dayTv);
            changeBtn = itemView.findViewById(R.id.changeBtn);
            chooseBtn = itemView.findViewById(R.id.chooseBtn);

            changeBtn.setOnClickListener(v-> onWorkoutClickListener.onChangeWoClick(getBindingAdapterPosition()));

            chooseBtn.setOnClickListener(v-> onWorkoutClickListener.onChooseWoClick(getBindingAdapterPosition()));
        }
    }

    public interface OnWorkoutClickListener{
        void onChangeWoClick(int position);
        void onChooseWoClick(int position);
    }
}
