package com.wgfxer.learningprogram;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wgfxer.learningprogram.models.Lecture;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.wgfxer.learningprogram.LearningProgramAdapter.SORT_BY_WEEKS;
import static com.wgfxer.learningprogram.LearningProgramAdapter.WITHOUT_SORT;

public class LearningProgramListFragment extends Fragment implements LearningProgramAdapter.OnLectureClickListener {
    private View view;

    private LearningProgramListHolder learningProgramListHolder;

    private static final int POSITION_ALL = 0;

    private LearningProgramProvider provider = new LearningProgramProvider();
    private LearningProgramAdapter adapter;


    private Spinner spinnerLectors;
    private int currentModeOfGroup = 0;
    private List<Lecture> lectures;


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
        initRecyclerView();
        initSpinnerLectors();
        initSpinnerWeeks();
        return view;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.learning_program_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LearningProgramAdapter();
        adapter.setOnLectureClickListener(this);
        recyclerView.setAdapter(adapter);
        lectures = provider.getUpdatedLectures();
        adapter.setLectures(lectures, WITHOUT_SORT);
        recyclerView.scrollToPosition(provider.getNumberOfNextLecture());
    }

    private void initSpinnerLectors() {
        spinnerLectors = view.findViewById(R.id.lectors_spinner);
        final List<String> lectors = provider.provideLectors();
        Collections.sort(lectors);
        lectors.add(POSITION_ALL, getResources().getString(R.string.all));
        LectorSpinnerAdapter spinnerAdapter = new LectorSpinnerAdapter(lectors);
        spinnerLectors.setAdapter(spinnerAdapter);
        spinnerLectors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == POSITION_ALL) {
                    lectures = provider.getLectures();
                } else {
                    String lectorName = spinnerLectors.getSelectedItem().toString();
                    lectures = provider.filterBy(lectorName);
                }
                adapter.setLectures(lectures, currentModeOfGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpinnerWeeks() {
        Spinner spinner = view.findViewById(R.id.spinner_weeks);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.weeksVariants));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case WITHOUT_SORT: {
                        currentModeOfGroup = 0;
                        break;
                    }
                    case SORT_BY_WEEKS: {
                        currentModeOfGroup = 1;
                        break;
                    }
                }
                adapter.setLectures(lectures, currentModeOfGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onLectureClick(Lecture lecture) {
        if (learningProgramListHolder != null) {
            learningProgramListHolder.onLectureClick(lecture);
            Log.i("MYTAG", "LISTENER2");
        }
    }
}
