package com.wgfxer.learningprogram.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
    private RecyclerView recyclerView;

    private LearningProgramListHolder learningProgramListHolder;

    private Spinner spinnerLectors;

    private LearningProgramListPresenter presenter;

    private boolean groupByWeeks = false;
    private String lectorName;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof LearningProgramListHolder)
            learningProgramListHolder = (LearningProgramListHolder) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_learning_program_list, container, false);
        presenter = new LearningProgramListPresenter(this, new LearningProgramProvider());
        initViews();
        presenter.loadLectures();
        presenter.loadLectors();
        presenter.scrollToNextLecture();  //почему-то после этого все равно возвращается на 0 позицию,если не использовать хендлер
        return view;
    }

    private void initViews() {
        recyclerView = view.findViewById(R.id.learning_program_recycler);
        spinnerLectors = view.findViewById(R.id.lectors_spinner);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        final CheckBox groupByWeeksCheckBox = view.findViewById(R.id.group_by_weeks);
        groupByWeeksCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupByWeeks = groupByWeeksCheckBox.isChecked();
                if (lectorName == null) {
                    presenter.loadLectures();
                } else {
                    presenter.loadLectures(lectorName);
                }
            }
        });
    }

    @Override
    public void onLectureClick(Lecture lecture) {
        if (learningProgramListHolder != null) {
            learningProgramListHolder.onLectureClick(lecture);
        }
    }

    @Override
    public void showLectures(List<Lecture> lectures) {
        LearningProgramAdapter adapter = new LearningProgramAdapter();
        adapter.setOnLectureClickListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setLectures(lectures, groupByWeeks);
    }

    @Override
    public void showLectors(List<String> lectors) {
        lectors.add(POSITION_ALL, getResources().getString(R.string.all));
        LectorSpinnerAdapter spinnerAdapter = new LectorSpinnerAdapter(lectors);
        spinnerLectors.setAdapter(spinnerAdapter);
        spinnerLectors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == POSITION_ALL) {
                    lectorName = null;
                    presenter.loadLectures();
                } else {
                    lectorName = spinnerLectors.getSelectedItem().toString();
                    presenter.loadLectures(lectorName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void scrollToLecture(final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(position);
            }
        }, 200);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
