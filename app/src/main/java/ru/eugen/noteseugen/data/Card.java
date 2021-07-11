package ru.eugen.noteseugen.data;

public class Card {
    private String note;
    private String date;
    private String essence;

    public Card(String note, String date, String essence) {
        this.note = note;
        this.date = date;
        this.essence = essence;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEssence() {
        return essence;
    }

    public void setEssence(String essence) {
        this.essence = essence;
    }
}
