package com.sanket.generativeart.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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

//
        //add more colors
        addColorInList("#FADBD8");
        addColorInList("#F5B7B1");
        addColorInList("#F1948A");
        addColorInList("#EC7063");
        addColorInList("#E74C3C");
        addColorInList("#CB4335");
        addColorInList("#B03A2E");
        addColorInList("#943126");
        addColorInList("#78281F");

        addColorInList("#E8DAEF");
        addColorInList("#D7BDE2");
        addColorInList("#C39BD3");
        addColorInList("#AF7AC5");
        addColorInList("#9B59B6");
        addColorInList("#884EA0");
        addColorInList("#76448A");
        addColorInList("#633974");
        addColorInList("#512E5F");


        addColorInList("#D4E6F1");
        addColorInList("#A9CCE3");
        addColorInList("#7FB3D5");
        addColorInList("#5499C7");
        addColorInList("#2980B9");
        addColorInList("#2471A3");
        addColorInList("#1F618D");
        addColorInList("#1A5276");
        addColorInList("#154360");

        addColorInList("#D5F5E3");
        addColorInList("#ABEBC6");
        addColorInList("#82E0AA");
        addColorInList("#58D68D");
        addColorInList("#2ECC71");
        addColorInList("#28B463");
        addColorInList("#239B56");
        addColorInList("#1D8348");
        addColorInList("#186A3B");

        addColorInList("#FCF3CF");
        addColorInList("#F9E79F");
        addColorInList("#F7DC6F");
        addColorInList("#F4D03F");
        addColorInList("#F1C40F");
        addColorInList("#D4AC0D");
        addColorInList("#B7950B");
        addColorInList("#9A7D0A");
        addColorInList("#7D6608");

        addColorInList("#FAE5D3");
        addColorInList("#F5CBA7");
        addColorInList("#F0B27A");
        addColorInList("#EB984E");
        addColorInList("#E67E22");
        addColorInList("#CA6F1E");
        addColorInList("#AF601A");
        addColorInList("#935116");
        addColorInList("#784212");

        addColorInList("#F2F3F4");
        addColorInList("#E5E7E9");
        addColorInList("#D7DBDD");
        addColorInList("#CACFD2");
        addColorInList("#BDC3C7");
        addColorInList("#A6ACAF");
        addColorInList("#909497");
        addColorInList("#797D7F");
        addColorInList("#626567");

        addColorInList("#D6DBDF");
        addColorInList("#AEB6BF");
        addColorInList("#85929E");
        addColorInList("#5D6D7E");
        addColorInList("#34495E");
        addColorInList("#2E4053");
        addColorInList("#283747");
        addColorInList("#212F3C");
        addColorInList("#1B2631");


        addColorInList("#ffffff");
        addColorInList("#000000");


        setUpRecyclerView();

        return  v;
    }

    public void addColorInList(String color){
//        color = "#"+color;
        int col = Color.parseColor(color);
        MyColor myColor = new MyColor(col, false);
        arrayList.add(myColor);
    }

    public interface BottomSheetListener {
        void setSize(int size);
        void setDensity(int density);
        void dismiss(boolean dismiss);
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
        LayoutManager gridLayoutManager = new LayoutManager(getContext(), 9, LinearLayoutManager.VERTICAL, false);

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

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        mListener.dismiss(true);
    }
}
