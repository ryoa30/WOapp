package com.aryopraset.woapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.aryopraset.woapp.activities.EditDayWorkoutActivity;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.Set;
import com.aryopraset.woapp.utils.LiveDataUtils;
import com.aryopraset.woapp.viewModels.EditExerciseViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputFilter;
import android.text.Layout;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.databinding.FragmentEditExerciseDialogBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EditExerciseFragment extends BottomSheetDialogFragment {
    private FragmentEditExerciseDialogBinding binding;
    private int workoutId;
    private int exerciseId;
    private ArrayAdapter<CharSequence> adapter;
    private EditExerciseViewModel editExerciseViewModel;

    public static EditExerciseFragment newInstance(int workoutId, int exerciseId, String status) {
        EditExerciseFragment fragment = new EditExerciseFragment();
        Bundle args = new Bundle();
        args.putInt("workoutId", workoutId);
        args.putInt("exerciseId", exerciseId);
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentEditExerciseDialogBinding.inflate(inflater, container, false);
        editExerciseViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(EditExerciseViewModel.class);

        adapter = ArrayAdapter.createFromResource(getContext(), R.array.set_numbers, R.layout.spinner_text_style);

        binding.setSpinner.setAdapter(adapter);

        binding.setSpinner.setText(adapter.getItem(0), false);

        binding.exRestEt.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
        binding.exRestSecEt.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});

        if (getArguments() != null) {
            workoutId = getArguments().getInt("workoutId");
            exerciseId = getArguments().getInt("exerciseId");
            String status = getArguments().getString("status");
            setButtons(status, inflater);
        }

        binding.setSpinner.setOnItemClickListener((parent, view, position, id) -> {
            // Clear existing views in the container
            binding.weightsContainer.removeAllViews();

            // Get the selected number of sets
            String selectedValue = parent.getItemAtPosition(position).toString();
            if (selectedValue.isEmpty()) return;

            int setsCount = Integer.parseInt(selectedValue);

            // Dynamically add EditText fields for each set
            if(exerciseId!=-1){
                LiveDataUtils.observeOnce(editExerciseViewModel.getSets(exerciseId), this, sets->{
                    for (int i = 0; i < setsCount; i++) {
                        if(i<sets.size()){
                            addWeightField(inflater, sets.get(i).getWeight());
                        }else{
                            addWeightField(inflater, i);
                        }
                    }
                });
            }else{
                for (int i = 0; i < setsCount; i++) {
                    addWeightField(inflater, i);
                }
            }
        });

        binding.deleteBtn.setOnClickListener(v ->{
            LiveDataUtils.observeOnce(editExerciseViewModel.getExerciseById(exerciseId), this, exercise -> {
                editExerciseViewModel.deleteExercise(exercise);
            });
            dismiss();
        });

        binding.saveBtn.setOnClickListener(v->{
            String exName = binding.exNameEt.getText().toString().trim();
            String exMinRepsString = binding.exMinRepsEt.getText().toString().trim();
            String exMaxRepsString = binding.exMaxRepsEt.getText().toString().trim();
            String exRestString = binding.exRestEt.getText().toString().trim();
            String exRestSecString = binding.exRestSecEt.getText().toString().trim();
            List<Double> weightValues = new ArrayList<>();
            int exSets = Integer.parseInt(binding.setSpinner.getText().toString());

            if(exName.isEmpty()){
                Toast.makeText(getContext(), "You must fill the exercise name", Toast.LENGTH_LONG).show();
            }else if(exMaxRepsString.isEmpty() || exMinRepsString.isEmpty()){
                Toast.makeText(getContext(), "You must fill the exercise target reps", Toast.LENGTH_LONG).show();
            }else if(exRestString.isEmpty()){
                Toast.makeText(getContext(), "You must fill the exercise rest time", Toast.LENGTH_LONG).show();
            }
            else{
                int exMinReps = Integer.parseInt(exMinRepsString);
                int exMaxReps = Integer.parseInt(exMaxRepsString);
                int exRest = Integer.parseInt(exRestString);
                int exRestSec = (!exRestSecString.isEmpty())?Integer.parseInt(exRestSecString):0;

                for(int i=0;i<exSets;i++){
                    View child = binding.weightsContainer.getChildAt(i);
                    if (child instanceof EditText) {
                        EditText weightEditText = (EditText) child;
                        String weightString = weightEditText.getText().toString().trim();

                        if (weightString.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill all weight fields", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        Double weight = Double.parseDouble(weightString);

                        weightValues.add(weight);
                    }
                }
                LiveDataUtils.observeOnce(editExerciseViewModel.getExerciseById(exerciseId), this, exercise -> {
                    exercise.setExercise_name(exName);
                    exercise.setNumber_of_sets(exSets);
                    exercise.setRest_time_minutes(exRest);
                    exercise.setRest_time_seconds(exRestSec);
                    exercise.setTarget_reps_min(exMinReps);
                    exercise.setTarget_reps_max(exMaxReps);
                    editExerciseViewModel.updateExercise(exercise);

                });

                LiveDataUtils.observeOnce(editExerciseViewModel.getSets(exerciseId), this, sets ->{
                    Log.d("TAGGING", "onCreateView:  MASUK");
                    if(sets.isEmpty()){
                        Log.d("TAGGING", "onCreateView:  MASUK1");
                        for(int i=0; i<exSets; i++){
                            Set set = new Set(exerciseId, i+1, weightValues.get(i));
                            editExerciseViewModel.insertSet(set);
                        }
                    }else if(exSets<sets.size()){
                        for(int i=sets.size()-1; i>=exSets; i--){
                            editExerciseViewModel.deleteSet(sets.get(i));
                        }
                    }else{
                        Log.d("TAGGING", "onCreateView:  MASUK2");
                        for(int i=0; i<exSets; i++){
                            if(i<sets.size()){
                                sets.get(i).setWeight(weightValues.get(i));
                                editExerciseViewModel.updateSet(sets.get(i));
                            }else{
                                Set set = new Set(exerciseId, i+1, weightValues.get(i));
                                editExerciseViewModel.insertSet(set);
                            }
                        }
                    }
                });
                dismiss();
            }
        });

        binding.addBtn.setOnClickListener(v -> {
            String exName = binding.exNameEt.getText().toString().trim();
            String exMinRepsString = binding.exMinRepsEt.getText().toString().trim();
            String exMaxRepsString = binding.exMaxRepsEt.getText().toString().trim();
            String exRestString = binding.exRestEt.getText().toString().trim();
            String exRestSecString = binding.exRestSecEt.getText().toString().trim();
            List<Double> weightValues = new ArrayList<>();
            int exSets = Integer.parseInt(binding.setSpinner.getText().toString());

            if(exName.isEmpty()){
                Toast.makeText(getContext(), "You must fill the exercise name", Toast.LENGTH_LONG).show();
            }else if(exMaxRepsString.isEmpty() || exMinRepsString.isEmpty()){
                Toast.makeText(getContext(), "You must fill the exercise target reps", Toast.LENGTH_LONG).show();
            }else if(exRestString.isEmpty()){
                Toast.makeText(getContext(), "You must fill the exercise rest time", Toast.LENGTH_LONG).show();
            }
            else{
                int exMinReps = Integer.parseInt(exMinRepsString);
                int exMaxReps = Integer.parseInt(exMaxRepsString);
                int exRest = Integer.parseInt(exRestString);
                int exRestSec = (!exRestSecString.isEmpty())?Integer.parseInt(exRestSecString):0;
                for(int i=0;i<exSets;i++){
                    View child = binding.weightsContainer.getChildAt(i);
                    if (child instanceof EditText) {
                        EditText weightEditText = (EditText) child;
                        String weightString = weightEditText.getText().toString().trim();

                        if (weightString.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill all weight fields", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        Double weight = Double.parseDouble(weightString);

                        weightValues.add(weight);
                    }
                }
                LiveDataUtils.observeOnce(editExerciseViewModel.getLastOrder(workoutId), this, exercise -> {
                    int order = 1;
                    if(exercise!=null){
                        order = exercise.getOrder()+1;
                    }
                    Exercise insExercise = new Exercise(exName, exSets, order, exRest, exRestSec, exMaxReps, exMinReps, workoutId);
                    long insertedId = editExerciseViewModel.insertExercise(insExercise);
                    for(int i=0; i<weightValues.size(); i++){
                        editExerciseViewModel.insertSet(new Set((int) insertedId, i+1, weightValues.get(i)));
                    }
                });
                dismiss();
            }
        });


        return binding.getRoot();

    }

    private void addWeightField(LayoutInflater inflater, int i) {
        EditText weightEditText = (EditText) inflater.inflate(R.layout.wight_set_edit_text, null);
        weightEditText.setHint("Set " + (i + 1));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
        params.setMargins(8, 8, 8, 8);

        weightEditText.setLayoutParams(params);

        binding.weightsContainer.addView(weightEditText);
    }

    private void addWeightField(LayoutInflater inflater, Double value) {
        EditText weightEditText = (EditText) inflater.inflate(R.layout.wight_set_edit_text, null);
        weightEditText.setText(value.toString());

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
        params.setMargins(8, 8, 8, 8);

        weightEditText.setLayoutParams(params);

        binding.weightsContainer.addView(weightEditText);
    }

    private void setButtons(String status, LayoutInflater inflater){
        if(status.equalsIgnoreCase("update")){
            binding.addBtn.setVisibility(View.GONE);
            binding.saveBtn.setVisibility(View.VISIBLE);
            binding.deleteBtn.setVisibility(View.VISIBLE);
            editExerciseViewModel.getExerciseById(exerciseId).observe(this, exercise -> {
                if(exercise!=null){
                    binding.exNameEt.setText(exercise.getExercise_name());
                    binding.exRestEt.setText(String.format("%02d", exercise.getRest_time_minutes()));
                    binding.exRestSecEt.setText(String.format("%02d", exercise.getRest_time_seconds()));
                    binding.setSpinner.setText(adapter.getItem(exercise.getNumber_of_sets()-1), false);
                    binding.exMinRepsEt.setText(String.valueOf(exercise.getTarget_reps_min()));
                    binding.exMaxRepsEt.setText(String.valueOf(exercise.getTarget_reps_max()));
                    editExerciseViewModel.getSets(exerciseId).observe(this, new Observer<List<Set>>() {
                        @Override
                        public void onChanged(List<Set> sets) {
                            for (int i = 0; i < sets.size(); i++) {
                                addWeightField(inflater, sets.get(i).getWeight());
                            }
                        }
                    });
                }
            });
        }else{
            addWeightField(inflater, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class InputFilterMinMax implements InputFilter{
        private int minimumValue;
        private int maximumValue;

        public InputFilterMinMax(int minimumValue, int maximumValue) {
            this.minimumValue = minimumValue;
            this.maximumValue = maximumValue;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
                if (isInRange(minimumValue, maximumValue, input))
                    return null;
            }
            catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}