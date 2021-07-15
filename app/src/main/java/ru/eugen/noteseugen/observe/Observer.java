package ru.eugen.noteseugen.observe;

import ru.eugen.noteseugen.data.Card;

public interface Observer {
    void updateCard(Card card);
}
