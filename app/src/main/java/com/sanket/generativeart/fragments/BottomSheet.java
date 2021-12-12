package com.sanket.generativeart.fragments;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanket.generativeart.R;
import com.sanket.generativeart.adapters.ColorAdapter;
import com.sanket.generativeart.models.MyColor;
import com.sanket.generativeart.util.LayoutManager;
import com.sanket.generativeart.util.TinyDB;

import java.util.ArrayList;


public class BottomSheet extends BottomSheetDialogFragment implements ColorAdapter.OnNoteList {
    private BottomSheetListener mListener;
    //SharedPref sharedPref;
    TinyDB tinydb;
    RecyclerView recyclerView;
    ColorAdapter colorAdapter;

    private ArrayList<MyColor> arrayList;
    ArrayList<MyColor> selectedColors;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        tinydb = new TinyDB(getContext());
       // sharedPref = new SharedPref(getContext());
        recyclerView = v.findViewById(R.id.recycler);
        selectedColors = new ArrayList<>();
        recyclerInit();
        SeekBar size = v.findViewById(R.id.size);
        SeekBar density = v.findViewById(R.id.density);

        size.setMax(50);
        density.setMax(10);

        size.setProgress(tinydb.getInt("size"));
        density.setProgress(tinydb.getIntDensity("density"));


        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                System.out.println("progress = " + progress);
                mListener.setSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        density.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                System.out.println("progress density = " + progress);
                mListener.setDensity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        addColorInList(0xFF000000);
        addColorInList(0xFF444444);
        addColorInList(0xFF888888);
        addColorInList(0xFFFFFFFF);
        addColorInList(0xFF00FF00);
        addColorInList(0xFF0000FF);
        addColorInList(0xFFFFFF00);
        addColorInList(0xFFFF0000);

        setUpRecyclerView();

        return  v;
    }

    public void addColorInList(int color){
        MyColor myColor = new MyColor(color, false);
        arrayList.add(myColor);
    }

    public interface BottomSheetListener {
        void setSize(int size);
        void setDensity(int density);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    private void setUpRecyclerView() {

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                //recyclerView.getRecycledViewPool().clear();
                colorAdapter.notifyDataSetChanged();
            }
        });
        //recyclerAdapterIngredient.notifyDataSetChanged();
    }

    private void recyclerInit() {
        arrayList = new ArrayList<>();

        colorAdapter = new ColorAdapter(getLayoutInflater(),arrayList,this,getActivity());
        LayoutManager gridLayoutManager = new LayoutManager(getContext(), 10, LinearLayoutManager.VERTICAL, false);

        // StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(gridLayoutManager);


        recyclerView.setAdapter(colorAdapter);
        recyclerView.clearAnimation();

    }

    @Override
    public void OnnoteClick(MyColor userClass) {
        userClass.setSelected(!userClass.isSelected());


        if(userClass.isSelected()){
            selectedColors.add(userClass);
        }
        else{
            selectedColors.remove(userClass);
        }

        tinydb.putListObject("colors", selectedColors);

        colorAdapter.notifyDataSetChanged();



    }


}
