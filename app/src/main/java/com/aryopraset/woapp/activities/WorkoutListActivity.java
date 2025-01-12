package com.aryopraset.woapp.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.adapters.WorkoutAdapter;
import com.aryopraset.woapp.databinding.ActivityWorkoutListBinding;
import com.aryopraset.woapp.models.Workout;
import com.aryopraset.woapp.utils.LiveDataUtils;
import com.aryopraset.woapp.viewModels.WorkoutListViewModel;

import java.util.List;


public class WorkoutListActivity extends AppCompatActivity implements WorkoutAdapter.OnWorkoutClickListener {

    WorkoutListViewModel workoutListViewModel;
    ActivityWorkoutListBinding binding;
    private int userId;
    private int dayNumber;
    Dialog dialog;
    Button addBtn, cancelBtn;
    EditText woNameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_list);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_workout_list);
        userId = getIntent().getIntExtra(MainActivity.USER_ID, 1);
        dayNumber = getIntent().getIntExtra(EditWorkoutActivity.DAY_NUMBER, -1);
        binding.workoutRecV.setLayoutManager(new LinearLayoutManager(this));
        binding.workoutRecV.setHasFixedSize(true);

        dialog = new Dialog(WorkoutListActivity.this);
        dialog.setContentView(R.layout.dialog_add_workout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.setCancelable(false);

        addBtn = dialog.findViewById(R.id.addBtn);
        cancelBtn = dialog.findViewById(R.id.cancelBtn);
        woNameEt = dialog.findViewById(R.id.woNameEt);

        cancelBtn.setOnClickListener(v->{
            dialog.dismiss();
        });

        binding.addBtn.setOnClickListener(v->{
            dialog.show();
        });

        addBtn.setOnClickListener(v->{
            String woName = woNameEt.getText().toString();
            if(woName.isEmpty()){
                Toast.makeText(this, "Workout Name Cannot Be Empty", Toast.LENGTH_LONG).show();
            }else{
                LiveDataUtils.observeOnce(workoutListViewModel.getDayWorkout(userId, dayNumber), this, workout->{
                    if(workout!=null){
                        workoutListViewModel.insertWorkout(new Workout(-1, userId, woName));
                        dialog.dismiss();
                    }else{
                        workoutListViewModel.insertWorkout(new Workout(dayNumber, userId, woName));
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        workoutListViewModel = new ViewModelProvider.AndroidViewModelFactory(WorkoutListActivity.this.getApplication()).create(WorkoutListViewModel.class);

        workoutListViewModel.getWorkoutExludeDay(userId, dayNumber).observe(this, workouts -> {
//            for(Workout workout: workouts){
//                Log.d("TAGGING", "onCreate: " + workout.getWorkout_name() + " : " + workout.getDay_of_week());
//            }
            binding.workoutRecV.setAdapter(new WorkoutAdapter(WorkoutListActivity.this, this, workouts));
        });

        binding.backButton.setOnClickListener(v->{
            finish();
        });
    }

    @Override
    public void onChangeWoClick(int position) {
        LiveDataUtils.observeOnce(workoutListViewModel.getDayWorkout(userId, dayNumber), this, workout -> {
            Workout changeWorkout = workoutListViewModel.workouts.getValue().get(position);
            int changeWoDay = changeWorkout.getDay_of_week();
            changeWorkout.setDay_of_week(dayNumber);
            workoutListViewModel.updateWorkout(changeWorkout);
            if(workout!=null){
                workout.setDay_of_week(changeWoDay);
                workoutListViewModel.updateWorkout(workout);
            }
        });
        finish();
    }

    @Override
    public void onChooseWoClick(int position) {
        LiveDataUtils.observeOnce(workoutListViewModel.getDayWorkout(userId, dayNumber), this, workout -> {
            Workout changeWorkout = workoutListViewModel.workouts.getValue().get(position);
            changeWorkout.setDay_of_week(dayNumber);
            workoutListViewModel.updateWorkout(changeWorkout);
            if(workout!=null){
                workout.setDay_of_week(-1);
                workoutListViewModel.updateWorkout(workout);
            }
        });
        finish();
    }
}