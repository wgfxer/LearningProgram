package com.wgfxer.learningprogram.presentation.view;

import java.util.List;

public interface ILearningProgramList {

    void showLectures(List<Object> lectures);

    void showLectors(List<String> lectors);

    void scrollToLecture(int position);

    void showLoad();

    void hideLoad();

}
