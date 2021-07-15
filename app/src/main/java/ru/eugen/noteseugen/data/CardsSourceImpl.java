package ru.eugen.noteseugen.data;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.eugen.noteseugen.R;

public class CardsSourceImpl implements CardsSource {
    private List<Card> cardsSource;
    private Resources resources;

    public CardsSourceImpl(Resources resources) {
        cardsSource = new ArrayList<>(5);
        this.resources = resources;
    }

    public CardsSourceImpl init(){
        String[] notes = resources.getStringArray(R.array.notes);
        String[] essence = resources.getStringArray(R.array.essence);
        for (int i = 0; i < notes.length; i++) {
            cardsSource.add(new Card(notes[i], essence[i], Calendar.getInstance().getTime()));
        }
        Log.d("Log", "CardsSourceImpl");
        return this;
    }

    @Override
    public Card getCard(int position) {
        return cardsSource.get(position);
    }

    @Override
    public int size() {
        return cardsSource.size();
    }

    @Override
    public void deleteCard(int position) {
        cardsSource.remove(position);
    }

    @Override
    public void updateCard(int position, Card card) {
        cardsSource.set(position, card);
    }

    @Override
    public void addCard(Card card) {
        cardsSource.add(card);
    }

    @Override
    public void clearCard() {
        cardsSource.clear();
    }
}
