package com.wgfxer.learningprogram.presentation.presenter;

import android.util.Log;

import com.wgfxer.learningprogram.data.model.Lecture;
import com.wgfxer.learningprogram.data.provider.LearningProgramProvider;
import com.wgfxer.learningprogram.presentation.view.ILearningProgramList;

import java.lang.ref.WeakReference;
import java.util.List;

public class LearningProgramListPresenter {

    private WeakReference<ILearningProgramList> learningProgramListWeakReference;
    private LearningProgramProvider provider;


    public LearningProgramListPresenter(ILearningProgramList learningProgramList, LearningProgramProvider provider) {
        learningProgramListWeakReference = new WeakReference<>(learningProgramList);
        this.provider = provider;
    }

    public void loadLectures() {
        List<Lecture> lectures = provider.getLectures();
        if (learningProgramListWeakReference.get() != null) {
            learningProgramListWeakReference.get().showLectures(lectures);
        }
    }

    public void loadLectures(String lectorName) {
        List<Lecture> lectures = provider.filterBy(lectorName);
        if (learningProgramListWeakReference.get() != null) {
            learningProgramListWeakReference.get().showLectures(lectures);
        }
    }

    public void loadLectors() {
        List<String> lectors = provider.provideLectors();
        if (learningProgramListWeakReference.get() != null) {
            learningProgramListWeakReference.get().showLectors(lectors);
        }
    }

    public void scrollToNextLecture() {
        if (learningProgramListWeakReference.get() != null) {
            learningProgramListWeakReference.get().scrollToLecture(provider.getNumberOfNextLecture());
        }
    }

    public void detachView() {
        learningProgramListWeakReference.clear();
    }
}
