package ru.eugen.noteseugen.observe

import ru.eugen.noteseugen.data.Card

class Publisher {
    val observers: MutableList<Observer>

    constructor() {
        observers = ArrayList()
    }

    fun subscribe(observer:Observer):Unit {
        observers.add(observer)
    }

    fun unsubscribe(observer:Observer):Unit {
        observers.remove(observer)
    }

    fun notifySingle(card: Card):Unit {
        for (observer:Observer in observers) {
            observer.updateCard(card)
            unsubscribe(observer)
        }
    }
}
