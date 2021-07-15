package ru.eugen.noteseugen.data;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.eugen.noteseugen.R;

public class CardsSourceImpl implements CardsSource, Parcelable {
    private List<Card> cardsSourceImpl;
    private Resources resources;

    public CardsSourceImpl(Resources resources) {
        cardsSourceImpl = new ArrayList<>(5);
        this.resources = resources;
    }

    protected CardsSourceImpl(Parcel in) {
        cardsSourceImpl = in.createTypedArrayList(Card.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cardsSourceImpl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CardsSourceImpl> CREATOR = new Creator<CardsSourceImpl>() {
        @Override
        public CardsSourceImpl createFromParcel(Parcel in) {
            return new CardsSourceImpl(in);
        }

        @Override
        public CardsSourceImpl[] newArray(int size) {
            return new CardsSourceImpl[size];
        }
    };

    public CardsSourceImpl init(){
        String[] notes = resources.getStringArray(R.array.notes);
        String[] essence = resources.getStringArray(R.array.essence);
        for (int i = 0; i < notes.length; i++) {
            cardsSourceImpl.add(new Card(notes[i], essence[i], Calendar.getInstance().getTime()));
        }
        return this;
    }

    @Override
    public Card getCard(int position) {
        return cardsSourceImpl.get(position);
    }

    @Override
    public int size() {
        return cardsSourceImpl.size();
    }

    @Override
    public void deleteCard(int position) {
        cardsSourceImpl.remove(position);
    }

    @Override
    public void updateCard(int position, Card card) {
        cardsSourceImpl.set(position, card);
    }

    @Override
    public void addCard(Card card) {
        cardsSourceImpl.add(card);
    }

    @Override
    public void clearCard() {
        cardsSourceImpl.clear();
    }
}
