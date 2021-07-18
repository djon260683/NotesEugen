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

public class CardSourceFirebaseImpl implements CardsSource {
    private static final String CARDS_COLLECTION = "cards";
    private static final String TAG = "[CardsSourceFirebaseImpl]";

    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private CollectionReference collection = store.collection(CARDS_COLLECTION);
    private List<Card> cards = new ArrayList<Card>();

    @Override
    public CardsSource init(final CardsSourceResponse cardsSourceResponse) {
        collection.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cards = new ArrayList<Card>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                Card card = CardDataMapping.toCard(id, doc);
                                cards.add(card);
                            }
                            Log.d(TAG, "success " + cards.size() + " qnt");
                            cardsSourceResponse.initialized(CardsSourceFirebaseImpl.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public Card getCard(int position) {
        return cards.get(position);
    }

    @Override
    public int size() {
        if (cards == null) {
            return 0;
        }
        return cards.size();
    }

    @Override
    public void deleteCard(int position) {
        collection.document(cards.get(position).getId()).delete();
        cards.remove(position);
    }

    @Override
    public void updateCard(int position, Card card) {
        String id = card.getId();
        collection.document(id).set(CardDataMapping.toDocument(card));
    }

    @Override
    public void addCard(Card card) {
        collection.add(CardDataMapping.toDocument(card)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                card.setId(documentReference.getId());
            }
        });
    }

    @Override
    public void clearCard() {
        for (Card card : cards) {
            collection.document(card.getId()).delete();
        }
        cards = new ArrayList<Card>();
    }
}
