package ru.eugen.noteseugen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentNotesList extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_name_notes, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListNotesName(view);
    }

    private void initListNotesName(View view) {
        LinearLayout lv = (LinearLayout)view;
        String[] notes = getResources().getStringArray(R.array.notes);
        for (int i = 0; i < notes.length; i++) {
            TextView nt = new TextView(getContext());
            nt.setText(notes[i]);
            nt.setTextSize(22);
            lv.addView(nt);
            final int fi = i;
            nt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(fi);
                }
            });

        }
    }

    private void showFragment(int index) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), PortretActivity.class);
        intent.putExtra(FragmentNote.INDEX, index);
        startActivity(intent);
    }
}