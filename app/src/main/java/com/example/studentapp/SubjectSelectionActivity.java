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
    private final int CREDIT_LIMIT = 24; // 最大总学分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        lvSubjects = findViewById(R.id.lv_subjects);
        btnEnroll = findViewById(R.id.btn_enroll);
        dbHelper = new DatabaseHelper(this);

        // 向数据库中插入 5 个学科和对应的学分（仅在首次运行时）
        insertDefaultSubjects();

        // 加载科目数据
        loadSubjects();

        btnEnroll.setOnClickListener(v -> {
            enrollSelectedSubjects();
        });
    }

    // 插入 5 个默认的科目
    private void insertDefaultSubjects() {
        if (dbHelper.getSubjects().getCount() == 0) {
            dbHelper.insertSubject("数学", 5);
            dbHelper.insertSubject("物理", 4);
            dbHelper.insertSubject("化学", 3);
            dbHelper.insertSubject("英语", 2);
            dbHelper.insertSubject("计算机科学", 6);
        }
    }

    // 加载科目到 ListView
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

        // 使用SimpleAdapter将数据绑定到ListView
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

    // 处理提交注册
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
            Toast.makeText(this, "总学分不能超过 " + CREDIT_LIMIT + " 分，当前为 " + totalCredits + " 分！", Toast.LENGTH_LONG).show();
        } else if (selectedSubjects.size() > 0) {
            String subjects = String.join(", ", selectedSubjects);
            long result = dbHelper.enrollSubjects(1, subjects, totalCredits); // 假设学生ID = 1

            if (result > 0) {
                Toast.makeText(this, "注册成功！总学分: " + totalCredits, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "注册失败，请重试！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请至少选择一门学科！", Toast.LENGTH_SHORT).show();
        }
    }
}
