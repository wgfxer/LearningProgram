package com.wgfxer.learningprogram.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.wgfxer.learningprogram.R;
import com.wgfxer.learningprogram.data.model.Lecture;
import com.wgfxer.learningprogram.data.provider.LearningProgramProvider;
import com.wgfxer.learningprogram.presentation.presenter.LearningProgramListPresenter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class LearningProgramListFragment extends Fragment implements LearningProgramAdapter.OnLectureClickListener, ILearningProgramList {

    private static final int POSITION_ALL = 0;

    private View view;

    private LearningProgramListPresenter presenter;

    private RecyclerView recyclerView;
    private LearningProgramListHolder learningProgramListHolder;
    private LearningProgramAdapter learningProgramAdapter;

    private LectorSpinnerAdapter spinnerAdapter;

    private boolean groupByWeeks = false;
    private String lectorName;

    private FrameLayout progressBarFrame;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof LearningProgramListHolder)
            learningProgramListHolder = (LearningProgramListHolder) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        view = inflater.inflate(R.layout.fragment_learning_program_list_coordinator, container, false);
        presenter = new LearningProgramListPresenter(this, new LearningProgramProvider());
        initViews();
        presenter.loadDataAsync(lectorName, groupByWeeks);
        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.learning_program_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        learningProgramAdapter = new LearningProgramAdapter();
        learningProgramAdapter.setOnLectureClickListener(this);
        recyclerView.setAdapter(learningProgramAdapter);

        Spinner spinnerLectors = view.findViewById(R.id.lectors_spinner);
        if (spinnerAdapter == null) {                         //проверка, чтобы спиннерАдаптер не создавался заново, чтобы можно было проверить
            spinnerAdapter = new LectorSpinnerAdapter();    //равны ли загруженные лекции и те что в адаптере
        }
        spinnerLectors.setAdapter(spinnerAdapter);
        spinnerLectors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == POSITION_ALL) {
                    lectorName = null;
                } else {
                    lectorName = spinnerAdapter.getItem(i);
                }
                presenter.loadDataAsync(lectorName, groupByWeeks);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final CheckBox groupByWeeksCheckBox = view.findViewById(R.id.group_by_weeks);
        groupByWeeksCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupByWeeks = groupByWeeksCheckBox.isChecked();
                presenter.loadDataAsync(lectorName, groupByWeeks);
            }
        });

        progressBarFrame = view.findViewById(R.id.progress_bar_frame);
    }

    @Override
    public void onLectureClick(Lecture lecture) {
        if (learningProgramListHolder != null) {
            learningProgramListHolder.onLectureClick(lecture);
        }
    }

    @Override
    public void showLectures(List<Object> lectures) {
        learningProgramAdapter.setLectures(lectures);
    }

    @Override
    public void showLectors(@NonNull List<String> lectors) {
        lectors.add(POSITION_ALL, getResources().getString(R.string.all));
        if (!lectors.equals(spinnerAdapter.getLectors())) {  //если убрать проверку,то всегда будут ставиться лекторы спиннеры и у него будет вызываться
            spinnerAdapter.setLectors(lectors);            //onItemSelectedListener и lectorName будет присваиваться null и будет заново загрузка
        }
    }

    @Override
    public void scrollToLecture(int position) {
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void showLoad() {
        progressBarFrame.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoad() {
        progressBarFrame.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }


}
