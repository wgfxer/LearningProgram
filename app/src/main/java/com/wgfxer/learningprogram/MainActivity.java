package com.wgfxer.learningprogram;

import android.os.Bundle;

import com.wgfxer.learningprogram.models.Lecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements LearningProgramListHolder {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LearningProgramListFragment learningProgramListFragment = new LearningProgramListFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_activity_frame, learningProgramListFragment).commit();
    }

    @Override
    public void onLectureClick(Lecture lecture) {
        LectureInfoFragment lectureInfoFragment = LectureInfoFragment.newInstance(lecture);
        fragmentManager.beginTransaction().replace(R.id.main_activity_frame, lectureInfoFragment).addToBackStack(null).commit();
    }
}

