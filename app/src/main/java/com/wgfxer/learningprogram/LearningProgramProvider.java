package com.wgfxer.learningprogram;

import android.os.AsyncTask;
import android.util.Log;

import com.wgfxer.learningprogram.models.Lecture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

class LearningProgramProvider {
    private static final String URL = "https://landsovet.ru/learning_program.json";
    private List<Lecture> lectures = new ArrayList<>();
    private static final String NUMBER_KEY = "number";
    private static final String DATE_KEY = "date";
    private static final String THEME_KEY = "theme";
    private static final String LECTOR_KEY = "lector";
    private static final String SUBTOPICS_KEY = "subtopics";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    List<Lecture> getUpdatedLectures() {
        List<Lecture> lecturesFromJSON = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONLoadTask().execute().get();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectLecture = jsonArray.getJSONObject(i);
                    int number = jsonObjectLecture.getInt(NUMBER_KEY);
                    String date = jsonObjectLecture.getString(DATE_KEY);
                    String theme = jsonObjectLecture.getString(THEME_KEY);
                    String lector = jsonObjectLecture.getString(LECTOR_KEY);
                    JSONArray subtopicsJSONArray = jsonObjectLecture.getJSONArray(SUBTOPICS_KEY);
                    String[] subtopics = new String[subtopicsJSONArray.length()];
                    for (int j = 0; j < subtopicsJSONArray.length(); j++) {
                        subtopics[j] = subtopicsJSONArray.getString(j);
                    }
                    Lecture lecture = new Lecture(number, date, theme, lector, subtopics);
                    lecturesFromJSON.add(lecture);
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!lecturesFromJSON.isEmpty()) lectures = lecturesFromJSON;
        return lectures;
    }

    List<Lecture> getLectures() {
        return lectures;
    }

    int getNumberOfNextLecture() {
        int positionResult = 0;
        for (Lecture lecture : lectures) {
            if (isLecturePassed(lecture)) positionResult++;
        }
        return positionResult;
    }

    private boolean isLecturePassed(Lecture lecture) {
        String lectureDateString = lecture.getDate();
        Date lectureDate = null;
        try {
            lectureDate = dateFormat.parse(lectureDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendarLecture = Calendar.getInstance();
        calendarLecture.setTime(lectureDate);
        switch (calendarLecture.get(Calendar.DAY_OF_WEEK) - 1) {
            case 2:
            case 4:
                calendarLecture.set(Calendar.HOUR_OF_DAY, 18);
                calendarLecture.set(Calendar.MINUTE, 30);
                break;
            case 6:
                calendarLecture.set(Calendar.HOUR_OF_DAY, 13);
                break;
        }
        calendarLecture.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        Calendar calendarNow = Calendar.getInstance();
        calendarNow.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        Log.i("MYTAG", lecture.getTheme() + " " + calendarLecture.after(calendarNow));
        return calendarLecture.before(calendarNow);
    }

    List<String> provideLectors() {
        Set<String> lectorsSet = new HashSet<>();
        for (Lecture lecture : lectures) {
            lectorsSet.add(lecture.getLector());
        }
        return new ArrayList<>(lectorsSet);
    }

    List<Lecture> filterBy(String lectorName) {
        List<Lecture> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (lecture.getLector().equals(lectorName)) {
                result.add(lecture);
            }
        }
        return result;
    }

    private static class JSONLoadTask extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            try {
                java.net.URL url = new URL(URL);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                return new JSONArray(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }
    }
}
