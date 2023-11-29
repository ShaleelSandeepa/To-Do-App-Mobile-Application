package com.todolist.todoapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todolist.todoapp.AddNewTask;
import com.todolist.todoapp.MainActivity;
import com.todolist.todoapp.R;
import com.todolist.todoapp.model.ToDoModel;
import com.todolist.todoapp.utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> modelList;
    private MainActivity activity;
    private DataBaseHelper dataBaseHelper;

    public ToDoAdapter(DataBaseHelper dataBaseHelper, MainActivity activity) {
        this.activity = activity;
        this.dataBaseHelper = dataBaseHelper;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = modelList.get(position);
        holder.checkBox.setText(item.getTask());
        holder.checkBox.setChecked(toBoolean(item.getStatus()));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataBaseHelper.updateStatus(item.getId(), 1);
                } else {
                    dataBaseHelper.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = modelList.get(position);
        dataBaseHelper.deleteTask(item.getId());
        modelList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = modelList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), task.getTag());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

}
