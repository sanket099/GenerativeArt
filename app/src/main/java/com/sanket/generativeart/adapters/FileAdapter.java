package com.sanket.generativeart.adapters;


import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.generativeart.R;

import java.io.File;
import java.util.ArrayList;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<File> dataModelArrayList;
    private final OnNoteList onNoteList;


    // private int selected = -1;
    public FileAdapter(LayoutInflater inflater, ArrayList<File> dataModelArrayList, OnNoteList onNoteList, Activity activity) {
        this.inflater = inflater;
        this.dataModelArrayList = dataModelArrayList;
        this.onNoteList = onNoteList;

    }

    @NonNull
    @Override
    public FileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.screenshots, parent, false);
        FileAdapter.MyViewHolder holder = new FileAdapter.MyViewHolder(view,onNoteList);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileAdapter.MyViewHolder holder, int position) {

        String path = dataModelArrayList.get(position).getPath();
        File imgFile = new  File(path);
        if(imgFile.exists())
        {

            holder.ing_iv.setImageURI(Uri.fromFile(imgFile));

        }

    }


    @Override
    public int getItemCount() {

        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ing_iv;

        OnNoteList onNoteList;
        public MyViewHolder(@NonNull View itemView ,OnNoteList onNoteList) {
            super(itemView);

            ing_iv = itemView.findViewById(R.id.screen_img);

            this.onNoteList = onNoteList;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (dataModelArrayList != null) {
                try {
                    onNoteList.OnNoteClick(dataModelArrayList.get(getAdapterPosition()));
                } catch (IndexOutOfBoundsException e) {
                    //.println(dataModelArrayList);
                }

            }
        }
    }





    public interface OnNoteList {
        void OnNoteClick(File userClass);


    }
}
