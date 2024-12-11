package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipal extends AppCompatActivity {

    private EditText edtUserName;
    private RadioGroup rgDifficulty;
    private GuardarNomeDB databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        // Inicializa o banco de dados
        databaseHelper = new GuardarNomeDB(this);

        // Vincula os elementos de layout
        edtUserName = findViewById(R.id.edtUserName);
        rgDifficulty = findViewById(R.id.rgDifficulty);
        Button btnStart = findViewById(R.id.btnStart);

        // Verifica se os componentes estão carregados
        if (edtUserName == null || rgDifficulty == null || btnStart == null) {
            Toast.makeText(this, "Erro ao carregar os componentes da interface", Toast.LENGTH_LONG).show();
            return;
        }

        // Configura o botão "Iniciar"
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserName.getText().toString().trim();
                int selectedDifficultyId = rgDifficulty.getCheckedRadioButtonId();

                if (userName.isEmpty()) {
                    Toast.makeText(MenuPrincipal.this, "Por favor, insira um nome", Toast.LENGTH_SHORT).show();
                } else if (selectedDifficultyId == -1) {
                    Toast.makeText(MenuPrincipal.this, "Por favor, selecione uma dificuldade", Toast.LENGTH_SHORT).show();
                } else {
                    String difficulty = getSelectedDifficulty(selectedDifficultyId);
                    saveUserNameAndStartGame(userName, difficulty);
                }
            }
        });
    }

    private String getSelectedDifficulty(int selectedId) {
        RadioButton selectedButton = findViewById(selectedId);
        if (selectedButton != null) {
            return selectedButton.getText().toString();
        }
        return "Indefinida"; // Evita null pointer
    }

    private void saveUserNameAndStartGame(String userName, String difficulty) {
        // Salva o nome no banco de dados
        long result = databaseHelper.addUser(userName);
        if (result > 0) {
            Toast.makeText(this, "Nome salvo com sucesso!", Toast.LENGTH_SHORT).show();

            // Inicia a atividade "Perguntas" e passa os dados
            Intent intent = new Intent(this, Perguntas.class);
            intent.putExtra("user_name", userName);
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erro ao salvar nome", Toast.LENGTH_SHORT).show();
        }
    }
}
