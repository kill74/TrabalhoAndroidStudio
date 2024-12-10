package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PerguntasDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "perguntas.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_QUESTIONS = "questions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_ANSWER1 = "answer1";
    private static final String COLUMN_ANSWER2 = "answer2";
    private static final String COLUMN_ANSWER3 = "answer3";
    private static final String COLUMN_ANSWER4 = "answer4";
    private static final String COLUMN_CORRECT = "correct";

    public PerguntasDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT NOT NULL, " +
                COLUMN_ANSWER1 + " TEXT, " +
                COLUMN_ANSWER2 + " TEXT, " +
                COLUMN_ANSWER3 + " TEXT, " +
                COLUMN_ANSWER4 + " TEXT, " +
                COLUMN_CORRECT + " TEXT);";
        db.execSQL(createTable);

        // Inserir 6 perguntas
        db.execSQL("INSERT INTO " + TABLE_QUESTIONS + " (" + COLUMN_QUESTION + ", " +
                COLUMN_ANSWER1 + ", " + COLUMN_ANSWER2 + ", " + COLUMN_ANSWER3 + ", " +
                COLUMN_ANSWER4 + ", " + COLUMN_CORRECT + ") VALUES " +
                "('Qual é a capital de Portugal?', 'Lisboa', 'Porto', 'Faro', 'Coimbra', 'Lisboa')," +
                "('Quantos continentes existem?', '5', '6', '7', '8', '7')," +
                "('Qual é o maior planeta do sistema solar?', 'Terra', 'Marte', 'Júpiter', 'Saturno', 'Júpiter')," +
                "('Qual é o menor país do mundo?', 'Vaticano', 'Mônaco', 'San Marino', 'Andorra', 'Vaticano')," +
                "('Quem escreveu Hamlet?', 'Shakespeare', 'Dante', 'Homer', 'Chaucer', 'Shakespeare')," +
                "('Qual é o elemento químico representado por O?', 'Ouro', 'Osmium', 'Oxygen', 'Oxigênio', 'Oxygen');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    public Cursor getQuestion(int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_ID + " = ?",
                new String[]{String.valueOf(questionId)});
    }
}
