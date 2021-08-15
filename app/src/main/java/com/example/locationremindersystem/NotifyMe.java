package com.example.locationremindersystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class NotifyMe extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String FILE_NAME = "TaskList";
    public ArrayList<Task> tasks = new ArrayList<>();
    public void addTaskClicked(View view) {

        EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
        EditText taskDescriptionEditText = (EditText) findViewById(R.id.taskDescEditText);

        String taskTitle = taskTitleEditText.getText().toString();
        String taskDescription = taskDescriptionEditText.getText().toString();

        if(taskTitle.isEmpty() || taskTitle == null) {
            Toast.makeText(NotifyMe.this, "Place cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (taskDescription.isEmpty() || taskDescription == null) {
            Toast.makeText(NotifyMe.this, "Task cannot be empty", Toast.LENGTH_LONG).show();
        }
        else
        {
            taskTitleEditText.setText("");
            taskDescriptionEditText.setText("");
            Task newTask = new Task(taskTitle, taskDescription,"1");
            tasks.add(newTask);
            saveData();
            TaskAdapter adapter = new TaskAdapter(this, R.layout.adapter_view_laylout, tasks);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);

        }

    }
    public void checkBoxClicked(View view) {

        int position = (Integer) view.getTag();

        tasks.remove(position);
        saveData();
        TaskAdapter adapter = new TaskAdapter(this, R.layout.adapter_view_laylout, tasks);
        ListView listView = findViewById(R.id.listView);
        listView.animate();
        listView.setAdapter(adapter);
        Toast.makeText(NotifyMe.this, "Task Completed", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_me);
       // getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    //    getSupportActionBar().setCustomView(R.layout.action_bar);
        Log.d(TAG, "onCreate: started");

        final ListView listView = findViewById(R.id.listView);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        loadData();
        final TaskAdapter adapter = new TaskAdapter(this, R.layout.adapter_view_laylout, tasks);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                tasks.remove(position);
                saveData();

                TaskAdapter adapter1 = new TaskAdapter(NotifyMe.this, R.layout.adapter_view_laylout, tasks);
                listView.setAdapter(adapter);

                Toast.makeText(NotifyMe.this, "Task Completed", Toast.LENGTH_SHORT).show();



                return false;
            }
        });
    }
    private void saveData(){

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(tasks);
        editor.putString("task list", json);
        editor.apply();

    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken< ArrayList<Task> >() {}.getType();
        tasks = gson.fromJson(json, type);

        if(tasks == null) {
            tasks = new ArrayList<Task>();
        }

    }

}