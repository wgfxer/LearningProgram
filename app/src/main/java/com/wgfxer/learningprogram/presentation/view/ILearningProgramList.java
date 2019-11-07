package com.wgfxer.learningprogram.presentation.view;

import com.wgfxer.learningprogram.data.model.Lecture;

import java.util.List;

public interface ILearningProgramList {

    void showLectures(List<Lecture> lectures);

    void showLectors(List<String> lectors);

    void scrollToLecture(int position);
}
