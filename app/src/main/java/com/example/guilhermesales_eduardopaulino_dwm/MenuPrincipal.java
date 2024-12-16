package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {

    // Elementos da interface do utilizador
    private EditText edtUserName;
    private GuardarNomeDB databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Inicializa a base de dados
        databaseHelper = new GuardarNomeDB(this);

        // Vincula os elementos de layout
        edtUserName = findViewById(R.id.edtUserName);
        Button btnStart = findViewById(R.id.btnStart);

        // Verifica se os componentes estão carregados
        if (edtUserName == null || btnStart == null) {
            Toast.makeText(this, "Erro ao carregar os componentes da interface", Toast.LENGTH_LONG).show();
            return;
        }

        // Configura o botão "Iniciar"
        btnStart.setOnClickListener(v -> onStartButtonClicked());
    }

    // Trata o clique no botão "Iniciar"
    private void onStartButtonClicked() {
        String userName = edtUserName.getText().toString().trim(); // Obtém o nome do utilizador

        // Verifica se o nome do utilizador está vazio
        if (userName.isEmpty()) {
            Toast.makeText(MenuPrincipal.this, "Por favor, insira um nome", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salva o nome do utilizador na base de dados e inicia o jogo
        saveUserNameAndStartGame(userName);
    }

    // Salva o nome do utilizador na base de dados e inicia o jogo
    private void saveUserNameAndStartGame(String userName) {
        // Salva o nome na base de dados
        long result = databaseHelper.addUser(userName);
        if (result > 0) {
            Toast.makeText(this, "Nome salvo com sucesso!", Toast.LENGTH_SHORT).show();

            // Inicia a atividade "Perguntas" e passa o nome do utilizador
            Intent intent = new Intent(this, Perguntas.class);
            intent.putExtra("user_name", userName);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erro ao salvar nome", Toast.LENGTH_SHORT).show();
        }
    }
}
