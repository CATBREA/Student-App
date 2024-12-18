package com.example.studentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT = "students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_SUBJECTS = "subjects";
    public static final String COLUMN_SUBJECT_ID = "subject_id";
    public static final String COLUMN_SUBJECT_NAME = "subject_name";
    public static final String COLUMN_CREDIT = "credit";

    public static final String TABLE_ENROLLMENT = "enrollment";
    public static final String COLUMN_ENROLLMENT_ID = "enrollment_id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_ENROLLED_SUBJECTS = "enrolled_subjects";
    public static final String COLUMN_TOTAL_CREDITS = "total_credits";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_STUDENT + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_SUBJECTS + " (" +
                COLUMN_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SUBJECT_NAME + " TEXT, " +
                COLUMN_CREDIT + " INTEGER);");

        db.execSQL("CREATE TABLE " + TABLE_ENROLLMENT + " (" +
                COLUMN_ENROLLMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_ID + " INTEGER, " +
                COLUMN_ENROLLED_SUBJECTS + " TEXT, " +
                COLUMN_TOTAL_CREDITS + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENT);
        onCreate(db);
    }

    public long registerStudent(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_STUDENT, null, values);
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENT +
                        " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void insertSubject(String subjectName, int credit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT_NAME, subjectName);
        values.put(COLUMN_CREDIT, credit);
        db.insert(TABLE_SUBJECTS, null, values);
    }

    public Cursor getSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SUBJECTS, null);
    }

    public long enrollSubjects(int studentId, String enrolledSubjects, int totalCredits) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_ENROLLED_SUBJECTS, enrolledSubjects);
        values.put(COLUMN_TOTAL_CREDITS, totalCredits);
        return db.insert(TABLE_ENROLLMENT, null, values);
    }
}
