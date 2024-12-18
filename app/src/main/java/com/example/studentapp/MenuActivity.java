package com.example.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnSelectSubjects, btnViewSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnSelectSubjects = findViewById(R.id.btn_select_subjects);
        btnViewSummary = findViewById(R.id.btn_view_summary);

        btnSelectSubjects.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SubjectSelectionActivity.class);
            startActivity(intent);
        });

        btnViewSummary.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, SummaryActivity.class);
            startActivity(intent);
        });
    }
}
