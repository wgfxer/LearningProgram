package com.wgfxer.learningprogram.presentation.presenter;

import com.wgfxer.learningprogram.data.model.Lecture;
import com.wgfxer.learningprogram.data.provider.LearningProgramProvider;
import com.wgfxer.learningprogram.presentation.view.ILearningProgramList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    public void loadDataAsyncTest() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                LearningProgramProvider.OnLoadingFinishedListener listener = (LearningProgramProvider.OnLoadingFinishedListener) invocation.getArguments()[0];
                listener.onLoadingFinished(getTestLectures());
                return null;
            }
        }).when(provider).loadLectures(any(LearningProgramProvider.OnLoadingFinishedListener.class));
        when(provider.filterBy(null, false)).thenReturn(getLecturesObjects());
        when(provider.getNumberOfNextLecture()).thenReturn(2);
        when(provider.provideLectors()).thenReturn(getTestLectors());

        presenter.loadDataAsync(null, false);

        InOrder inOrder = Mockito.inOrder(iLearningProgramList);
        inOrder.verify(iLearningProgramList).showLoad();
        inOrder.verify(iLearningProgramList).showLectures(provider.filterBy(null, false));
        inOrder.verify(iLearningProgramList).scrollToLecture(2);
        inOrder.verify(iLearningProgramList).showLectors(getTestLectors());
        inOrder.verify(iLearningProgramList).hideLoad();


        inOrder.verifyNoMoreInteractions();
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

    private List<Object> getLecturesObjects() {
        List<Object> lectures = new ArrayList<>();
        lectures.add(new Lecture(0, "15.01.2019", "Mockito", "Кузнецов", new String[]{"Теория Mockito", "Практика Mockito"}));
        lectures.add(new Lecture(1, "17.01.2019", "RxJava", "Семенов", new String[]{"Теория RxJava", "Практика RxJava"}));
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