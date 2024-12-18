package com.example.studentapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SubjectSelectionActivity extends AppCompatActivity {

    private ListView lvSubjects;
    private Button btnEnroll;
    private DatabaseHelper dbHelper;
    private ArrayList<HashMap<String, String>> subjectList;
    private int totalCredits = 0;
    private final int CREDIT_LIMIT = 24; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        lvSubjects = findViewById(R.id.lv_subjects);
        btnEnroll = findViewById(R.id.btn_enroll);
        dbHelper = new DatabaseHelper(this);

        insertDefaultSubjects();

        loadSubjects();

        btnEnroll.setOnClickListener(v -> {
            enrollSelectedSubjects();
        });
    }

    private void insertDefaultSubjects() {
        if (dbHelper.getSubjects().getCount() == 0) {
            dbHelper.insertSubject("Mathematics", 5);
            dbHelper.insertSubject("Physics", 4);
            dbHelper.insertSubject("Chemistry", 3);
            dbHelper.insertSubject("English", 2);
            dbHelper.insertSubject("Computer Science", 6);
        }
    }

    private void loadSubjects() {
        Cursor cursor = dbHelper.getSubjects();
        subjectList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> subject = new HashMap<>();
                subject.put("subject_name", cursor.getString(cursor.getColumnIndex("subject_name")));
                subject.put("credit", cursor.getString(cursor.getColumnIndex("credit")));
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                subjectList,
                android.R.layout.simple_list_item_multiple_choice,
                new String[]{"subject_name", "credit"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        lvSubjects.setAdapter(adapter);
        lvSubjects.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private void enrollSelectedSubjects() {
        ArrayList<String> selectedSubjects = new ArrayList<>();
        totalCredits = 0;

        for (int i = 0; i < lvSubjects.getCount(); i++) {
            if (lvSubjects.isItemChecked(i)) {
                String subjectName = subjectList.get(i).get("subject_name");
                int credit = Integer.parseInt(subjectList.get(i).get("credit"));
                selectedSubjects.add(subjectName);
                totalCredits += credit;
            }
        }

        if (totalCredits > CREDIT_LIMIT) {
            Toast.makeText(this, "The total number of credits cannot be exceeded " + CREDIT_LIMIT + " points, currently is " + totalCredits + " points！", Toast.LENGTH_LONG).show();
        } else if (selectedSubjects.size() > 0) {
            String subjects = String.join(", ", selectedSubjects);
            long result = dbHelper.enrollSubjects(1, subjects, totalCredits); 

            if (result > 0) {
                Toast.makeText(this, "Registration Successful! Total credits: " + totalCredits, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Registration failed, please try again！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select at least one subject！", Toast.LENGTH_SHORT).show();
        }
    }
}
