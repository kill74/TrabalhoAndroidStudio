package com.example.guilhermesales_eduardopaulino_dwm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Ganhou extends AppCompatActivity {

    private TextView txtMensagem, txtDinheiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganhou);

        txtMensagem = findViewById(R.id.youWonText);
        txtDinheiro = findViewById(R.id.moneyText);

        // Receber os pontos e o prêmio do intent
        int pontos = getIntent().getIntExtra("pontos", 0);
        int dinheiro = getIntent().getIntExtra("dinheiro", 0);

        txtMensagem.setText("Parabéns, você ganhou!");
        txtDinheiro.setText("Prêmio total: €" + dinheiro);
    }
}
