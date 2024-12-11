package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Perdeu extends AppCompatActivity {

    private TextView txtMensagem, txtPontos, txtDinheiro;
    private Button btnJogarNovamente, btnVoltarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdeu_jogo);

        txtMensagem = findViewById(R.id.GameOver); // Atualize para o ID correto no XML
        txtPontos = findViewById(R.id.scoreText); // Atualize para o ID correto no XML
        txtDinheiro = findViewById(R.id.moneyText); // Atualize para o ID correto no XML
        btnJogarNovamente = findViewById(R.id.restartButton); // Atualize para o ID correto no XML
        btnVoltarMenu = findViewById(R.id.menuButton); // Atualize para o ID correto no XML

        Intent intent = getIntent();
        int pontos = intent.getIntExtra("pontos", 0);
        int dinheiro = intent.getIntExtra("dinheiro", 0);

        txtMensagem.setText("Você perdeu!");
        txtPontos.setText("Níveis completados: " + pontos);
        txtDinheiro.setText("Prémio total: €" + dinheiro);

        btnJogarNovamente.setOnClickListener(v -> {
            Intent intentRestart = new Intent(Perdeu.this, Perguntas.class);
            startActivity(intentRestart);
            finish();
        });

        btnVoltarMenu.setOnClickListener(v -> {
            Intent intentMenu = new Intent(Perdeu.this, MenuPrincipal.class);
            startActivity(intentMenu);
            finish();
        });
    }
}
