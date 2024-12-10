package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {

    private EditText edtUserName;
    private GuardarNomeDB databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Inicializa o banco de dados
        databaseHelper = new GuardarNomeDB(this);

        // Vincula os elementos de layout
        edtUserName = findViewById(R.id.edtUserName);
        Button btnStart = findViewById(R.id.btnStart);

        // Configura o botão
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserName.getText().toString().trim();
                if (userName.isEmpty()) {
                    Toast.makeText(MenuPrincipal.this, "Por favor, insira um nome", Toast.LENGTH_SHORT).show();
                } else {
                    saveUserName(userName);
                }
            }
        });
    }

    private void saveUserName(String userName) {
        // Adiciona o nome ao banco de dados
        long result = databaseHelper.addUser(userName);
        if (result > 0) {
            Toast.makeText(this, "Nome salvo com sucesso!", Toast.LENGTH_SHORT).show();
            // Inicia a próxima atividade e passa o nome do usuário
            Intent intent = new Intent(this, Perguntas.class);
            intent.putExtra("user_name", userName);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erro ao salvar nome", Toast.LENGTH_SHORT).show();
        }
    }
}
