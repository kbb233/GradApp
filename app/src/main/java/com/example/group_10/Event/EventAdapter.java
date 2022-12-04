package com.example.group_10.Event;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class EventAdapter extends RecyclerView.Adapter {
    private ArrayList<Event_Unit> eventList;
    private View.OnClickListener mOnItemClickListener;
    private Context parentContext;
    private boolean isDeleting;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDelete(boolean status) {
        isDeleting = status;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView textEventTitle, textEventTContent, textEventTDay;
        public Button deleteEventButton;
        public CountdownView cv;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textEventTitle = itemView.findViewById(R.id.eventTitle);
            textEventTContent = itemView.findViewById(R.id.eventContent);
            textEventTDay = itemView.findViewById(R.id.eventDay);
            deleteEventButton = itemView.findViewById(R.id.button_delete);
            cv = itemView.findViewById(R.id.counting);
        }

        public CountdownView getCountdownView() {return cv;}
        public Button getDeleteButton(){
            return deleteEventButton;
        }
        public TextView getEventTitle() {
            return textEventTitle;
        }
        public TextView getEventContent() {
            return textEventTContent;
        }
        public TextView getEventDay() {
            return textEventTDay;
        }
    }
    public EventAdapter(ArrayList<Event_Unit> arr,Context context){
        eventList = arr;
        parentContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list,parent,false);
        return new EventViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        EventViewHolder evh = (EventViewHolder) holder;
        evh.getEventTitle().setText(eventList.get(position).getTitle());
        evh.getEventContent().setText(eventList.get(position).getContent());
        evh.getEventDay().setText("Event date: "+eventList.get(position).getYear()+"/"
                +eventList.get(position).getMonth()+"/"
                +eventList.get(position).getDay()+" "
                +eventList.get(position).getHour()+":"
                +eventList.get(position).getMinute());

        long sec = setTimer(eventList.get(position).getYear(),eventList.get(position).getMonth(),eventList.get(position).getDay(),eventList.get(position).getHour(),eventList.get(position).getMinute());
        if(sec<0){
            Log.e("adapter","sec<0");
        }

        evh.getCountdownView().start(sec);

        if(isDeleting){
            evh.getDeleteButton().setVisibility(View.VISIBLE);
            evh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else{
            evh.getDeleteButton().setVisibility(View.INVISIBLE);
        }

    }
    private long setTimer(int year, int month, int day, int hour, int minute){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String countData = day+"-"+(month+1)+"-"+year+" "+hour+":"+minute+":"+"00";
        Date now = new Date();
        long diff = 0;
        try{
            Date date = formatter.parse(countData);
            diff = date.getTime()-now.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    private void deleteItem(int position) {
        Event_Unit events = eventList.get(position);
        EventDataSource ds = new EventDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteEvent(events.getEventId());
            ds.close();
            if (didDelete){
                eventList.remove(position);
                notifyDataSetChanged();
            }
            else{
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }
    public int getItemCount() {
        return eventList.size();
    }
}
