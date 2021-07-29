package ru.eugen.noteseugen.data

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.*

class CardSourceFirebaseImpl : CardsSource {
    val CARDS_COLLECTION: String = "cards"

    val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    val collectionReference: CollectionReference = store.collection(CARDS_COLLECTION)

    var cards: MutableList<Card> = ArrayList()

    override fun init(cardsSourceResponse: CardsSourceResponse): CardsSource {
        collectionReference.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful())
                                cards = ArrayList()

                            for (document in task.result!!) {
                                val doc: MutableMap<String, Any> = document.data
                                val id: String = document.id
                                val card: Card = CardDataMapping.toCard(id, doc)
                                cards.add(card)
                            }
                            cardsSourceResponse.initialized(this)
                        })
        return this
    }

    override fun getCard(position: Int): Card {
        return cards.get(position)
    }

    override fun size(): Int {
        if (cards == null) {
            return 0
        }
        return cards.size
    }

    override fun deleteCard(position: Int) {
        collectionReference.document(cards.get(position).id).delete()
        cards.removeAt(position)
    }

    override fun updateCard(position: Int, card: Card) {
        val id: String  = card.id
        collectionReference.document(id).set(CardDataMapping.toDocument(card))
    }

    override fun addCard(card: Card) {
        collectionReference.add(CardDataMapping.toDocument(card)).addOnSuccessListener(OnSuccessListener < DocumentReference >() {
            fun onSuccess(documentReference:DocumentReference) {
                card.id=(documentReference.id)
            }
        });
    }

    override fun clearCard() {
        for (card:Card in cards) {
            collectionReference.document(card.id).delete()
        }
        cards = ArrayList()
    }
}
