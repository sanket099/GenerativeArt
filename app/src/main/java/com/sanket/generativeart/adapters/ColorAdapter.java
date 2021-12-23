    package com.sanket.generativeart.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanket.generativeart.models.MyColor;
import com.sanket.generativeart.R;
import com.sanket.generativeart.util.TinyDB;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<MyColor> dataModelArrayList;
    private OnNoteList onNoteList;
    private TinyDB tinydb;


    // private int selected = -1;
    public ColorAdapter(LayoutInflater inflater, List<MyColor> dataModelArrayList, OnNoteList onNoteList, Activity activity) {
        this.inflater = inflater;
        this.dataModelArrayList = dataModelArrayList;
        this.onNoteList = onNoteList;
        tinydb = new TinyDB(inflater.getContext());

    }

    @NonNull
    @Override
    public ColorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //("hello2");
        View view = inflater.inflate(R.layout.color_thumb, parent, false);
        ColorAdapter.MyViewHolder holder = new ColorAdapter.MyViewHolder(view,onNoteList);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ColorAdapter.MyViewHolder holder, int position) {

        try {
                int col = dataModelArrayList.get(position).getColor();
                holder.ing_iv.setBackgroundColor(col);

                if(tinydb.getListObject("colors", MyColor.class).size() != 0){
                        if(tinydb.getListObject("colors", MyColor.class)
                                .contains(dataModelArrayList.get(position))){
                            dataModelArrayList.get(position).setSelected(true);
                        }
                }

                if (dataModelArrayList.get(position).isSelected()) {
                    //("visible " + dataModelArrayList.get(position).getIngredientName());
                    holder.iv_ticked.setVisibility(View.VISIBLE);
                }
                else {
                    //("invisible " + dataModelArrayList.get(position).getIngredientName());
                    holder.iv_ticked.setVisibility(View.INVISIBLE);
                }

        }
        catch (IndexOutOfBoundsException e){
            System.out.println("e.getMessage() = " + e.getMessage());
        }


    }



    @Override
    public int getItemCount() {
        if(dataModelArrayList == null )return 0;
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ing_iv;
        ImageView iv_ticked;
        OnNoteList onNoteList;
        public MyViewHolder(@NonNull View itemView ,OnNoteList onNoteList) {
            super(itemView);

            ing_iv = itemView.findViewById(R.id.iv_ing);
            iv_ticked = itemView.findViewById(R.id.iv_ticked);

            this.onNoteList = onNoteList;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (dataModelArrayList != null) {
                try {
                    onNoteList.OnnoteClick(dataModelArrayList.get(getAdapterPosition()));
                } catch (IndexOutOfBoundsException e) {
                    //.println(dataModelArrayList);
                }

            }
        }
    }

    public interface OnNoteList {
        void OnnoteClick(MyColor userClass);


    }
}
