package ru.eugen.noteseugen.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.eugen.noteseugen.R;

public class DialogCustomFragment extends Fragment {


    public DialogCustomFragment() {

    }

    public static DialogCustomFragment newInstance(String param1, String param2) {
        DialogCustomFragment fragment = new DialogCustomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_custom, container, false);
    }
}