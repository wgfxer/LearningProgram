package com.wgfxer.learningprogram.presentation.presenter;

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

    public void loadDataAsync(final String lectorName, final boolean groupByWeeks) {
        if (learningProgramListWeakReference.get() != null) {
            learningProgramListWeakReference.get().showLoad();
        }

        LearningProgramProvider.OnLoadingFinishedListener onLoadingFinishedListener = new LearningProgramProvider.OnLoadingFinishedListener() {
            @Override
            public void onLoadingFinished(List<Lecture> lectures) {
                if (learningProgramListWeakReference.get() != null) {
                    learningProgramListWeakReference.get().showLectures(provider.filterBy(lectorName, groupByWeeks));
                    if (lectorName == null) {
                        learningProgramListWeakReference.get().scrollToLecture(provider.getNumberOfNextLecture());
                    }
                    learningProgramListWeakReference.get().showLectors(provider.provideLectors());
                    learningProgramListWeakReference.get().hideLoad();
                }
            }
        };
        provider.loadLectures(onLoadingFinishedListener);
    }


    public void detachView() {
        learningProgramListWeakReference.clear();
    }

}
