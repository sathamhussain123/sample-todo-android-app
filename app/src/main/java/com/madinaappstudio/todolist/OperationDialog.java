package com.madinaappstudio.todolist;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class OperationDialog {

    public static void showAddTaskDialog(Context context, DatabaseHelper databaseHelper, TaskAdapter taskAdapter,
                                         List<TaskModel> taskModels, RecyclerView mRecyclerView, Integer taskId) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_operation);

        TextView mOperationText = dialog.findViewById(R.id.mOperationText);
        TextInputLayout mInputTitleLayout = dialog.findViewById(R.id.mInputTitle);
        TextInputLayout mInputDescriptionLayout = dialog.findViewById(R.id.mInputDescription);
        Button mBtnDialogCancel = dialog.findViewById(R.id.mBtnDialogCancel);
        Button mBtnDialogOperation = dialog.findViewById(R.id.mBtnDialogOperation);

        if (taskId != null) {
            TaskModel taskModel = databaseHelper.getSpecificTask(taskId);
            mInputTitleLayout.getEditText().setText(taskModel.getTitle());
            mInputDescriptionLayout.getEditText().setText(taskModel.getDescription());
            mOperationText.setText(R.string.text_edit_task);
            mBtnDialogOperation.setText(R.string.save);
        } else {
            mOperationText.setText(R.string.text_add_task);
            mBtnDialogOperation.setText(R.string.add);
        }

        dialog.show();

        mBtnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mBtnDialogOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputTitleLayout.getEditText() != null && mInputDescriptionLayout.getEditText() != null) {

                    String title = mInputTitleLayout.getEditText().getText().toString();
                    String des = mInputDescriptionLayout.getEditText().getText().toString();

                    if (!title.isEmpty() && !des.isEmpty()) {
                        long millis = System.currentTimeMillis();
                        String date = dateFormat(millis);
                        if (taskId != null) {
                            databaseHelper.updateTask(new TaskModel(taskId, title, des, date));
                        } else {
                            databaseHelper.addTask(new TaskModel(title, des, date));
                        }

                        OperationDialog.refreshTaskList(context, taskAdapter, taskModels, databaseHelper, mRecyclerView);
                        dialog.dismiss();

                    } else {
                        Toast.makeText(context, "All fields required!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private static String dateFormat(long selection) {
        Instant instant = Instant.ofEpochMilli(selection);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM", Locale.getDefault());
        String dateString = localDateTime.format(formatter);
        Log.d("TAG", "dateFormat: " + dateString);
        return String.valueOf(dateString);
    }

    public static void refreshTaskList(Context context, TaskAdapter taskAdapter, List<TaskModel> taskModels,
                                       DatabaseHelper databaseHelper, RecyclerView mRecyclerView) {
        taskAdapter.notifyItemInserted(taskModels.size());
        taskModels = databaseHelper.getAllTask();
        taskAdapter = new TaskAdapter(context, taskModels, mRecyclerView);
        mRecyclerView.setAdapter(taskAdapter);
    }
}
