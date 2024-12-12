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

        txtMensagem = findViewById(R.id.GameOver);
        txtDinheiro = findViewById(R.id.moneyText);
        btnJogarNovamente = findViewById(R.id.restartButton);
        btnVoltarMenu = findViewById(R.id.menuButton);

        Intent intent = getIntent();
        int dinheiro = intent.getIntExtra("dinheiro", 0);

        txtMensagem.setText("Você perdeu!");
        txtDinheiro.setText("Prémio total: €" + dinheiro);

        // Ao carregar neste botão irá conseguir jogar de novo
        btnJogarNovamente.setOnClickListener(v -> {
            Intent intentRestart = new Intent(Perdeu.this, Perguntas.class);
            startActivity(intentRestart);
            finish();
        });

        // Ao carregar neste botao irá mandar para o Menu Principal
        btnVoltarMenu.setOnClickListener(v -> {
            Intent intentMenu = new Intent(Perdeu.this, MenuPrincipal.class);
            startActivity(intentMenu);
            finish();
        });
    }
}
