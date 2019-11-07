package com.wgfxer.learningprogram.presentation.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wgfxer.learningprogram.R;
import com.wgfxer.learningprogram.data.model.Lecture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LectureInfoFragment extends Fragment {
    private static final String LECTURE_KEY = "LECTURE";
    private Lecture lecture;

    static LectureInfoFragment newInstance(Lecture lecture) {
        Bundle args = new Bundle();
        args.putParcelable(LECTURE_KEY, lecture);
        LectureInfoFragment fragment = new LectureInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecture_info, container, false);
        lecture = getArguments().getParcelable(LECTURE_KEY);
        TextView numberTextView = view.findViewById(R.id.numberTextView);
        TextView lectorTextView = view.findViewById(R.id.lectorTextView);
        TextView themeTextView = view.findViewById(R.id.themeTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        ListView subtopicsListView = view.findViewById(R.id.subtopicsListView);
        numberTextView.setText(String.valueOf(lecture.getNumber()));
        themeTextView.setText(lecture.getTheme());
        lectorTextView.setText(lecture.getLector());
        dateTextView.setText(lecture.getDate());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, lecture.getSubtopics());
        subtopicsListView.setAdapter(adapter);
        return view;
    }
}
