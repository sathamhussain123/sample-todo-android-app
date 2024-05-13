package com.madinaappstudio.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "date";

    Context context;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TASKS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TITLE + " TEXT," +
                KEY_DESCRIPTION + " TEXT," +
                KEY_DATE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
    }

    public List<TaskModel> getAllTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskModel> taskModels = new ArrayList<>();

        Cursor cursor = db.query(TABLE_TASKS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));
                    taskModels.add(new TaskModel(id, title, description, date));
                } while (cursor.moveToNext());

                cursor.close();
            } catch (Exception e) {
                Log.d("DBHelper", "unable to get all task " + e);
            }
        }
        db.close();
        return taskModels;
    }

    public void addTask(TaskModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, taskModel.getTitle());
        contentValues.put(KEY_DESCRIPTION, taskModel.getDescription());
        contentValues.put(KEY_DATE, taskModel.getDate());
        db.insert(TABLE_TASKS, null, contentValues);

        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(taskId)};

        db.delete(TABLE_TASKS, whereClause, whereArgs);
        db.close();
    }

    public void updateTask(TaskModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_ID, taskModel.getId());
        contentValues.put(KEY_TITLE, taskModel.getTitle());
        contentValues.put(KEY_DESCRIPTION, taskModel.getDescription());

        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {String.valueOf(taskModel.getId())};

        db.update(TABLE_TASKS, contentValues, whereClause, whereArgs);
        db.close();
    }

    public TaskModel getSpecificTask(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TaskModel taskModel = null;
        Cursor cursor = db.query(TABLE_TASKS,
                null,
                KEY_ID + " = ?",
                new String[]{String.valueOf(taskId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            try {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE));
                taskModel = new TaskModel(taskId, title, description, date);
            } catch (Exception e) {
                Log.e("DBHelper", "unable to retrieve specific task ", e);
            } finally {
                cursor.close();
            }
        }
        db.close();
        return taskModel;
    }

    public void importDemoDB() {
        SQLiteDatabase tasksDB = getWritableDatabase();

        try {
            InputStream inputStream = context.getAssets().open("demo.db");
            String outFileName = context.getDatabasePath("demo.db").getPath();
            OutputStream outputStream = Files.newOutputStream(Paths.get(outFileName));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            SQLiteDatabase demoDB = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = demoDB.rawQuery("SELECT * FROM tasks", null);
            while (cursor.moveToNext()) {
                ContentValues values = new ContentValues();
                values.put("id", cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                values.put("title", cursor.getString(cursor.getColumnIndexOrThrow("title")));
                values.put("description", cursor.getString(cursor.getColumnIndexOrThrow("description")));
                values.put("date", cursor.getString(cursor.getColumnIndexOrThrow("date")));
                tasksDB.insert("tasks", null, values);
            }

            cursor.close();
            demoDB.close();
            tasksDB.close();

        } catch (IOException e) {
            Toast.makeText(context, "Unable to import!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
