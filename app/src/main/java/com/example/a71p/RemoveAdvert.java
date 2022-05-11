package com.example.a71p;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.a71p.model.LostArticle;

public class RemoveAdvert extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Integer advertPos;

    public RemoveAdvert() {
    }

    public static RemoveAdvert newInstance(String param1, String param2) {
        RemoveAdvert fragment = new RemoveAdvert();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remove_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textItem = view.findViewById(R.id.textItem);
        TextView textTime = view.findViewById(R.id.textTime);
        TextView textLocation = view.findViewById(R.id.textLocation);
        Button removeButton = view.findViewById(R.id.removeButton);
        Bundle args = getArguments();
        this.advertPos = args.getInt("key",0);
        LostArticle thisAdvert = ((MainActivity)getActivity()).dbGetArticle(this.advertPos);
        textItem.setText(thisAdvert.Condition + " " + thisAdvert.Name);
        textTime.setText(thisAdvert.Date);
        textLocation.setText(thisAdvert.Location);
        removeButton.setTag(this.advertPos);
    }


}