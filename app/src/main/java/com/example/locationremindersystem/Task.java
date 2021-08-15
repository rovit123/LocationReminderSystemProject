package com.example.locationremindersystem;

public class Task {
    private String taskTitle;
    private String taskDescription;
    private String value;
    private  String number;

    public Task(){
        this.taskTitle = "empty";
        this.taskDescription = "empty";
        this.value="0";
        this.number="";
    }
    public Task(String taskTitle, String taskDescription,String value) {

        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.value=value;
        this.number="";
    }
    public Task(String taskTitle, String taskDescription,String value,String number) {

        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.value=value;
        this.number=number;
    }

    public String getTaskTitle() { return this.taskTitle.toLowerCase(); }

    public String getTaskDescription() {
        return this.taskDescription;
    }
    public String getValue() { return this.value;  }
    public String getNumber() { return this.number;  }
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
