package com.wgfxer.learningprogram.data.provider;

import android.os.AsyncTask;

import com.wgfxer.learningprogram.data.model.Lecture;

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

public class LearningProgramProvider {
    private static final String URL = "https://landsovet.ru/learning_program.json";
    private List<Lecture> lectures;
    private static final String NUMBER_KEY = "number";
    private static final String DATE_KEY = "date";
    private static final String THEME_KEY = "theme";
    private static final String LECTOR_KEY = "lector";
    private static final String SUBTOPICS_KEY = "subtopics";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");


    public void loadLectures(OnLoadingFinishedListener listener) {
        LoadLecturesTask task = new LoadLecturesTask(listener);
        task.execute();
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public List<Object> filterBy(String lectorName, boolean groupByWeeks) {
        List<Lecture> result = new ArrayList<>();
        if (lectorName != null) {
            for (Lecture lecture : lectures) {
                if (lecture.getLector().equals(lectorName)) {
                    result.add(lecture);
                }
            }
        } else {
            result = lectures;
        }
        return getListWithWeeks(result, groupByWeeks);
    }

    List<Object> getListWithWeeks(List<Lecture> lectures, boolean groupByWeeks) {
        List<Object> listObjects;
        if (!groupByWeeks) {
            listObjects = new ArrayList<>();
            listObjects.addAll(lectures);
        } else {
            listObjects = addWeeksInLectures(lectures);
        }
        return listObjects;
    }

    public int getNumberOfNextLecture() {
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
        return calendarLecture.before(calendarNow);
    }

    public List<String> provideLectors() {
        Set<String> lectorsSet = new HashSet<>();
        for (Lecture lecture : lectures) {
            lectorsSet.add(lecture.getLector());
        }
        return new ArrayList<>(lectorsSet);
    }

    public List<Object> addWeeksInLectures(List<Lecture> lectures) {
        List<Object> items = new ArrayList<>();
        items.addAll(lectures);
        int i = 0;
        while (i < items.size()) {
            Lecture lecture = (Lecture) items.get(i);
            int numberOfWeek = (lecture.getNumber() - 1) / 3 + 1;
            String textForWeek = "Неделя " + numberOfWeek;
            if (!items.contains(textForWeek)) items.add(i, "Неделя " + numberOfWeek);
            i += 2;
        }
        return items;
    }


    private class LoadLecturesTask extends AsyncTask<Void, Void, List<Lecture>> {
        private final OnLoadingFinishedListener listener;

        LoadLecturesTask(OnLoadingFinishedListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Lecture> doInBackground(Void... voids) {
            return getLecturesFromJSON(getJSONFromNetwork());
        }

        @Override
        protected void onPostExecute(List<Lecture> result) {
            super.onPostExecute(result);
            lectures = result;
            listener.onLoadingFinished(result);
        }
    }

    private JSONArray getJSONFromNetwork() {
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

    private List<Lecture> getLecturesFromJSON(JSONArray jsonArray) {
        List<Lecture> lecturesFromJSON = new ArrayList<>();
        try {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lecturesFromJSON;
    }

    public interface OnLoadingFinishedListener {
        void onLoadingFinished(List<Lecture> lectures);
    }
}
