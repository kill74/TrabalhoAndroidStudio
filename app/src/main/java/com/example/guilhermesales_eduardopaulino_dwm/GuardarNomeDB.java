package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Classe para gerir a base de dados dos utilizadores
public class GuardarNomeDB extends SQLiteOpenHelper {

    // Nome e versão da base de dados
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    // Nome da tabela e das colunas
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    // Construtor - inicializa a base de dados
    public GuardarNomeDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Criar a tabela quando a base de dados for criada pela primeira vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // ID auto-incrementado
                COLUMN_NAME + " TEXT NOT NULL);"; // Nome do utilizador, obrigatório
        db.execSQL(createTable); // Executa o comando SQL
    }

    // Atualizar a base de dados (caso a versão seja alterada)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS); // Apaga a tabela existente
        onCreate(db); // Recria a tabela
    }

    // Adiciona um novo utilizador
    public long addUser(String name) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtém a base de dados em modo de escrita
        ContentValues values = new ContentValues(); // Estrutura para armazenar os dados
        values.put(COLUMN_NAME, name); // Insere o nome no ContentValues
        return db.insert(TABLE_USERS, null, values); // Insere os dados na tabela e devolve o ID da linha inserida
    }

    // Retorna todos os utilizadores da tabela
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtém a base de dados em modo de leitura
        return db.query(TABLE_USERS, null, null, null, null, null, COLUMN_ID + " ASC"); // Consulta ordenada por ID
    }
}
