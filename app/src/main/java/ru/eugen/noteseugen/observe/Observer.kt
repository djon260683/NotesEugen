package ru.eugen.noteseugen.observe

import ru.eugen.noteseugen.data.Card

interface Observer {
    fun updateCard(card:Card)
}
