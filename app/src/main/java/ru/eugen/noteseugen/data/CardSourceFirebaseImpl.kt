package ru.eugen.noteseugen.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CardSourceFirebaseImpl : CardsSource {
    val CARDS_COLLECTION: String = "cards"

    val store: FirebaseFirestore = FirebaseFirestore.getInstance()
    val collectionReference: CollectionReference = store.collection(CARDS_COLLECTION)

    var cards: MutableList<Card> = ArrayList()

//    @Override
//    public CardSourceFirebaseImpl init(final CardsSourceResponse cardsSourceResponse) {
//        collectionReference.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        cards = new ArrayList<Card>();
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Map<String, Object> doc = document.getData();
//                                String id = document.getId();
//                                Card card = CardDataMapping.toCard(id, doc);
//                                cards.add(card);
//                            }
//                            cardsSourceResponse.initialized(CardSourceFirebaseImpl.this);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Log", "onFailure ", e);
//                    }
//                });
//        return this;
//    }
//
//    @Override
//    public Card getCard(int position) {
//        return cards.get(position);
//    }
//
//    @Override
//    public int size() {
//        if (cards == null) {
//            return 0;
//        }
////        Log.d("Log", "size");
//        return cards.size();
//    }
//
//    @Override
//    public void deleteCard(int position) {
//        collectionReference.document(cards.get(position).getId()).delete();
//        cards.remove(position);
//        Log.d("Log", "deleteCard");
//    }
//
//    @Override
//    public void updateCard(int position, Card card) {
//        String id = card.getId();
//        collectionReference.document(id).set(CardDataMapping.toDocument(card));
//        Log.d("Log", "updateCard");
//    }
//
//    @Override
//    public void addCard(Card card) {
//        Log.d("Log", "begin addCard");
//        collectionReference.add(CardDataMapping.toDocument(card)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                card.setId(documentReference.getId());
//                Log.d("Log", "onSuccess addCard");
//            }
//        });
////        collectionReference.add(CardDataMapping.toDocument(card));
////        cards.add(card);
//        Log.d("Log", "end addCard");
//    }
//
//    @Override
//    public void clearCard() {
//        for (Card card : cards) {
//            collectionReference.document(card.getId()).delete();
//        }
//        cards = new ArrayList<Card>();
//        Log.d("Log", "clearCard");
//    }

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
