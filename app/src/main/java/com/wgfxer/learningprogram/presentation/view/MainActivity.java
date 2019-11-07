package com.wgfxer.learningprogram.presentation.view;

import android.os.Bundle;

import com.wgfxer.learningprogram.R;
import com.wgfxer.learningprogram.data.model.Lecture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements LearningProgramListHolder {

    FragmentManager fragmentManager;
    LearningProgramListFragment learningProgramListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        learningProgramListFragment = new LearningProgramListFragment();
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.main_activity_frame) == null) {
            fragmentManager.beginTransaction().replace(R.id.main_activity_frame, learningProgramListFragment).commit();
        }
    }

    @Override
    public void onLectureClick(Lecture lecture) {
        LectureInfoFragment lectureInfoFragment = LectureInfoFragment.newInstance(lecture);
        fragmentManager.beginTransaction().replace(R.id.main_activity_frame, lectureInfoFragment).addToBackStack(null).commit();
    }
}

