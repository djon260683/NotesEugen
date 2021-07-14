package ru.eugen.noteseugen.observe;

import java.util.ArrayList;
import java.util.List;

import ru.eugen.noteseugen.data.Card;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }
    public void subscribe(Observer observer) {
        observers.add(observer);
    }
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }
    public void notifySingle(Card card) {
        for (Observer observer : observers) {
            observer.updateCard(card);
            unsubscribe(observer);
        }
    }
}
