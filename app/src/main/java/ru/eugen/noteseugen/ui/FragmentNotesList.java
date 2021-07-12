package ru.eugen.noteseugen.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.eugen.noteseugen.data.CardsSource;
import ru.eugen.noteseugen.data.CardsSourceImpl;
import ru.eugen.noteseugen.data.Note;
import ru.eugen.noteseugen.R;

public class FragmentNotesList extends Fragment {
    public static final String NOTE = "NOTE";
    private Note note;
    private boolean isLandscape;
    public static boolean isInstance;

    public static FragmentNotesList newInstance() {
        FragmentNotesList fragment = new FragmentNotesList();
        isInstance = false;
        return fragment;
    }

    public static FragmentNotesList newInstanceNote(Note note) {
        FragmentNotesList fragment = new FragmentNotesList();
        Bundle args = new Bundle();
        args.putParcelable(NOTE, note);
        fragment.setArguments(args);
        isInstance = true;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        CardsSource notes = new CardsSourceImpl(getResources()).init();
        initRecyclerView(recyclerView, notes);
        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, CardsSource notes) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        NotesAdapter adapter = new NotesAdapter(notes);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
        recyclerView.addItemDecoration(itemDecoration);

        adapter.SetOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                note = new Note(getResources().getStringArray(R.array.notes)[position], position);
                showFragment(note);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            note = getArguments().getParcelable(NOTE);
        }
        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(NOTE);
        }
        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape==true && note == null) {
            clearLandFragment();
        }
        if (isLandscape==true && note != null && isInstance == false) {
            showLandFragment(note);
        }
        if (isLandscape==false && note != null) {
            showPortFragment(note);
        }
    }

    private void clearLandFragment() {
        FragmentLandNote details = FragmentLandNote.newInstanceClear();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NOTE, note);
        super.onSaveInstanceState(outState);
    }

    private void showFragment(Note indexNote) {
        if (isLandscape) {
            showLandFragment(indexNote);
        } else {
            showPortFragment(indexNote);
        }
    }

    private void showLandFragment(Note note) {
        FragmentLandNote details = FragmentLandNote.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    private void showPortFragment(Note note) {
        FragmentPortNote details = FragmentPortNote.newInstance(note);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.notes, details);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}