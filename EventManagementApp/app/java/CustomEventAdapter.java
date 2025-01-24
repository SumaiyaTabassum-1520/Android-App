package com.example.eventmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import java.util.ArrayList;

public class CustomEventAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> events;

    public CustomEventAdapter(@NonNull Context context, @NonNull ArrayList<Event> items) {
        super(context, -1, items);
        this.context = context;
        this.events = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row_event, parent, false);

        TextView eventName = rowView.findViewById(R.id.tvEventName);
        TextView eventDateTime = rowView.findViewById(R.id.tvDate);
        TextView eventPlace = rowView.findViewById(R.id.tvEventPlace);
        //TextView eventType = rowView.findViewById(R.id.tvEventType);

        Event e = events.get(position);
        eventName.setText(e.name);
        eventDateTime.setText(e.datetime);
        eventPlace.setText(e.place);
        //eventType.setText(e.eventType);
        return rowView;
    }

    public void notifyDatasetChanged() {
    }
}
