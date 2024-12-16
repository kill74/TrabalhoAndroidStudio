package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Esta classe é responsável por gerir uma base de dados SQLite para armazenar nomes de utilizadores.
 */
public class GuardarNomeDB extends SQLiteOpenHelper {

    // Nome do ficheiro da base de dados
    private static final String DATABASE_NAME = "users.db";

    // Versão da base de dados
    private static final int DATABASE_VERSION = 1;

    // Nome da tabela para armazenar os nomes do utilizador
    private static final String TABLE_USERS = "users";

    // Nome da coluna para o id do utilizador
    private static final String COLUMN_ID = "id";

    // Nome da coluna para o nome do utilizador
    private static final String COLUMN_NAME = "name";

    // Construtor da classe
    public GuardarNomeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Ira criar a base de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL)");
    }

    // Atualiza a base de dados para a nova versão
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Adiciona um novo utilizador a base de dados
    public long addUser(String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        return db.insert(TABLE_USERS, null, values);
    }

    // Obtém os utilizadores da base de dados
    public Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_USERS, null, null, null, null, null, COLUMN_ID + " ASC");
    }
}