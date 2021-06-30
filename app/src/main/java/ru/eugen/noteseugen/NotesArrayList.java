package ru.eugen.noteseugen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class NotesArrayList implements Parcelable {
    private ArrayList<Note> notesArrayList;

    protected NotesArrayList(Parcel in) {
        notesArrayList = new ArrayList<>();
        in.readTypedList(notesArrayList, Note.CREATOR);
    }

    public static final Creator<NotesArrayList> CREATOR = new Creator<NotesArrayList>() {
        @Override
        public NotesArrayList createFromParcel(Parcel in) {
            return new NotesArrayList(in);
        }

        @Override
        public NotesArrayList[] newArray(int size) {
            return new NotesArrayList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(notesArrayList);
    }
}
