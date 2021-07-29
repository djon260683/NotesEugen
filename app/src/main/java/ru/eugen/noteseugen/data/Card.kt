package ru.eugen.noteseugen.data

import android.os.Parcel
import android.os.Parcelable

import java.util.Date

class Card : Parcelable {
    var id:String
    var note:String
    var essence:String
    var date:Date

    constructor(note:String, essence:String, date:Date ) {
        this.note = note
        this.essence = essence
        this.date = date
        this.id = ""
    }

    constructor (in2:Parcel) {
        id = in2.readString()?:""
        note = in2.readString()?:""
        essence = in2.readString()?:""
        date = Date(in2.readLong())
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(note)
        dest.writeString(essence)
        dest.writeLong(date.getTime())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(in2: Parcel): Card {
            return Card(in2)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}
