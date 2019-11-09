package com.wgfxer.learningprogram.presentation.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgfxer.learningprogram.R;
import com.wgfxer.learningprogram.data.model.Lecture;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LearningProgramAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int WEEK_VIEW_TYPE = 0;
    private static final int LECTURE_VIEW_TYPE = 1;

    private List<Object> listItems;

    private OnLectureClickListener onLectureClickListener;

    interface OnLectureClickListener {
        void onLectureClick(Lecture lecture);
    }

    void setOnLectureClickListener(OnLectureClickListener onLectureClickListener) {
        this.onLectureClickListener = onLectureClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == WEEK_VIEW_TYPE) {
            return new WeekViewHolder(
                    inflater.inflate(R.layout.item_week, parent, false)
            );
        } else {
            return new LectureViewHolder(
                    inflater.inflate(R.layout.item_lecture, parent, false),
                    onLectureClickListener
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object listItem = listItems.get(position);
        if (holder.getItemViewType() == WEEK_VIEW_TYPE) {
            WeekViewHolder weekViewHolder = (WeekViewHolder) holder;
            weekViewHolder.bind(listItem.toString());
        } else if (holder.getItemViewType() == LECTURE_VIEW_TYPE) {
            Lecture lecture = ((Lecture) listItem);
            LectureViewHolder lectureViewHolder = (LectureViewHolder) holder;
            lectureViewHolder.bind(lecture);
        }
    }

    @Override
    public int getItemCount() {
        return listItems == null ? 0 : listItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems.get(position) instanceof Lecture) {
            return LECTURE_VIEW_TYPE;
        } else {
            return WEEK_VIEW_TYPE;
        }
    }

    void setLectures(List<Object> lectures) {
        listItems = lectures;
        notifyDataSetChanged();
    }

    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        private final TextView number;
        private final TextView date;
        private final TextView theme;
        private final TextView lector;
        private OnLectureClickListener onLectureClickListener;

        LectureViewHolder(@NonNull View itemView, OnLectureClickListener onLectureClickListener) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            date = itemView.findViewById(R.id.date);
            theme = itemView.findViewById(R.id.theme);
            lector = itemView.findViewById(R.id.lector);
            this.onLectureClickListener = onLectureClickListener;
        }

        void bind(final Lecture lecture) {
            number.setText(String.valueOf(lecture.getNumber()));
            date.setText(lecture.getDate());
            theme.setText(lecture.getTheme());
            lector.setText(lecture.getLector());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onLectureClickListener != null) {
                        onLectureClickListener.onLectureClick(lecture);
                    }
                }
            });
        }
    }

    public static class WeekViewHolder extends RecyclerView.ViewHolder {
        private final TextView weekTextView;

        WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            weekTextView = itemView.findViewById(R.id.text);
        }

        void bind(String week) {
            weekTextView.setText(week);
        }
    }


}
