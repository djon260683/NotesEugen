package ru.eugen.noteseugen.data;

public interface CardsSource {
    CardsSource init(CardsSourceResponse cardsSourceResponse);
    Card getCard(int position);
    int size();
    void deleteCard(int position);
    void updateCard(int position, Card card);
    void addCard(Card card);
    void clearCard();
}
