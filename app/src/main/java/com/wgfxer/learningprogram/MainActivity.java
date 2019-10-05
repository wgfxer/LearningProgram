package com.wgfxer.learningprogram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wgfxer.learningprogram.models.Lecture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.wgfxer.learningprogram.LearningProgramAdapter.SORT_BY_WEEKS;
import static com.wgfxer.learningprogram.LearningProgramAdapter.WITHOUT_SORT;

public class MainActivity extends AppCompatActivity {
    private static final int POSITION_ALL = 0;

    private LearningProgramProvider provider = new LearningProgramProvider();
    private LearningProgramAdapter adapter;

    private Spinner spinnerLectors;
    private int currentModeOfGroup = 0;
    private List<Lecture> lectures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
        initSpinnerLectors();
        initSpinnerWeeks();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.learning_program_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LearningProgramAdapter();
        recyclerView.setAdapter(adapter);
        lectures = provider.provideLectures();
        adapter.setLectures(lectures, WITHOUT_SORT);
        recyclerView.scrollToPosition(getNumberOfNextLecture());
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);*/
    }

    private int getNumberOfNextLecture() {
        int positionResult = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for(Lecture lecture : lectures){
            try {
                Date lectureDate = format.parse(lecture.getDate());
                if(lectureDate.before(new Date())) positionResult++;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return positionResult;
    }

    private void initSpinnerLectors(){
        spinnerLectors = findViewById(R.id.lectors_spinner);
        final List<String> lectors = provider.provideLectors();
        Collections.sort(lectors);
        lectors.add(POSITION_ALL,getResources().getString(R.string.all));
        LectorSpinnerAdapter spinnerAdapter = new LectorSpinnerAdapter(lectors);
        spinnerLectors.setAdapter(spinnerAdapter);
        spinnerLectors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == POSITION_ALL){
                    lectures = provider.provideLectures();
                }else{
                    String lectorName = "";
                    lectorName = spinnerLectors.getSelectedItem().toString();
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
        Spinner spinner = findViewById(R.id.spinner_weeks);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.weeksVariants));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case WITHOUT_SORT:{
                        currentModeOfGroup = 0;
                        break;
                    }
                    case SORT_BY_WEEKS:{
                        currentModeOfGroup = 1;
                        break;
                    }
                }
                adapter.setLectures(lectures,currentModeOfGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}

