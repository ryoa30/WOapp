package com.aryopraset.woapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.viewModels.EditExerciseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EditExerciseAdapter extends RecyclerView.Adapter<EditExerciseAdapter.ViewHolder> {
    List<Exercise> exerciseList;
    Context context;
    OnExerciseClickListener onExerciseClickListener;
    EditExerciseViewModel editExerciseViewModel;
    public EditExerciseAdapter(Context context, List<Exercise> exerciseList, OnExerciseClickListener onExerciseClickListener, EditExerciseViewModel editExerciseViewModel) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.onExerciseClickListener = onExerciseClickListener;
        this.editExerciseViewModel = editExerciseViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_edit_day_wo_exercises, parent, false);
        return new ViewHolder(view, onExerciseClickListener);
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
        holder.setRecV.setLayoutManager(new GridLayoutManager(context, 3));
        holder.setRecV.setHasFixedSize(true);

        editExerciseViewModel.getSets(exercise.getId()).observe((LifecycleOwner) context, sets -> {
            if(!sets.isEmpty()){
                holder.setRecV.setAdapter(new EditSetAdapter(sets));
//                for(Set element:sets){
//                    Log.d("TAGGING", "onBindViewHolder: " + exercise.getExercise_name() + " " + element.getSet_number() + " " + element.getWeight());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView exerciseName;
        TextView exerciseSets;
        TextView exerciseReps;
        TextView exerciseRest;
        FloatingActionButton editFab;
        RecyclerView setRecV;
        ImageButton buttonUp;
        ImageButton buttonDown;
        public ViewHolder(@NonNull View itemView, OnExerciseClickListener onExerciseClickListener) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exNameTv);
            exerciseReps = itemView.findViewById(R.id.repsTv);
            exerciseSets = itemView.findViewById(R.id.setTv);
            exerciseRest = itemView.findViewById(R.id.restTv);
            editFab = itemView.findViewById(R.id.editExerciseFab);
            setRecV = itemView.findViewById(R.id.sets_recV);
            buttonDown = itemView.findViewById(R.id.downOrderBtn);
            buttonUp = itemView.findViewById(R.id.upOrderBtn);

            editFab.setOnClickListener(v -> onExerciseClickListener.onEditButtonClick(getBindingAdapterPosition()));
            buttonUp.setOnClickListener(v -> onExerciseClickListener.onButtonUpClick(getBindingAdapterPosition()));
            buttonDown.setOnClickListener(v -> onExerciseClickListener.onButtonDownClick(getBindingAdapterPosition()));
        }
    }

    public interface OnExerciseClickListener {
        void onButtonUpClick(int position);
        void onButtonDownClick(int position);
        void onEditButtonClick(int position);
    }

}
