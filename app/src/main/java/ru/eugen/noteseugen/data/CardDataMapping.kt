package ru.eugen.noteseugen.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

class CardDataMapping {
    object Fields{
        const val DATE:String  = "date"
        const val NOTE: String  = "note"
        const val ESSENCE: String  = "essence"
    }

    companion object{

        @JvmStatic
        fun toCard(id2:String , doc:MutableMap<String, Any> ) :Card {
            val timeStamp: Timestamp  = doc.get(Fields.DATE) as Timestamp
            val card: Card = Card(doc.get(Fields.NOTE) as String,
                    doc.get(Fields.ESSENCE) as String,
                    timeStamp.toDate());
            card.id = id2
            return card
        }
        @JvmStatic
        fun  toDocument(card:Card) : MutableMap<String, Any> {
            val doc:MutableMap<String, Any>  = HashMap()
            doc.put(Fields.NOTE, card.note)
            doc.put(Fields.ESSENCE, card.essence)
            doc.put(Fields.DATE, card.date)
            return doc;
        }
    }


}
