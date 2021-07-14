package ru.eugen.noteseugen.data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.eugen.noteseugen.R;

public class CardsSourceImpl implements CardsSource {
    private List<Card> dataSource;
    private Resources resources;


    public CardsSourceImpl(Resources resources) {
        dataSource = new ArrayList<>(5);
        this.resources = resources;
    }

    public CardsSourceImpl init(){
        String[] notes = resources.getStringArray(R.array.notes);
        String[] date = resources.getStringArray(R.array.date);
        String[] essence = resources.getStringArray(R.array.essence);

        for (int i = 0; i < notes.length; i++) {
            dataSource.add(new Card(notes[i], date[i], essence[i], Calendar.getInstance().getTime()));
        }
        return this;
    }

    @Override
    public Card getCard(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }

    @Override
    public void deleteCard(int position) {
        dataSource.remove(position);
    }

    @Override
    public void updateCard(int position, Card card) {
        dataSource.set(position, card);
    }

    @Override
    public void addCard(Card card) {
        dataSource.add(card);
    }

    @Override
    public void clearCard() {
        dataSource.clear();
    }
}
