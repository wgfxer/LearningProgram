package com.wgfxer.learningprogram.presentation.presenter;

import com.wgfxer.learningprogram.data.model.Lecture;
import com.wgfxer.learningprogram.data.provider.LearningProgramProvider;
import com.wgfxer.learningprogram.presentation.view.ILearningProgramList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LearningProgramListPresenterTest {

    @Mock
    private ILearningProgramList iLearningProgramList;

    @Mock
    private LearningProgramProvider provider;

    private LearningProgramListPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new LearningProgramListPresenter(iLearningProgramList, provider);
    }

    @Test
    public void loadLecturesTest() {
        when(provider.getLectures()).thenReturn(getTestLectures());
        presenter.loadLectures();
        verify(iLearningProgramList).showLectures(getTestLectures());
    }


    @Test
    public void loadLecturesByNameTest() {
        when(provider.filterBy("Кузнецов")).thenReturn(getLecturesWithOneLector());
        presenter.loadLectures("Кузнецов");
        verify(iLearningProgramList).showLectures(getLecturesWithOneLector());
    }

    @Test
    public void loadLectorsTest() {
        when(provider.provideLectors()).thenReturn(getTestLectors());
        presenter.loadLectors();
        verify(iLearningProgramList).showLectors(getTestLectors());
    }

    @Test
    public void scrollToNextLectureTest() {
        when(provider.getNumberOfNextLecture()).thenReturn(5);
        presenter.scrollToNextLecture();
        verify(iLearningProgramList).scrollToLecture(5);
    }

    @Test
    public void detachViewTest() {
        presenter.detachView();
        verifyNoMoreInteractions(iLearningProgramList);
        verifyNoMoreInteractions(provider);
    }

    private List<Lecture> getTestLectures() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(0, "15.01.2019", "Mockito", "Кузнецов", new String[]{"Теория Mockito", "Практика Mockito"}));
        lectures.add(new Lecture(1, "17.01.2019", "RxJava", "Семенов", new String[]{"Теория RxJava", "Практика RxJava"}));
        lectures.add(new Lecture(2, "19.01.2019", "Retrofit", "Кузнецов", new String[]{"Теория Retrofit", "Практика Retrofit"}));
        return lectures;
    }

    private List<Lecture> getLecturesWithOneLector() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(new Lecture(0, "15.01.2019", "Mockito", "Кузнецов", new String[]{"Теория Mockito", "Практика Mockito"}));
        lectures.add(new Lecture(2, "19.01.2019", "Retrofit", "Кузнецов", new String[]{"Теория Retrofit", "Практика Retrofit"}));
        return lectures;
    }

    private List<String> getTestLectors() {
        List<String> lectors = new ArrayList<>();
        lectors.add("Кузнецов");
        lectors.add("Лунев");
        lectors.add("Аникин");
        return lectors;
    }
}