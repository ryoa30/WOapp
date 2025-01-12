package com.aryopraset.woapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.adapters.HomeExerciseAdapter;
import com.aryopraset.woapp.databinding.ActivityMainBinding;
import com.aryopraset.woapp.models.User;
import com.aryopraset.woapp.utils.LiveDataUtils;
import com.aryopraset.woapp.utils.WOAppRoomDatabase;
import com.aryopraset.woapp.viewModels.HomepageViewModel;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String USER_ID = "userID";
    public static final String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private ActivityMainBinding binding;
    private HomepageViewModel homepageViewModel;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        int dayNumber = (calendar.get(Calendar.DAY_OF_WEEK)==1)?8:calendar.get(Calendar.DAY_OF_WEEK);
        int userId = 1;
        WOAppRoomDatabase db = WOAppRoomDatabase.getInstance(this);

        homepageViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(HomepageViewModel.class);

        binding.dayTv.setText(dayNames[dayNumber-2]);
        homepageViewModel.getUser(userId).observe(this, user -> binding.greetTv.setText(String.format("Hi, %s", user.getName())));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);

        homepageViewModel.getDayWorkout(userId, dayNumber-1).observe(this, workout ->{
            if(workout == null){
                binding.noWoTv.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }else{
                binding.dayTv.setText(dayNames[dayNumber-2] + " | " + workout.getWorkout_name());
                binding.noWoTv.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                homepageViewModel.getAllData(workout.getId()).observe(MainActivity.this, exercises -> {
                    if (exercises.isEmpty()) {
                        Toast.makeText(this, "There is no workouts yet", Toast.LENGTH_LONG).show();
                    } else {
                        binding.recyclerView.setAdapter(new HomeExerciseAdapter(MainActivity.this, exercises));
                    }
                });
            }
        });

        binding.profileBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, EditProfile.class);
            intent.putExtra(USER_ID, userId);
            startActivity(intent);
        });

        binding.editFab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditWorkoutActivity.class);
            intent.putExtra(USER_ID, userId);
            startActivity(intent);
        });

        binding.startBtn.setOnClickListener(v->{
            LiveDataUtils.observeOnce(homepageViewModel.getDayWorkout(userId, dayNumber-1), this, workout->{
                if(workout == null){
                    Toast.makeText(this, "No workout to start", Toast.LENGTH_LONG).show();
                }else{
                    LiveDataUtils.observeOnce(homepageViewModel.getAllData(workout.getId()), this, exercises -> {
                        if(exercises.isEmpty()){
                            Toast.makeText(this, "No Exercises to start", Toast.LENGTH_LONG).show();
                        }else{
                            Intent intent = new Intent(MainActivity.this, StartWorkout.class);
                            intent.putExtra("workoutId", workout.getId());
                            startActivity(intent);
                        }
                    });
                }
            });
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

    }
}