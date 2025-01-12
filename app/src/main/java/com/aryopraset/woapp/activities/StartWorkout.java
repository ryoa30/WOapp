package com.aryopraset.woapp.activities;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.databinding.ActivityStartWorkoutBinding;
import com.aryopraset.woapp.fragments.TimerFragment;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.Set;
import com.aryopraset.woapp.utils.LiveDataUtils;
import com.aryopraset.woapp.viewModels.StartWorkoutViewModel;
import com.aryopraset.woapp.viewModels.viewModelFactory.StartWorkoutViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class StartWorkout extends AppCompatActivity implements TimerFragment.DismissListener{

    private int workoutId;
    private int currOrder=0;
    private int lastOrder=0;
    private int setCount = 1;
    private List<Integer> skipQueue = new ArrayList<>();
    private ActivityStartWorkoutBinding binding;
    private StartWorkoutViewModel startWorkoutViewModel;
    Drawable orange;
    Drawable green;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_workout);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        orange = ContextCompat.getDrawable(this, R.drawable.tv_rounded_corners);
        green = ContextCompat.getDrawable(this, R.drawable.timer_background);


        Intent intent = getIntent();
        workoutId = intent.getIntExtra("workoutId", -1);
        if(workoutId == -1) finish();

        StartWorkoutViewModelFactory factory = new StartWorkoutViewModelFactory(getApplication(), workoutId);
        startWorkoutViewModel = new ViewModelProvider(this, factory).get(StartWorkoutViewModel.class);

        LiveDataUtils.observeOnce(startWorkoutViewModel.exercises,this, exercises -> {
            Exercise exercise = exercises.get(currOrder);
            populateData(exercise);
        });

        binding.backButton.setOnClickListener(v->{
            finish();
        });

        binding.editWeightsBtn.setOnClickListener(v -> {
            if(binding.weightsContainer.getChildAt(0).isEnabled()){
                int id = startWorkoutViewModel.exercises.getValue().get(currOrder).getId();
                LiveDataUtils.observeOnce(startWorkoutViewModel.getSets(id), this, sets->{
                    for(int i=0; i<binding.weightsContainer.getChildCount(); i++){
                        EditText weightEt = (EditText) binding.weightsContainer.getChildAt(i);
                        weightEt.setEnabled(false);
                        sets.get(i).setWeight(Double.parseDouble(weightEt.getText().toString().trim()));
                        startWorkoutViewModel.updateSet(sets.get(i));
                    }
                    binding.editWeightsBtn.setText("EDIT");
                });
            }else{
                for(int i=0; i<binding.weightsContainer.getChildCount(); i++){
                    binding.weightsContainer.getChildAt(i).setEnabled(true);
                }
                binding.editWeightsBtn.setText("SAVE");
            }
        });

        binding.skipBtn.setOnClickListener(v->{
            List<Exercise> exercises = startWorkoutViewModel.exercises.getValue();
            setCount=1;
            if(lastOrder+1>=exercises.size()){
                if(skipQueue.isEmpty()){
                    Toast.makeText(this, "Cannot Skip Last Workout", Toast.LENGTH_LONG).show();
                }else{
                    skipQueue.add(currOrder);
                    currOrder = skipQueue.get(0);
                    populateData(exercises.get(currOrder));
                    skipQueue.remove(0);
                }
            }else{
                if(skipQueue.isEmpty()){
                    lastOrder++;
                    currOrder = lastOrder;
                    populateData(exercises.get(currOrder));
                    skipQueue.add(lastOrder-1);
                }else{
                    skipQueue.add(currOrder);
                    currOrder = skipQueue.get(0);
                    populateData(exercises.get(currOrder));
                    skipQueue.remove(0);
                }
            }
        });

        binding.doneBtn.setOnClickListener(v->{
            List<Exercise> exercises = startWorkoutViewModel.exercises.getValue();
            changeExercise(exercises);
        });
    }

    private void changeExercise(List<Exercise> exercises) {
        TimerFragment timerFragment = TimerFragment.newInstance(exercises.get(currOrder).getRest_time_minutes(), exercises.get(currOrder).getRest_time_seconds());
        timerFragment.setDismissListener(this);
        timerFragment.setCancelable(false);
        setCount++;
        if(setCount <= exercises.get(currOrder).getNumber_of_sets()){
            timerFragment.show(getSupportFragmentManager(), null);
            EditText weightEt1 = (EditText) binding.weightsContainer.getChildAt(setCount-1);
            weightEt1.setBackground(green);

            EditText weightEt2 = (EditText) binding.weightsContainer.getChildAt(setCount-2);
            weightEt2.setBackground(orange);
        }
        else if(lastOrder+1>= exercises.size()){
            setCount=1;
            if(skipQueue.isEmpty()){
                Log.d("TAGGING", "onCreate: cannot skip");
                Toast.makeText(this, "Workout Finished", Toast.LENGTH_LONG).show();
                finish();
                return;
            }else{
                timerFragment.show(getSupportFragmentManager(), null);
                currOrder = skipQueue.get(0);
                populateData(exercises.get(currOrder));
                skipQueue.remove(0);
            }
        }else{
            setCount=1;
            timerFragment.show(getSupportFragmentManager(), null);
            if(skipQueue.isEmpty()){
                lastOrder++;
                currOrder = lastOrder;
                populateData(exercises.get(currOrder));
            }else{
                currOrder = skipQueue.get(0);
                populateData(exercises.get(currOrder));
                skipQueue.remove(0);
            }
        }
    }

    private void populateData(Exercise exercise) {
        binding.greetTv.setText("Move " + exercise.getOrder());
        binding.exNameTv.setText(exercise.getExercise_name());
        binding.repsTv.setText(String.format("%d - %d", exercise.getTarget_reps_min(), exercise.getTarget_reps_max()));
        binding.minsTv.setText(String.format("%02d:%02d", exercise.getRest_time_minutes(), exercise.getRest_time_seconds()));

        startWorkoutViewModel.getSets(exercise.getId()).observe(this, sets -> {
            binding.weightsContainer.removeAllViews();
            for(int i = 0 ;i<sets.size(); i++){
                addWeightField(getLayoutInflater(), sets.get(i).getWeight(), i);
            }
        });
    }

    private void addWeightField(LayoutInflater inflater, Double value, int index) {
        EditText weightEditText = (EditText) inflater.inflate(R.layout.start_wo_weight_edit_text, null);
        weightEditText.setText(value.toString());

        if(index==setCount-1){
            weightEditText.setBackground(green);
        }else{
            weightEditText.setBackground(orange);
        }
        weightEditText.setEnabled(false);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
        params.setMargins(15, 15, 15, 15);

        weightEditText.setLayoutParams(params);

        binding.weightsContainer.addView(weightEditText);
    }

    public boolean foregrounded() {
        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel_id = "timer_channel_id";
            CharSequence name = "Timer Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "timer_channel_id")
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Timer Finished")
                .setContentText("Your timer is complete! Time to get back to work.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
    @Override
    public void onDismiss() {
        if(!foregrounded()){
            Log.d("TAGGING", "onFinish: finish and backgrounded");
            createNotificationChannel();
            sendNotification();
        }
    }
}