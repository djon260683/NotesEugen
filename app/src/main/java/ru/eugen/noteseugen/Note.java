package ru.eugen.noteseugen;

import android.os.Parcel;
import android.os.Parcelable;


public class Note implements Parcelable {
    private String name;
    private int index;

    public Note(String name, int index) {
        this.name = name;
        this.index = index;
    }

    protected Note(Parcel in) {
        index = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getIndex());
        dest.writeString(getName());
    }


    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
