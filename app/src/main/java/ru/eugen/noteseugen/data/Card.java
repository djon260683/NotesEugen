package ru.eugen.noteseugen.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Card implements Parcelable {
    private String note;
    private String dateS;
    private String essence;
    private Date date;


    public Card(String note, String essence, Date date) {
        this.note = note;
        this.essence = essence;
        this.date = date;
    }

    protected Card(Parcel in) {
        note = in.readString();
        essence = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.dateS = date;
    }

    public String getEssence() {
        return essence;
    }

    public void setEssence(String essence) {
        this.essence = essence;
    }
    public Date getDate() {
        return date;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(note);
        dest.writeString(dateS);
        dest.writeString(essence);
        dest.writeLong(date.getTime());
    }
}
