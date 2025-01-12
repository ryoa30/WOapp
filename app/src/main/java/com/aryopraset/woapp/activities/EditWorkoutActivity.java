package com.aryopraset.woapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.databinding.ActivityEditWorkoutBinding;

public class EditWorkoutActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DAY_NUMBER = "dayNumber";
    private int userId;
    ActivityEditWorkoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        binding = DataBindingUtil.setContentView(EditWorkoutActivity.this, R.layout.activity_edit_workout);

        userId = getIntent().getIntExtra(MainActivity.USER_ID, 1);

        binding.monBtn.setOnClickListener(this);
        binding.tueBtn.setOnClickListener(this);
        binding.wedBtn.setOnClickListener(this);
        binding.thurBtn.setOnClickListener(this);
        binding.friBtn.setOnClickListener(this);
        binding.satBtn.setOnClickListener(this);
        binding.sunBtn.setOnClickListener(this);

        binding.backButton.setOnClickListener(v->{
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(EditWorkoutActivity.this, EditDayWorkoutActivity.class);
        intent.putExtra(MainActivity.USER_ID, userId);
        if(v.getId() == R.id.monBtn){
            intent.putExtra(DAY_NUMBER, 1);
        }else if(v.getId() == R.id.tueBtn){
            intent.putExtra(DAY_NUMBER, 2);
        }else if(v.getId() == R.id.wedBtn){
            intent.putExtra(DAY_NUMBER, 3);
        }else if(v.getId() == R.id.thurBtn){
            intent.putExtra(DAY_NUMBER, 4);
        }else if(v.getId() == R.id.friBtn){
            intent.putExtra(DAY_NUMBER, 5);
        }else if(v.getId() == R.id.satBtn){
            intent.putExtra(DAY_NUMBER, 6);
        }else if(v.getId() == R.id.sunBtn){
            intent.putExtra(DAY_NUMBER, 7);
        }
        startActivity(intent);
    }
}