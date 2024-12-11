package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private static final String COLUMN_DIFFICULTY = "difficulty";

    private static final int TOTAL_QUESTIONS = 30;
    private static final int EASY_QUESTIONS = 10;
    private static final int MEDIUM_QUESTIONS = 10;
    private static final int HARD_QUESTIONS = 10;

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
                COLUMN_CORRECT + " TEXT, " +
                COLUMN_DIFFICULTY + " TEXT NOT NULL);";
        db.execSQL(createTable);
        insertQuestions(db);
    }

    private void insertQuestions(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_QUESTIONS + " (" + COLUMN_QUESTION + ", " +
                COLUMN_ANSWER1 + ", " + COLUMN_ANSWER2 + ", " + COLUMN_ANSWER3 + ", " +
                COLUMN_ANSWER4 + ", " + COLUMN_CORRECT + ", " + COLUMN_DIFFICULTY + ") VALUES " +
                "('Qual é a capital de Portugal?', 'Lisboa', 'Porto', 'Faro', 'Coimbra', 'Lisboa', 'fácil')," +
                "('Quantos continentes existem?', '5', '6', '7', '8', '7', 'fácil')," +
                "('Qual é o maior planeta do sistema solar?', 'Terra', 'Marte', 'Júpiter', 'Saturno', 'Júpiter', 'fácil')," +
                "('Qual é o menor país do mundo?', 'Vaticano', 'Mônaco', 'San Marino', 'Andorra', 'Vaticano', 'fácil')," +
                "('Qual é a língua oficial do Brasil?', 'Português', 'Espanhol', 'Inglês', 'Francês', 'Português', 'fácil')," +
                "('Qual é o maior oceano do mundo?', 'Pacífico', 'Atlântico', 'Índico', 'Ártico', 'Pacífico', 'intermediário')," +
                "('Qual é o elemento químico mais abundante no universo?', 'Hidrogênio', 'Oxigênio', 'Carbono', 'Hélio', 'Hidrogênio', 'intermediário')," +
                "('Quem foi o primeiro presidente do Brasil?', 'Deodoro da Fonseca', 'Getúlio Vargas', 'Juscelino Kubitschek', 'Luiz Inácio Lula da Silva', 'Deodoro da Fonseca', 'intermediário')," +
                "('Qual é o teorema de Pitágoras?', 'a^2 + b^2 = c^2', 'a^2 - b^2 = c^2', 'a^2 * b^2 = c^2', 'a^2 / b^2 = c^2', 'a^2 + b^2 = c^2', 'difícil')," +
                "('Qual é a fórmula da área de um círculo?', 'π * r^2', '2 * π * r', 'π * d^2', '4/3 * π * r^3', 'π * r^2', 'difícil')," +
                "('Quem foi o primeiro homem a pisar na lua?', 'Neil Armstrong', 'Buzz Aldrin', 'Michael Collins', 'Yuri Gagarin', 'Neil Armstrong', 'fácil')," +
                "('Qual é o nome do símbolo do elemento químico ouro?', 'Au', 'Ag', 'Cu', 'Fe', 'Au', 'fácil')," +
                "('Qual é o nome do filósofo grego que criou a teoria dos quatro elementos?', 'Platão', 'Aristóteles', 'Sócrates', 'Tales de Mileto', 'Aristóteles', 'intermediário')," +
                "('Qual é o nome da equação que descreve a velocidade de uma reação química?', 'Equação de Arrhenius', 'Equação de Clausius-Clapeyron', 'Equação de Nernst', 'Equação de Fick', 'Equação de Arrhenius', 'difícil')," +
                "('Qual é o nome do processo químico em que uma molécula de água é decomposta em hidrogênio e oxigênio?', 'Eletrólise', 'Fotossíntese', 'Combustão', 'Hidrólise', 'Eletrólise', 'difícil');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    // Verifica se a resposta fornecida está correta
    public boolean isAnswerCorrect(int questionId, String userAnswer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_CORRECT + " FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_ID + " = ?",
                new String[]{String.valueOf(questionId)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            try {
                String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT));
                return correctAnswer.equalsIgnoreCase(userAnswer);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

    // Obtém 15 perguntas aleatórias, distribuídas em 5 fáceis, 5 intermédias e 5 difíceis
    public List<Question> getRandomQuestions() {
        List<Question> questions = new ArrayList<>();

        // Obtém 10 perguntas fáceis aleatórias
        questions.addAll(getQuestionsByDifficulty("fácil", EASY_QUESTIONS));

        // Obtém 10 perguntas intermediárias aleatórias
        questions.addAll(getQuestionsByDifficulty("intermediário", MEDIUM_QUESTIONS));

        // Obtém 10 perguntas difíceis aleatórias
        questions.addAll(getQuestionsByDifficulty("difícil", HARD_QUESTIONS));

        // Embaralha as perguntas e seleciona as 15 primeiras
        Collections.shuffle(questions);

        return questions.subList(0, Math.min(15, questions.size()));
    }

    // Método auxiliar para obter perguntas por dificuldade
    private List<Question> getQuestionsByDifficulty(String difficulty, int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_DIFFICULTY + " = ? ORDER BY RANDOM() LIMIT ?",
                new String[]{difficulty, String.valueOf(limit)}
        );

        // Cria uma nova lista para armazenar as perguntas
        List<Question> questionList = new ArrayList<>();

        // Verifica se o cursor não é nulo e se contém ao menos um registro
        if (cursor != null && cursor.moveToFirst()) {
            // Faz um loop pelos registros retornados pelo cursor
            do {
                // Cria um novo objeto Question com os dados da linha atual do cursor
                Question question = new Question(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)), // Obtém o ID da pergunta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)), // Obtém o texto da pergunta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER1)), // Obtém a primeira alternativa de resposta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER2)), // Obtém a segunda alternativa de resposta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER3)), // Obtém a terceira alternativa de resposta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER4)), // Obtém a quarta alternativa de resposta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CORRECT)), // Obtém a resposta correta
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)) // Obtém o nível de dificuldade da pergunta
                );
                // Adiciona o objeto Question à lista de perguntas
                questionList.add(question);
            } while (cursor.moveToNext()); // Continua para o próximo registro enquanto houver registros

            // Fecha o cursor para liberar recursos
            cursor.close();
        }

        // Retorna a lista de perguntas extraídas do cursor
        return questionList;

    }

    // Classe auxiliar para representar perguntas
    public class Question {
        private int id;
        private String question;
        private String answer1;
        private String answer2;
        private String answer3;
        private String answer4;
        private String correct;
        private String difficulty;

        public Question(int id, String question, String answer1, String answer2, String answer3, String answer4, String correct, String difficulty) {
            this.id = id;
            this.question = question;
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer4;
            this.correct = correct;
            this.difficulty = difficulty;
        }

    }


    private Cursor getRandomQuestionsByDifficulty(String difficulty, int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_DIFFICULTY + " = ? ORDER BY RANDOM() LIMIT ?",
                new String[]{difficulty, String.valueOf(limit)});
    }

    // Obtém uma pergunta pelo ID
    public Cursor getQuestion(int questionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_ID + " = ?",
                new String[]{String.valueOf(questionId)});
    }
}