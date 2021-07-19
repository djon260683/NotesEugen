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

    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = store.collection(CARDS_COLLECTION);

    private List<Card> cards = new ArrayList<Card>();

    @Override
    public CardSourceFirebaseImpl init(final CardsSourceResponse cardsSourceResponse) {
        collectionReference.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        cards = new ArrayList<Card>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                Card card = CardDataMapping.toCard(id, doc);
                                cards.add(card);
                            }
                            cardsSourceResponse.initialized(CardSourceFirebaseImpl.this);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Log", "onFailure ", e);
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
//        Log.d("Log", "size");
        return cards.size();
    }

    @Override
    public void deleteCard(int position) {
        collectionReference.document(cards.get(position).getId()).delete();
        cards.remove(position);
        Log.d("Log", "deleteCard");
    }

    @Override
    public void updateCard(int position, Card card) {
        String id = card.getId();
        collectionReference.document(id).set(CardDataMapping.toDocument(card));
        Log.d("Log", "updateCard");
    }

    @Override
    public void addCard(Card card) {
        Log.d("Log", "begin addCard");
        collectionReference.add(CardDataMapping.toDocument(card)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                card.setId(documentReference.getId());
                Log.d("Log", "onSuccess addCard");
            }
        });
//        collectionReference.add(CardDataMapping.toDocument(card));
//        cards.add(card);
        Log.d("Log", "end addCard");
    }

    @Override
    public void clearCard() {
        for (Card card : cards) {
            collectionReference.document(card.getId()).delete();
        }
        cards = new ArrayList<Card>();
        Log.d("Log", "clearCard");
    }
}
