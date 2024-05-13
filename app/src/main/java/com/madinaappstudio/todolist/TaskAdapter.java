package com.madinaappstudio.todolist;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    Context context;
    List<TaskModel> taskModels;
    DatabaseHelper databaseHelper;
    RecyclerView mRecyclerView;

    public TaskAdapter(Context context, List<TaskModel> taskModels, RecyclerView mRecyclerView) {
        this.context = context;
        this.taskModels = taskModels;
        databaseHelper = new DatabaseHelper(context);
        this.mRecyclerView = mRecyclerView;
    }

    public TaskAdapter getInstance() {
        return TaskAdapter.this;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        int taskId = taskModels.get(position).getId();
        holder.mTitle.setText(taskModels.get(position).title);
        holder.mDescription.setText(taskModels.get(position).description);

        String[] date = trimDate(taskModels.get(position).date);
        holder.mDay.setText(date[0]);
        holder.mMonth.setText(getMonth(date[1]));

        holder.mTaskRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.dialog_more_option);
                bottomSheetDialog.show();

                Button mBtnDialogView = bottomSheetDialog.findViewById(R.id.mBtnDialogView);
                Button mBtnDialogEdit = bottomSheetDialog.findViewById(R.id.mBtnDialogEdit);
                Button mBtnDialogDelete = bottomSheetDialog.findViewById(R.id.mBtnDialogDelete);

                mBtnDialogView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        showViewTaskDialog(taskId);
                    }
                });

                mBtnDialogDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseHelper.deleteTask(taskId);
                        taskModels.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        bottomSheetDialog.dismiss();
                    }
                });

                mBtnDialogEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OperationDialog.showAddTaskDialog(context, databaseHelper, getInstance(), taskModels, mRecyclerView, taskId);
                        bottomSheetDialog.dismiss();
                    }
                });

                return true;
            }
        });
    }

    private void showViewTaskDialog(int taskId) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_view_task);
        dialog.setCancelable(false);

        TextView mDialogViewTitle = dialog.findViewById(R.id.mDialogViewTitle);
        TextView mDialogViewDes = dialog.findViewById(R.id.mDialogViewDes);
        TextView mBtnDialogOk = dialog.findViewById(R.id.mBtnDialogOk);

        TaskModel taskModel = databaseHelper.getSpecificTask(taskId);

        mDialogViewTitle.setText(taskModel.getTitle());
        mDialogViewDes.setText(taskModel.getDescription());
        dialog.show();

        mBtnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private String getMonth(String s) {
        String month;
        switch (Integer.parseInt(s)) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sep";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;
            default:
                month = "!";
        }
        return month;

    }

    private String[] trimDate(String rawDate) {
        String day = rawDate.substring(0, 2);
        String month = rawDate.substring(3, 5);
        String[] date = new String[2];
        date[0] = day;
        date[1] = month;
        return date;
    }

    @Override
    public int getItemCount() {
        return taskModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mDescription, mDay, mMonth;
        MaterialCardView mTaskRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.mTitle);
            mDescription = itemView.findViewById(R.id.mDescription);
            mDay = itemView.findViewById(R.id.mDay);
            mMonth = itemView.findViewById(R.id.mMonth);
            mTaskRow = itemView.findViewById(R.id.mTaskRow);
        }
    }
}
