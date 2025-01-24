package com.example.eventmanagementapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class mainActivity extends Activity {


    private ListView lvEvents;
    private ArrayList<Event> events;
    private CustomEventAdapter adapter;
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        lvEvents = findViewById(R.id.lvEvents);
        events = new ArrayList();
        loadData();
    }
    public void onStart() {

        super.onStart();
        loadData();

        String keys[] = {"action", "sid", "semester"};
        String values[] = {"restore", "2021-1-60-042", "2023-2"};
        httpRequest(keys, values);

    }

    private void httpRequest(String[] keys, String[] values) {
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


    private void updateEventListByServerData(String data){
        System.out.println("found");
        try{
            JSONObject jo = new JSONObject(data);
            if(jo.has("events")){
                events.clear();
                JSONArray ja = jo.getJSONArray("events");
                for(int i=0; i<ja.length(); i++){
                    JSONObject event = ja.getJSONObject(i);
                    String id = event.getString("id");
                    String title = event.getString("title");
                    String place = event.getString("place");
                    String type=event.getString("type");
                    long dateTime=event.getLong("date_time");
                    int capacity=event.getInt("capacity");
                    double budget=event.getDouble("budget");
                    String email = event.getString("email");
                    String phone = event.getString("phone");
                    String des = event.getString("des");

                    Event e = new Event(id, title, place,type,""+dateTime,""+capacity,""+budget,email,phone,des);
                    events.add(e);
                }
                adapter.notifyDatasetChanged();
            }
        }catch(Exception e){}
    }



    private void loadData() {

        events.clear();
        String q = "SELECT * from events";
            EventDB db = new EventDB(this);
            Cursor rows = db.selectEvents(q);
        if (rows.getCount() > 0) {
            while(rows.moveToNext()){
                String id= rows.getString(0);
                String name= rows.getString(1);
                String place= rows.getString(2);
                long dateTime= rows.getLong(3);
                int  capacity= rows.getInt(4);
                double budget= rows.getDouble(5);
                String email= rows.getString(6);
                String phone= rows.getString(7);
                String description= rows.getString(8);
                String eventType=rows.getString(9);

                Event e = new Event(id, name, place,""+ dateTime, ""+capacity, ""+budget, email, phone, description, eventType);
                events.add(e);
            }

        }
        db.close();

        adapter = new CustomEventAdapter(this, events);
        lvEvents.setAdapter(adapter);
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                System.out.println(position);
                Intent i = new Intent(mainActivity.this, activity_create_event.class);
                i.putExtra("ID", events.get(position).id);
                i.putExtra("Name", events.get(position).name);
                i.putExtra("Place", events.get(position).place);
                i.putExtra("Datetime", events.get(position).datetime);
                i.putExtra("Capacity", events.get(position).capacity);
                i.putExtra("Budget", events.get(position).budget);
                i.putExtra("Email", events.get(position).email);
                i.putExtra("Phone", events.get(position).phone);
                i.putExtra("description", events.get(position).description);
                i.putExtra("EventType", events.get(position).eventType);
                startActivity(i);
            }
        });
        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity.this);
                builder.setTitle("Confirm Delete");
                String message = "Do you want to delete event -" +events.get(position).name +"?";
                System.out.println(message);
                return true;
            }
        });
    }

}
