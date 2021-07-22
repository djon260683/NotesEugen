package ru.eugen.noteseugen.ui;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.eugen.noteseugen.MainActivity;
import ru.eugen.noteseugen.R;

public class DialogCustomFragment extends DialogFragment {
    private String action;
    private static final String ARG_ACTION = "action";

    public DialogCustomFragment() {

    }

    public static DialogCustomFragment newInstance(String action) {
        DialogCustomFragment fragment = new DialogCustomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getString(ARG_ACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_custom, null);
        TextView txt = view.findViewById(R.id.textAction);
        if (action == "clear") {
            txt.setText("ДеЙствительно очистить?");
        }
        if (action == "delete") {
            txt.setText("ДеЙствительно удалить?");
        }
        view.findViewById(R.id.positiveButton).setOnClickListener(listenerPositive);
        view.findViewById(R.id.negativeButton).setOnClickListener(listenerNegative);
        setCancelable(false);
        return view;
    }

    private View.OnClickListener listenerPositive = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentNotesList fnl = (FragmentNotesList) requireFragmentManager().getFragments().get(0);
            fnl.onDialogResult(action);
            dismiss();
        }
    };
    private View.OnClickListener listenerNegative = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };
}