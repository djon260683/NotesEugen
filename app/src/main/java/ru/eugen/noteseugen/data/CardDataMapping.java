package ru.eugen.noteseugen.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class CardDataMapping {
    public static class Fields{
        public final static String DATE = "date";
        public final static String NOTE = "note";
        public final static String ESSENCE = "essence";
    }
    public static Card toCard(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp)doc.get(Fields.DATE);
        Card card = new Card((String) doc.get(Fields.NOTE),
                (String) doc.get(Fields.ESSENCE),
                timeStamp.toDate());
        card.setId(id);
        return card;
    }
    public static Map<String, Object> toDocument(Card card){
        Map<String, Object> doc = new HashMap<>();
        doc.put(Fields.NOTE, card.getNote());
        doc.put(Fields.ESSENCE, card.getEssence());
        doc.put(Fields.DATE, card.getDate());
        return doc;
    }
}
