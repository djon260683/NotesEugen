package ru.eugen.noteseugen.data;

interface CardsSource {
    fun init(cardsSourceResponse:CardsSourceResponse):CardsSource
    fun getCard(position:Int):Card
    fun size():Int
    fun deleteCard(position:Int):Unit
    fun updateCard(position:Int, card:Card):Unit
    fun addCard(card:Card):Unit
    fun clearCard():Unit
}
