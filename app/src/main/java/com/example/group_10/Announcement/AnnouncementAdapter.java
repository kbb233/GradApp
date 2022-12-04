package com.example.group_10.Announcement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.group_10.R;

import java.util.ArrayList;

public class AnnouncementAdapter extends RecyclerView.Adapter {
    private ArrayList<Announcement_Unit> announceList;
    private View.OnClickListener mOnItemClickListener;
    private Context parentContext;
    private boolean isDeleting;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDelete(boolean status) {
        isDeleting = status;
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        public TextView textTitle,textContent
                ,textYear,textMonth,textDay
                ,textHour,textMinute,textSecond;
        public Button deleteButton;
        public AnnouncementViewHolder(View v){
            super(v);
            textTitle = v.findViewById(R.id.textTitle);
            textContent = v.findViewById(R.id.textContent);
            textYear = v.findViewById(R.id.textYear);
            textMonth = v.findViewById(R.id.textMonth);
            textDay = v.findViewById(R.id.textDay);
            textHour = v.findViewById(R.id.textHour);
            textMinute = v.findViewById(R.id.textMinute);
            textSecond = v.findViewById(R.id.textSecond);
            deleteButton = v.findViewById(R.id.button_delete);
        }

        public Button getDeleteButton(){
            return deleteButton;
        }
        public TextView getTitleTextView() {
            return textTitle;
        }
        public TextView getContentTextView() {
            return textContent;
        }
        public TextView getYearTextView() {
            return textYear;
        }
        public TextView getMonthTextView() {
            return textMonth;
        }
        public TextView getDayTextView() {
            return textDay;
        }
        public TextView getHourTextView() {
            return textHour;
        }
        public TextView getMinuteTextView() {
            return textMinute;
        }
        public TextView getSecondTextView() {
            return textSecond;
        }

    }

    public AnnouncementAdapter(ArrayList<Announcement_Unit> arr, Context context){
        announceList = arr;
        parentContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announce_list,parent,false);
        return new AnnouncementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AnnouncementViewHolder avh = (AnnouncementViewHolder)holder;
        avh.getTitleTextView().setText(announceList.get(position).getTitle());
        avh.getContentTextView().setText(announceList.get(position).getContent());
        avh.getYearTextView().setText(String.valueOf(announceList.get(position).getYear()));
        avh.getMonthTextView().setText(String.valueOf(announceList.get(position).getMonth()));
        avh.getDayTextView().setText(String.valueOf(announceList.get(position).getDay()));
        avh.getHourTextView().setText(String.valueOf(announceList.get(position).getHour()));
        avh.getMinuteTextView().setText(String.valueOf(announceList.get(position).getMinute()));
        avh.getSecondTextView().setText(String.valueOf(announceList.get(position).getSecond()));

        if(isDeleting){
            avh.getDeleteButton().setVisibility(View.VISIBLE);
            avh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else{
            avh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    private void deleteItem(int position) {
        Announcement_Unit ann = announceList.get(position);
        AnnouncementDataSource ds = new AnnouncementDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteAnnouncement(ann.getAnnounceId());
            ds.close();
            if (didDelete){
                announceList.remove(position);
                notifyDataSetChanged();
            }
            else{
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return announceList.size();
    }
}
