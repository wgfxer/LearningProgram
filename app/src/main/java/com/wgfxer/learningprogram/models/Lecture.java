package com.wgfxer.learningprogram.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Lecture implements Parcelable {
    private final int number;
    private final String date;
    private final String theme;
    private final String lector;
    private final String[] subtopics;

    public Lecture(int number, String date, String theme, String lector, String[] subtopics) {
        this.number = number;
        this.date = date;
        this.theme = theme;
        this.lector = lector;
        this.subtopics = subtopics;
    }

    protected Lecture(Parcel in) {
        number = in.readInt();
        date = in.readString();
        theme = in.readString();
        lector = in.readString();
        subtopics = in.createStringArray();
    }

    public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public String getTheme() {
        return theme;
    }

    public String getLector() {
        return lector;
    }

    public String[] getSubtopics() {
        return subtopics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeString(date);
        parcel.writeString(theme);
        parcel.writeString(lector);
        parcel.writeStringArray(subtopics);
    }
}
