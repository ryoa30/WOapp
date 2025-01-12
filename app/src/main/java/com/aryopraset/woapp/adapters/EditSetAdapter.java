package com.aryopraset.woapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aryopraset.woapp.R;
import com.aryopraset.woapp.models.Exercise;
import com.aryopraset.woapp.models.Set;

import java.util.List;

public class EditSetAdapter extends RecyclerView.Adapter<EditSetAdapter.ViewHolder> {
    private List<Set> setsList;

    public EditSetAdapter(List<Set> setsList) {
        this.setsList = setsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_day_wo_sets, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set set = setsList.get(position);
        String weight = set.getWeight().toString();
//        Log.d("TAGGING", "onBindViewHolder: " + weight);
        holder.weightTv.setText(weight);
    }

    @Override
    public int getItemCount() {
        return setsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView weightTv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weightTv = itemView.findViewById(R.id.weightTv);
        }
    }
}
