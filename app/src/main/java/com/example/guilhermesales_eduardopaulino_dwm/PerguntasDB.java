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
    private static final String COLUMN_DIFFICULTY = "difficulty";

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
                "('Qual é o elemento químico representado por O?', 'Ouro', 'Osmium', 'Oxygen', 'Oxigênio', 'Oxygen', 'fácil')," +
                "('Em que ano começou a Segunda Guerra Mundial?', '1939', '1945', '1914', '1921', '1939', 'fácil')," +
                "('Qual é o maior oceano do mundo?', 'Atlântico', 'Pacífico', 'Índico', 'Ártico', 'Pacífico', 'fácil')," +
                "('Qual é a fórmula da água?', 'H2O', 'CO2', 'O2', 'H2SO4', 'H2O', 'fácil')," +
                "('Qual é a capital da França?', 'Paris', 'Londres', 'Berlim', 'Roma', 'Paris', 'fácil')," +
                "('Quantas cores tem o arco-íris?', '5', '6', '7', '8', '7', 'fácil');");

        db.execSQL("INSERT INTO " + TABLE_QUESTIONS + " (" + COLUMN_QUESTION + ", " +
                COLUMN_ANSWER1 + ", " + COLUMN_ANSWER2 + ", " + COLUMN_ANSWER3 + ", " +
                COLUMN_ANSWER4 + ", " + COLUMN_CORRECT + ", " + COLUMN_DIFFICULTY + ") VALUES " +
                "('Quem escreveu Hamlet?', 'Shakespeare', 'Dante', 'Homer', 'Chaucer', 'Shakespeare', 'intermédio')," +
                "('Qual é a velocidade da luz no vácuo?', '300 km/s', '3000 km/s', '300.000 km/s', '3.000.000 km/s', '300.000 km/s', 'intermédio')," +
                "('Quem pintou a Mona Lisa?', 'Da Vinci', 'Picasso', 'Van Gogh', 'Michelangelo', 'Da Vinci', 'intermédio')," +
                "('Qual é o país mais populoso do mundo?', 'Índia', 'China', 'EUA', 'Rússia', 'China', 'intermédio')," +
                "('Qual é a capital da Austrália?', 'Sydney', 'Melbourne', 'Canberra', 'Brisbane', 'Canberra', 'intermédio')," +
                "('Qual é o maior deserto do mundo?', 'Saara', 'Gobi', 'Kalahari', 'Antártico', 'Antártico', 'intermédio')," +
                "('Quem é o autor de Dom Quixote?', 'Cervantes', 'Shakespeare', 'Dante', 'Machado de Assis', 'Cervantes', 'intermédio')," +
                "('Qual é o idioma oficial do Brasil?', 'Português', 'Espanhol', 'Inglês', 'Francês', 'Português', 'intermédio')," +
                "('Qual é o maior mamífero do mundo?', 'Elefante', 'Baleia Azul', 'Rinoceronte', 'Hipopótamo', 'Baleia Azul', 'intermédio')," +
                "('Em que continente fica o Egito?', 'Ásia', 'África', 'Europa', 'América', 'África', 'intermédio');");

        db.execSQL("INSERT INTO " + TABLE_QUESTIONS + " (" + COLUMN_QUESTION + ", " +
                COLUMN_ANSWER1 + ", " + COLUMN_ANSWER2 + ", " + COLUMN_ANSWER3 + ", " +
                COLUMN_ANSWER4 + ", " + COLUMN_CORRECT + ", " + COLUMN_DIFFICULTY + ") VALUES " +
                "('Qual é a distância da Terra ao Sol?', '149 milhões km', '93 milhões km', '1 milhão km', '450 milhões km', '149 milhões km', 'difícil')," +
                "('Quem desenvolveu a teoria da relatividade?', 'Newton', 'Einstein', 'Galileu', 'Bohr', 'Einstein', 'difícil')," +
                "('Qual é o elemento químico com o símbolo Au?', 'Ouro', 'Prata', 'Cobre', 'Ferro', 'Ouro', 'difícil')," +
                "('Qual é o livro mais vendido do mundo?', 'Bíblia', 'Alcorão', 'Dom Quixote', 'Harry Potter', 'Bíblia', 'difícil')," +
                "('Quantos ossos tem o corpo humano adulto?', '200', '206', '210', '212', '206', 'difícil')," +
                "('Quem pintou o teto da Capela Sistina?', 'Michelangelo', 'Da Vinci', 'Raphael', 'Caravaggio', 'Michelangelo', 'difícil')," +
                "('Qual é o maior vulcão do mundo?', 'Kilauea', 'Mauna Loa', 'Etna', 'Vesuvius', 'Mauna Loa', 'difícil')," +
                "('Em que ano foi inventada a lâmpada?', '1860', '1879', '1890', '1901', '1879', 'difícil')," +
                "('Quem descobriu a penicilina?', 'Alexander Fleming', 'Louis Pasteur', 'Marie Curie', 'Thomas Edison', 'Alexander Fleming', 'difícil')," +
                "('Qual é a profundidade média do oceano?', '3 km', '3,7 km', '4 km', '5 km', '3,7 km', 'difícil');");
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
            String correctAnswer = cursor.getString(cursor.getColumnIndex(COLUMN_CORRECT));
            cursor.close();
            return correctAnswer.equalsIgnoreCase(userAnswer); // Ignora diferenças de maiúsculas e minúsculas
        }

        if (cursor != null) {
            cursor.close();
        }

        return false; // Retorna falso se a pergunta não foi encontrada
    }

    // Obtém perguntas aleatórias por dificuldade
    public Cursor getRandomQuestions(String difficulty, int limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + COLUMN_DIFFICULTY + " = ? ORDER BY RANDOM() LIMIT ?",
                new String[]{difficulty, String.valueOf(limit)});
    }
}
