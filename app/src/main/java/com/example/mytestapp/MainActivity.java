package com.example.mytestapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    //The view objects
    EditText username, email_id, phone_no;
    Button submit;


    //defining AwesomeValidation object
    String s_username, s_email, s_phone, date, s_location, s_address;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String fromEmail,fromPassword,toEmail,emailSubject,emailBody;
    List<String> toEmailList;

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTrack();
        findElements();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findValues();
                getDateTime();
                validateData();
                Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void getTrack() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("LOCATION", location.toString());
                s_location=location.toString();
                //Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();

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

        };

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
        }

    }


    //initializing view objects
    private void findElements() {
        username = findViewById(R.id.uname);
        email_id = findViewById(R.id.email);
        phone_no = findViewById(R.id.phone_no);
        submit = findViewById(R.id.submit);
    }

    //assigning  values from edit-text to String object
    private void findValues() {
        s_username = username.getText().toString();
        s_email = email_id.getText().toString();
        s_phone = phone_no.getText().toString();
    }

    // validating values
    private void validateData() {
        if (s_username.length() != 0) {
            if (s_email.length() != 0) {
                if (s_email.matches(emailPattern)) {
                    if (s_phone.length() != 0) {
                        if (s_phone.length() == 10) {

                            //Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                            sentMail();

                        } else {
                            phone_no.setError("enter valid phone number");
                        }
                    } else {
                        phone_no.setError("enter phone number");

                    }
                } else {
                    email_id.setError("enter valid email id");
                }
            } else {
                email_id.setError("enter email id");
            }
        } else {

            username.setError("enter username");

        }
    }

    // get current date and time
    private void getDateTime() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
        date = df.format(Calendar.getInstance().getTime());
        //  Log.e("getDateTime:", date);
    }

    // sent mail
    private void sentMail() {


        fromEmail = "makingthepath@cusat.ac.in";
         fromPassword = "Rajalakshmi1997";
        toEmail = "deadbrainviv@gmail.com";
        toEmailList = Arrays.asList(toEmail
                .split("\\s*,\\s*"));
        emailSubject = "User of TestApp Logged in at \" + date";
        emailBody = "Name :" + s_username + "\n Email :" + s_email + "\n Contact :" + s_phone + "\n Location" + s_location;

        new SendMailTask(MainActivity.this).execute(fromEmail,fromPassword,toEmailList,emailSubject,emailBody);


       /* Intent intent = new Intent(Intent.ACTION_SEND);
        String[] emails_in_to = {"deadbrainviv@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, emails_in_to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "User of TestApp Logged in at " + date);
        intent.putExtra(Intent.EXTRA_TEXT, "Name :" + s_username + "\n Email :" + s_email + "\n Contact :" + s_phone + "\n Location" + s_location);
        intent.putExtra(Intent.EXTRA_CC, "makingthepath@gmail.com");
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(intent);*/


    }


}