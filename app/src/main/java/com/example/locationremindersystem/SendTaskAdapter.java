package com.example.locationremindersystem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


public class SendTaskAdapter extends ArrayAdapter<Task> {

    public static final String TAG = "SendTaskAdapter";
    private Context mContext;
    private int mResource;


    static class ViewHolder {
        TextView taskTitle;
        TextView taskDescription;
        TextView number;
        CheckBox checkBox;
    }


    public SendTaskAdapter(Context context, int resource, ArrayList<Task> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String taskTitle = getItem(position).getTaskTitle();
        String taskDescription = getItem(position).getTaskDescription();
        String number = getItem(position).getNumber();


        Task task = new Task(taskTitle, taskDescription, "1", number);

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            holder = new ViewHolder();
            holder.taskTitle = (TextView) convertView.findViewById(R.id.send_taskTitle);
            holder.taskDescription = (TextView) convertView.findViewById(R.id.send_taskDescription);
            holder.number = (TextView) convertView.findViewById(R.id.send_Number);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkBox.setTag(position);
        holder.number.setText(number);
        holder.taskTitle.setText(taskTitle);
        holder.taskDescription.setText(taskDescription);


        return convertView;

    }
}

