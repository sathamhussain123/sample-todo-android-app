package com.madinaappstudio.todolist;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.madinaappstudio.todolist.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<TaskModel> taskModels = new ArrayList<>();
    TaskAdapter taskAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.mTopToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        
        binding.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        taskModels = databaseHelper.getAllTask();

        taskAdapter = new TaskAdapter(this, taskModels, binding.mRecyclerView);
        binding.mRecyclerView.setAdapter(taskAdapter);

        binding.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperationDialog.showAddTaskDialog(MainActivity.this, databaseHelper, taskAdapter,
                        taskModels, binding.mRecyclerView, null);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.opt_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.mOptImpDemo) {
            databaseHelper.importDemoDB();
            OperationDialog.refreshTaskList(this, taskAdapter, taskModels, databaseHelper, binding.mRecyclerView);
        }
        return super.onOptionsItemSelected(item);
    }
}