package com.wgfxer.learningprogram.models;

public class Lecture {
    private final String number;
    private final String date;
    private final String theme;
    private final String lector;

    public Lecture(String number, String date, String theme, String lector) {
        this.number = number;
        this.date = date;
        this.theme = theme;
        this.lector = lector;
    }

    public String getNumber() {
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
}
