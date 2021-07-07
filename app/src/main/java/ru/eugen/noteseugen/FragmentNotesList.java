package ru.eugen.noteseugen;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentNotesList extends Fragment {
    public static final String INDEX = "INDEX";
    private Note indexNote;


    private boolean isLandscape;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListNotesName(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            indexNote = savedInstanceState.getParcelable(INDEX);
        } else {
            indexNote = new Note(getResources().getStringArray(R.array.notes)[0], 0);
        }
        if (isLandscape) {
            showLandFragment(indexNote);
        }
    }

    private void initListNotesName(View view) {
        LinearLayout lv = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.notes);
        for (int i = 0; i < notes.length; i++) {
            TextView nt = new TextView(getContext());
            nt.setText(notes[i]);
            nt.setTextSize(22);
            lv.addView(nt);
            final int fi = i;
            nt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //initPopupMenu(view);// здесь должно быть контекстное меню
                    indexNote = new Note(getResources().getStringArray(R.array.notes)[fi], fi);
                    showFragment(indexNote);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(INDEX, indexNote);
        super.onSaveInstanceState(outState);
    }

    private void showFragment(Note indexNote) {
        if (isLandscape) {
            showLandFragment(indexNote);
        } else {
            showPortFragment(indexNote);
        }
    }

    private void showLandFragment(Note indexNote) {
        FragmentNote details = FragmentNote.newInstance(indexNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortFragment(Note indexNote) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PortretActivity.class);
        intent.putExtra(FragmentNote.NOTE, indexNote);
        startActivity(intent);
    }
}