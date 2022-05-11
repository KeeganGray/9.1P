package com.example.a71p;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.a71p.model.LostArticle;

import java.util.ArrayList;
import java.util.List;

public class LostnFound extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ListView listview;

    public LostnFound() {
        // Required empty public constructor
    }

    public static LostnFound newInstance(String param1, String param2) {
        LostnFound fragment = new LostnFound();
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
        return inflater.inflate(R.layout.lostnfound, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview = view.findViewById(R.id.listview);
        listViewManage();
        listview.setClickable(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                ((MainActivity)getActivity()).selectArticleFragment(position+1);
            }
        });
    }

    public void listViewManage(){

        List<LostArticle> table = ((MainActivity)getActivity()).dbGet();
        List<String> articleNames = new ArrayList<>();
        for (int i =0 ; i < table.size(); i++){
            LostArticle x = table.get(i);
            articleNames.add(x.Name);
        }

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, articleNames);
        listview.setAdapter(listViewAdapter);
    }
}