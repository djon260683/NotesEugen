package ru.eugen.noteseugen.data;

public interface CardsSource {
    Card getCard(int position);
    int size();
}
