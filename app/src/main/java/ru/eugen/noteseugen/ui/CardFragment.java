package ru.eugen.noteseugen.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;

import ru.eugen.noteseugen.MainActivity;
import ru.eugen.noteseugen.R;
import ru.eugen.noteseugen.data.Card;
import ru.eugen.noteseugen.observe.Publisher;

public class CardFragment extends Fragment {

    private static final String ARG_CARD_DATA = "Param_Card";
    private Card card;
    private Publisher publisher;
    private TextInputEditText note;
    private TextInputEditText essence;
    private DatePicker datePicker;

    public CardFragment() {

    }

    public static CardFragment newInstance() {
        CardFragment fragment = new CardFragment();
        return fragment;
    }

    public static CardFragment newInstance(Card card) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CARD_DATA, card);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            card = getArguments().getParcelable(ARG_CARD_DATA);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initView(view);
        if (card != null) {
            populateView();
        }
        return view;
    }

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

    @Override
    public void onStop() {
        card = collectCard();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        publisher.notifySingle(card);
        super.onDestroy();
    }

    private Card collectCard() {
        String note = this.note.getText().toString();
        String essence = this.essence.getText().toString();
        Date date = getDateFromDatePicker();
        if (card != null){
            Card answer;
            answer = new Card(note, essence, date);
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