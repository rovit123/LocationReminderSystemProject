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
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class SendMsg extends AppCompatActivity {
    public static final String TAG = "SndActivity";
    public static final String FILE_NAME = "SendList";
    public ArrayList<Task> sendtasks = new ArrayList<>();
    private Pattern pattern = Pattern.compile("^\\d{10}$");
    public void addTaskClicked(View view) {

        EditText taskTitleEditText = (EditText) findViewById(R.id.taskTitleEditText);
        EditText taskDescriptionEditText = (EditText) findViewById(R.id.taskDescEditText);
        EditText numberEditText=(EditText)findViewById(R.id.numberEditText);

        String taskTitle = taskTitleEditText.getText().toString();
        String taskDescription = taskDescriptionEditText.getText().toString();
        String number=numberEditText.getText().toString();

        if(taskTitle.isEmpty() || taskTitle == null) {
            Toast.makeText(SendMsg.this, "Place cannot be empty", Toast.LENGTH_LONG).show();
        }
        else if (taskDescription.isEmpty() || taskDescription == null) {
            Toast.makeText(SendMsg.this, "Task cannot be empty", Toast.LENGTH_LONG).show();
        }else if(number.isEmpty()||number==null||!(pattern.matcher(number).matches())){
            Toast.makeText(SendMsg.this, "Number cannot be empty or alphabet", Toast.LENGTH_LONG).show();
        }
        else
        {
            taskTitleEditText.setText("");
            taskDescriptionEditText.setText("");
            numberEditText.setText("");
            Task newTask = new Task(taskTitle, taskDescription,"1",number);
            sendtasks.add(newTask);
            saveData();
            SendTaskAdapter adapter = new SendTaskAdapter(this, R.layout.send_task_adapter, sendtasks);
            ListView listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);

        }

    }
    public void checkBoxClicked(View view) {

        int position = (Integer) view.getTag();

        sendtasks.remove(position);
        saveData();
        SendTaskAdapter adapter = new SendTaskAdapter(this, R.layout.send_task_adapter, sendtasks);
        ListView listView = findViewById(R.id.listView);
        listView.animate();
        listView.setAdapter(adapter);
        Toast.makeText(SendMsg.this, "Task Completed", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_msg);
     //   getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.send_message_action_bar);
        Log.d(TAG, "onCreate: started");

        final ListView listView = findViewById(R.id.listView);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        loadData();
        final SendTaskAdapter adapter = new SendTaskAdapter(this, R.layout.send_task_adapter, sendtasks);
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                sendtasks.remove(position);
                saveData();

                SendTaskAdapter adapter1 = new SendTaskAdapter(SendMsg.this, R.layout.send_task_adapter, sendtasks);
                listView.setAdapter(adapter);

                Toast.makeText(SendMsg.this, "Task Completed", Toast.LENGTH_SHORT).show();



                return false;
            }
        });
    }
    private void saveData(){

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(sendtasks);
        editor.putString("send list", json);
        editor.apply();

    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("send list", null);
        Type type = new TypeToken< ArrayList<Task> >() {}.getType();
        sendtasks = gson.fromJson(json, type);

        if(sendtasks == null) {
            sendtasks = new ArrayList<Task>();
        }

    }
}