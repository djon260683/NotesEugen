package ru.eugen.noteseugen.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.eugen.noteseugen.data.Note;
import ru.eugen.noteseugen.R;

public class FragmentLandNote extends Fragment {
    public static final String NOTE = "NOTE";
    private Note note;

    public FragmentLandNote() {
        // Required empty public constructor
    }

    public static FragmentLandNote newInstanceClear() {
        FragmentLandNote fragment = new FragmentLandNote();
        return fragment;
    }


    public static FragmentLandNote newInstance(Note note) {
        FragmentLandNote fragment = new FragmentLandNote();
        Bundle args = new Bundle();
        args.putParcelable(NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_land_note, container, false);
        TextView tv_note = view.findViewById(R.id.note);
        initPopupMenu(view, tv_note);
        TextView tv_date = view.findViewById(R.id.date);
        initPopupMenu(view, tv_date);
        TextView tv_essence = view.findViewById(R.id.essence);
        initPopupMenu(view, tv_essence);

        String[] notes = getResources().getStringArray(R.array.notes);
        String[] notesEssence = getResources().getStringArray(R.array.essence);

        if (note != null) {
            tv_note.setText(notes[note.getIndex()]);
            tv_note.setTextSize(18);
            tv_date.setTextSize(14);
            tv_essence.setText(notesEssence[note.getIndex()]);
            tv_essence.setTextSize(22);

            if (FragmentNotesList.isInstance == false) {
                FragmentNotesList details = FragmentNotesList.newInstanceNote(note);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.notes, details);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
                FragmentNotesList.isInstance = true;
            }
        }
        return view;
    }

    private void initPopupMenu(View view, TextView text) {
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = requireActivity();
                PopupMenu popupMenu = new PopupMenu(activity, view);
                activity.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                Menu menu = popupMenu.getMenu();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_delete:
                                Toast.makeText(getContext(), "Выбрано удалить во всплывающем меню", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }
}