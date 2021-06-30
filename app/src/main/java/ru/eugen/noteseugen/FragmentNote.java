package ru.eugen.noteseugen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentNote extends Fragment {
    public static final String INDEX = "index";
    private int index;

    public FragmentNote() {
        // Required empty public constructor
    }

    public static FragmentNote newInstance(int index) {
        FragmentNote fragment = new FragmentNote();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        TextView tv = view.findViewById(R.id.note);
        String[] notes = getResources().getStringArray(R.array.notes);
        tv.setText(notes[index]);
        tv.setTextSize(22);
        return view;
    }
}