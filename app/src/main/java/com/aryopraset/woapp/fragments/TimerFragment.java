package com.aryopraset.woapp.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.activities.StartWorkout;
import com.aryopraset.woapp.databinding.FragmentTimerBinding;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimerFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "minute";
    private static final String ARG_PARAM2 = "second";

    // TODO: Rename and change types of parameters
    private static int minute;
    private static int second;
    private FragmentTimerBinding binding;
    CountDownTimer timer;
    private Context context;
    private long timeLeftInMillis;
    private boolean isPaused = false;
    private DismissListener dismissListener;

    public void setDismissListener(DismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static TimerFragment newInstance(int minutes, int seconds) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, minutes);
        args.putInt(ARG_PARAM2, seconds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle);
        if (getArguments() != null) {
            minute = getArguments().getInt(ARG_PARAM1);
            second = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setDimAmount(0.7f); // Adjust dim amount here (0.0 - 1.0)
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false);
        timeLeftInMillis = (minute * 60 * 1000) + (second * 1000);
        startTime();
        binding.pauseBtn.setOnClickListener(v -> {
            if (!isPaused) {
                timer.cancel(); // Pause the timer
                binding.pauseBtn.setImageResource(R.drawable.baseline_play_circle_24); // Set icon to resume
                isPaused = true;
            } else {
                startTime(); // Resume with remaining time
                binding.pauseBtn.setImageResource(R.drawable.baseline_pause_circle_filled_24); // Set icon back to pause
                isPaused = false;
            }
        });

        binding.stopBtn.setOnClickListener(v->{
            timer.cancel();
            dismiss();
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void startTime() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;

                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                String minFormat = String.format(Locale.getDefault(), "%02d", min);
                String secFormat = String.format(Locale.getDefault(), "%02d", sec);

                binding.minuteTv.setText(minFormat);
                binding.secondTv.setText(secFormat);
            }

            @Override
            public void onFinish() {
                dismissListener.onDismiss();
                dismiss();
            }
        }.start();

    }

    public interface DismissListener{
        void onDismiss();
    }
}