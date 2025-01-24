package com.example.eventmanagementapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class activity_create_event extends Activity {

    private EditText etName, etPlace, etCapacity, etDateAndTime, etBudget, etEmail, etPhone, etDescription;

    private Button btnShare,btnSave,btnCancel;
    private TextView tvError;
    RadioButton rdIndoor, rdOutdoor, rdOnline;
    private String eventID="";
    private EventDB eventDB;


    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                //dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();


}
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("If you cancel record will not save.Close app?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                finish();
            }
        });
        builder.setNegativeButton("No",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void httpRequest(final String keys[],final String values[]){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair>();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                String data="";
                try {
                    data=JSONParser.getInstance().makeHttpRequest(url,"POST",params);
                    System.out.println(data);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    System.out.println(data);
                    System.out.println("Ok2");
                    //updateEventListByServerData(data);
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        etName=findViewById(R.id.etName);
        etPlace=findViewById(R.id.etPlace);
        etCapacity=findViewById(R.id.etCapacity);
        etDateAndTime=findViewById(R.id.etDateAndTime);
        etBudget=findViewById(R.id.etBudget);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etDescription=findViewById(R.id.etDescription);

        eventDB = new EventDB(activity_create_event.this);
        tvError=findViewById(R.id.tvError);
        rdIndoor=findViewById(R.id.rdIndoor);
        rdOutdoor=findViewById(R.id.rdOutdoor);
        rdOnline=findViewById(R.id.rdOnline);

         btnSave= findViewById(R.id.btnSave);
         btnCancel=findViewById(R.id.btnCancel);
         btnShare=findViewById(R.id.btnShare);

         btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Save button was clicked");
                String name = etName.getText().toString();
                String place = etPlace.getText().toString();
                String cap = etCapacity.getText().toString();
                String datetime = etDateAndTime.getText().toString();
                String budgetStr = etBudget.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String description = etDescription.getText().toString();
                boolean isIndoorChecked = rdIndoor.isChecked();
                boolean isOutdoorChecked = rdOutdoor.isChecked();
                boolean isOnlineChecked = rdOnline.isChecked();


                String err = "";


                if (!name.matches("[a-zA-Z]+") || name.length() < 4 || name.length() > 12) {
                    err = "Name should have 4-12 letters, ";
                }

                if (place.length() < 6 && name.length() > 64) {
                    err += "Place should be alpha-numeric values with 6-64 characters, ";
                }
                int capacity=0;
                if (cap.length() > 0) {
                    try {
                        capacity = Integer.parseInt(cap);
                    } catch (Exception e) {
                        err += "Capacity must be greater than zero, ";
                    }
                }

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                formatter.setLenient(false);
                long dt=0;
                try {
                    Date  date = formatter.parse(datetime);
                     dt=date.getTime();
                } catch (ParseException e) {
                    err += "Date format invalid ";

                }
                double budget=0;
                if (budgetStr.length() > 1000) {
                    try {
                        budget = Double.parseDouble(budgetStr);
                    } catch (Exception e) {
                        err += "Budget must be decimal value, ";
                    }
                }
                if (email.isEmpty() || !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    err += "Invalid email, ";

                }
                if ((phone.length() !=15 && phone.charAt(0) == '+') || (phone.charAt(0)!='+' && phone.length()!=11) ) {
                    err += "Invalid phone number.Must be start with +, ";
                }
                if (description.length() < 10 || description.length() > 1000) {
                    err += "description required more than 10 character not more than 1000 character.";
                }
                String eventType = "";
                if (isIndoorChecked == false && isOutdoorChecked == false && isOnlineChecked == false) {
                    err += "One button should be checked";
                }else{
                    if(isIndoorChecked) eventType="indoor" ;
                    else if(isOutdoorChecked) eventType="outdoor";
                    else{eventType="online";}
                }
                if (err.length() > 0) {
                    showErrorDialog(err);
                }
//                else{
//                    Toast.makeText(activity_create_event.this, "saved successfully",Toast.LENGTH_LONG).show();
//                }
                if(eventID.isEmpty()){
                    eventID=name+System.currentTimeMillis();
                    System.out.println("Generated eventID: " + eventID);
                    eventDB.insertEvent(eventID, name,place,dt,capacity,budget,email,phone,description,"");
                    Intent i = new Intent(activity_create_event.this, mainActivity.class);
                    startActivity(i);
                }else{
                    eventDB.updateEvent(eventID,name,place,dt,capacity,budget,phone,email,description,"");
                }
                //store in remote database
                String keys[]={"action", "sid", "semester", "id", "title", "place", "type", "date_type", "capacity", "budget", "phone", "email", "des"};
                String values[]={"backup","2021-1-60-042","2023-2",eventID, name, place, eventType,""+datetime,""+capacity, ""+budget, ""+phone,""+description};
                httpRequest(keys,values);
                //
                //
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showExitDialog();
            }
        });
    }

    }

