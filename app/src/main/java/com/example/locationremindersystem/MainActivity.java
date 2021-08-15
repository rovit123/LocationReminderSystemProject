package com.example.locationremindersystem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final String TAG = "MainActivity";
    public static final String FILE_NAME = "TaskList";
    public static final String FILE_NAME1 = "SendList";
    public ArrayList<Task> tasks = new ArrayList<>();
    public ArrayList<Task> sendtasks = new ArrayList<>();

    Button button_location,notify,sendMessage;
    TextView textView_location;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notify=findViewById(R.id.notify);
        textView_location = findViewById(R.id.text_location);
        button_location = findViewById(R.id.sendMessage);
        sendMessage=findViewById(R.id.sendMessage);
        //Runtime permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this,NotifyMe.class);
                    startActivity(intent);
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i1=new Intent(MainActivity.this,SendMsg.class);
               startActivity(i1);
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, MainActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            textView_location.setText(address);
            SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("task list", null);
            Type type = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks = gson.fromJson(json, type);
            SharedPreferences sharedPreferences1 = getSharedPreferences(FILE_NAME1, MODE_PRIVATE);
            Gson gson1 = new Gson();
            String json1 = sharedPreferences1.getString("send list", null);
            Type type1 = new TypeToken< ArrayList<Task> >() {}.getType();
            sendtasks = gson1.fromJson(json1, type1);
            for(int i=0;i<tasks.size();i++){
                if(address.toLowerCase().contains(tasks.get(i).getTaskTitle())){
                   sendNotification(i);

                }
            }
            for(int i=0;i<sendtasks.size();i++){
                if(address.toLowerCase().contains(sendtasks.get(i).getTaskTitle())){
                    sendMessages(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
    }
    public void sendMessages(int i){
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        String a=sendtasks.get(i).getTaskDescription();
        String num = sendtasks.get(i).getNumber().trim();
        sendtasks.remove(i);
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME1, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(sendtasks);
        editor.putString("send list", json);
        editor.apply();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(checkSelfPermission(Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED){
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(num,null,a,null,null);

            }else{
                requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
            }
        }
        //Get the SmsManager instance and call the sendTextMessage method to send message

    }
    public void sendNotification(int i){
        NotificationManager mNotificationManager;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this.getApplicationContext(), "notify_001");
        Intent ii = new Intent(MainActivity.this.getApplicationContext(), NotifyMe.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.setBigContentTitle(tasks.get(i).getTaskTitle());
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentText(tasks.get(i).getTaskDescription());
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);
        tasks.remove(i);
        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String json = gson.toJson(tasks);
        editor.putString("task list", json);
        editor.apply();
        mNotificationManager =
                (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
