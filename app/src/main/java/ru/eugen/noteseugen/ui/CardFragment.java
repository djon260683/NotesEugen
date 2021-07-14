package ru.eugen.noteseugen.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        initView(view);
        if (card != null) {
            populateView();
        }
        return view;
    }
    private void initView(View view) {
        note = view.findViewById(R.id.inputTitle);
        essence = view.findViewById(R.id.inputDescription);
        datePicker = view.findViewById(R.id.inputDate);
    }
    private void populateView(){
        note.setText(card.getNote());
        essence.setText(card.getEssence());
        initDatePicker(card.getDate());
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
        super.onStop();
        card = collectCard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(card);
    }

    private Card collectCard(){
        String note = this.note.getText().toString();
        String essence = this.essence.getText().toString();
        Date date = getDateFromDatePicker();
        int picture;
        boolean like;
        if (card != null){
//            picture = card.getPicture();
//            like = card.isLike();
        } else {
//            picture = R.drawable.nature1;
//            like = false;
        }
        return new Card(note, essence, essence, date);
    }

    private Date getDateFromDatePicker() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.datePicker.getYear());
        cal.set(Calendar.MONTH, this.datePicker.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, this.datePicker.getDayOfMonth());
        return cal.getTime();
    }
}