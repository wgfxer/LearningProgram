package com.wgfxer.learningprogram.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

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

    private Lecture(Parcel in) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lecture lecture = (Lecture) o;

        if (number != lecture.number) return false;
        if (date != null ? !date.equals(lecture.date) : lecture.date != null) return false;
        if (theme != null ? !theme.equals(lecture.theme) : lecture.theme != null) return false;
        if (lector != null ? !lector.equals(lecture.lector) : lecture.lector != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(subtopics, lecture.subtopics);
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (theme != null ? theme.hashCode() : 0);
        result = 31 * result + (lector != null ? lector.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(subtopics);
        return result;
    }
}
