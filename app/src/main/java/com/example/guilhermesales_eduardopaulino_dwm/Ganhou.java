package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Ganhou extends AppCompatActivity {

    private TextView txtMensagem, txtDinheiro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganhou);

        // Referenciar elementos da interface
        txtMensagem = findViewById(R.id.youWonText);
        txtDinheiro = findViewById(R.id.moneyText);
        Button restartButton = findViewById(R.id.restartButton);
        Button menuButton = findViewById(R.id.menuButton);

        // Receber os pontos e o prêmio do intent
        int pontos = getIntent().getIntExtra("pontos", 0);
        int dinheiro = getIntent().getIntExtra("dinheiro", 0);

        // Configurar os textos
        txtMensagem.setText("Parabéns, você ganhou!");
        txtDinheiro.setText("Prêmio total: €" + dinheiro);

        // Configurar ação do botão "Jogar Novamente"
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ganhou.this, Perguntas.class);
                startActivity(intent);
                finish(); // Finaliza a atividade atual
            }
        });

        // Configurar ação do botão "Voltar ao Menu"
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ganhou.this, MenuPrincipal.class);
                startActivity(intent);
                finish(); // Finaliza a atividade atual
            }
        });
    }
}
