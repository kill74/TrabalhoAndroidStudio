package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Perdeu extends AppCompatActivity {

    private TextView txtMensagem, txtDinheiro;
    private Button btnJogarNovamente, btnVoltarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdeu_jogo);

        // Vincula os elementos do layout aos objetos
        txtMensagem = findViewById(R.id.GameOver);
        txtDinheiro = findViewById(R.id.moneyText);
        btnJogarNovamente = findViewById(R.id.restartButton);
        btnVoltarMenu = findViewById(R.id.menuButton);

        // Recebe os dados enviados pela Intent
        Intent intent = getIntent();
        int dinheiro = intent.getIntExtra("dinheiro", 0);

        // Configura os textos na interface
        txtMensagem.setText("Você perdeu!");
        txtDinheiro.setText("Prémio total: €" + dinheiro);

        // Configura ação do botão "Jogar Novamente"
        btnJogarNovamente.setOnClickListener(v -> {
            Intent intentRestart = new Intent(Perdeu.this, Perguntas.class);
            startActivity(intentRestart);
            finish(); // Finaliza a atividade atual
        });

        // Configura ação do botão "Voltar ao Menu"
        btnVoltarMenu.setOnClickListener(v -> {
            Intent intentMenu = new Intent(Perdeu.this, MenuPrincipal.class);
            startActivity(intentMenu);
            finish(); // Finaliza a atividade atual
        });
    }
}
