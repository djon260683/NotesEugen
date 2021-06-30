package ru.eugen.noteseugen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Notes implements Parcelable {
    private ArrayList<Note> notesArrayList;

    protected Notes(Parcel in) {
        notesArrayList = new ArrayList<>();
        in.readTypedList(notesArrayList, Note.CREATOR);
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
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
