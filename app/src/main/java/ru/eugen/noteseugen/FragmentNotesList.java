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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.eugen.noteseugen.ui.NotesAdapter;

public class FragmentNotesList extends Fragment {
    public static final String INDEX = "INDEX";
    private Note indexNote;


    private boolean isLandscape;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        String[] notes = getResources().getStringArray(R.array.notes);
        initRecyclerView(recyclerView, notes);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, String[] notes) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        NotesAdapter adapter = new NotesAdapter(notes);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initListNotesName(view);
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

//    private void initListNotesName(View view) {
//        LinearLayout layoutView = (LinearLayout) view;
//        String[] notes = getResources().getStringArray(R.array.notes);
//
//        LayoutInflater ltInflater = getLayoutInflater();
//
//        for (int i = 0; i < notes.length; i++) {
//            View item = ltInflater.inflate(R.layout.item, layoutView, false);
//            TextView noteListItem = item.findViewById(R.id.textViewItem);
//            noteListItem.setText(notes[i]);
//            layoutView.addView(item);
//            final int fi = i;
//
//            noteListItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    indexNote = new Note(getResources().getStringArray(R.array.notes)[fi], fi);
//                    showFragment(indexNote);
//                }
//            });
//        }
//    }

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