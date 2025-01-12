package com.aryopraset.woapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.adapters.EditExerciseAdapter;
import com.aryopraset.woapp.databinding.ActivityEditDayWorkoutBinding;
import com.aryopraset.woapp.fragments.EditExerciseFragment;
import com.aryopraset.woapp.models.Workout;
import com.aryopraset.woapp.utils.LiveDataUtils;
import com.aryopraset.woapp.viewModels.EditExerciseViewModel;

public class EditDayWorkoutActivity extends AppCompatActivity implements EditExerciseAdapter.OnExerciseClickListener {
    private int userId;
    private int dayNumber;
    private EditExerciseViewModel editExerciseViewModel;

    ActivityEditDayWorkoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_day_workout);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_day_workout);

        userId = getIntent().getIntExtra(MainActivity.USER_ID, 1);
        dayNumber = getIntent().getIntExtra(EditWorkoutActivity.DAY_NUMBER, 0);
        editExerciseViewModel = new ViewModelProvider.AndroidViewModelFactory(EditDayWorkoutActivity.this.getApplication()).create(EditExerciseViewModel.class);
        binding.exerciseRecV.setHasFixedSize(true);
        binding.exerciseRecV.setLayoutManager(new LinearLayoutManager(this));

        binding.greetTv.setText("Edit " + MainActivity.dayNames[dayNumber-1]);

        editExerciseViewModel.getDayWorkout(userId, dayNumber).observe(this, workout -> {
            if(workout!=null){
                binding.woNameTv.setText(workout.getWorkout_name());
                editExerciseViewModel.getAllData(workout.getId()).observe(this, exercises -> {
                    if(!exercises.isEmpty()){
                        binding.exerciseRecV.setAdapter(new EditExerciseAdapter(EditDayWorkoutActivity.this, exercises, this, editExerciseViewModel));
                    }else{
                        binding.exerciseRecV.setAdapter(null);
                    }
                });
            }else{
                binding.woNameTv.setText("No workout");
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            LiveDataUtils.observeOnce(editExerciseViewModel.getDayWorkout(userId, dayNumber), this, workout -> {
                if(workout!=null){
                    EditExerciseFragment editExerciseFragment = EditExerciseFragment.newInstance(workout.getId(), -1, "add");
                    editExerciseFragment.show(getSupportFragmentManager(), null);
                }else{
                    Toast.makeText(this, "No Workout, Make or Choose a workout", Toast.LENGTH_LONG).show();
                }
            });
        });

        binding.changeWoBtn.setOnClickListener(v->{
            Intent intent = new Intent(EditDayWorkoutActivity.this, WorkoutListActivity.class);
            intent.putExtra(MainActivity.USER_ID, userId);
            intent.putExtra(EditWorkoutActivity.DAY_NUMBER, dayNumber);
            startActivity(intent);
        });

        binding.backButton.setOnClickListener(v->{
            finish();
        });

    }

    @Override
    public void onButtonUpClick(int position) {
        LiveDataUtils.observeOnce(editExerciseViewModel.getDayWorkout(userId, dayNumber), this, workout -> {
            if(workout!=null){
                LiveDataUtils.observeOnce(editExerciseViewModel.getAllData(workout.getId()), this, exercises -> {
                    if (exercises != null && !exercises.isEmpty()) {
                        editExerciseViewModel.swapAndUpdateExercises(exercises, position, position-1);
                    }
                });
            }
        });
    }

    @Override
    public void onButtonDownClick(int position) {
        LiveDataUtils.observeOnce(editExerciseViewModel.getDayWorkout(userId, dayNumber), this, workout -> {
            if(workout!=null){
                LiveDataUtils.observeOnce(editExerciseViewModel.getAllData(workout.getId()), this, exercises -> {
                    if (exercises != null && !exercises.isEmpty()) {
                        editExerciseViewModel.swapAndUpdateExercises(exercises, position, position+1);
                    }
                });
            }
        });

    }

    @Override
    public void onEditButtonClick(int position) {
        LiveDataUtils.observeOnce(editExerciseViewModel.getDayWorkout(userId, dayNumber), this, workout -> {
            if(workout!=null){
                LiveDataUtils.observeOnce(editExerciseViewModel.getAllData(workout.getId()), this, exercises -> {
                    if (exercises != null && !exercises.isEmpty()) {
                        EditExerciseFragment editExerciseFragment = EditExerciseFragment.newInstance(workout.getId(), exercises.get(position).getId(), "update");
                        editExerciseFragment.show(getSupportFragmentManager(), null);
                    }
                });
            }
        });
    }
}