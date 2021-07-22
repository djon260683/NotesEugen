package ru.eugen.noteseugen.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import ru.eugen.noteseugen.MainActivity;
import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.data.Card;
import ru.eugen.noteseugen.observe.Publisher;


public class CardDialogFragment extends DialogFragment {
    private String action;
    private static final String ARG_ACTION = "action";
    private static final String ARG_CARD_DATA = "Param_Card";
    private Card card;
    private TextInputEditText note;
    private TextInputEditText essence;
    private DatePicker datePicker;
//    private Publisher publisher;

    public CardDialogFragment() {

    }
    public static CardDialogFragment newInstance() {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTION, "add");
        fragment.setArguments(args);
        return fragment;
    }
    public static CardDialogFragment newInstance(Card card) {
        CardDialogFragment fragment = new CardDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_DATA, card);
        args.putString(ARG_ACTION, "update");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            card = getArguments().getParcelable(ARG_CARD_DATA);
            action = getArguments().getString(ARG_ACTION);
        }
    }
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        MainActivity activity = (MainActivity) context;
//        publisher = activity.getPublisher();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_dialog, null);
        view.findViewById(R.id.positiveButton).setOnClickListener(listenerPositive);
        view.findViewById(R.id.negativeButton).setOnClickListener(listenerNegative);
        setCancelable(false);
        initView(view);
        if (card != null) {
            populateView();
        }
        return view;
    }
    private View.OnClickListener listenerPositive = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            card = collectCard();
            FragmentNotesList fnl = (FragmentNotesList) requireFragmentManager().getFragments().get(0);
            fnl.onDialogResult(action, card);
//            ((MainActivity) requireActivity()).replaceFragmentList();
            dismiss();
        }
    };
    private View.OnClickListener listenerNegative = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    };

//    @Override
//    public void onDetach() {
//        publisher = null;
//        super.onDetach();
//    }
//    @Override
//    public void onStop() {
//        card = collectCard();
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        publisher.notifySingle(card);
//        super.onDestroy();
//    }

    private void populateView() {
        note.setText(card.getNote());
        essence.setText(card.getEssence());
        initDatePicker(card.getDate());
    }

    private void initView(View view) {
        note = view.findViewById(R.id.inputNote);
        essence = view.findViewById(R.id.inputEssense);
        datePicker = view.findViewById(R.id.inputDate);
    }

    private void initDatePicker(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
    }

    private Card collectCard() {
        String note = this.note.getText().toString();
        String essence = this.essence.getText().toString();
        Date date = getDateFromDatePicker();
        if (card != null){
            Card answer = new Card(note, essence, date);
            answer.setId(card.getId());
            return answer;
        }else{
            return new Card(note, essence, date);
        }
    }

    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }
}