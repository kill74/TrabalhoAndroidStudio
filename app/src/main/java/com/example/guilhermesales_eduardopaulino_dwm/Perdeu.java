package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Perdeu extends AppCompatActivity {

    private TextView mensagem, textoDinheiro;
    private Button botaoJogarNovamente, botaoVoltarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdeu_jogo);

        // Vincula os elementos do layout aos objetos
        mensagem = findViewById(R.id.GameOver);
        textoDinheiro = findViewById(R.id.moneyText);
        botaoJogarNovamente = findViewById(R.id.restartButton);
        botaoVoltarMenu = findViewById(R.id.menuButton);

        // Recebe os dados enviados pela Intent
        Intent intent = getIntent();
        int dinheiro = intent.getIntExtra("dinheiro", 0);

        // Configura os textos na interface
        mensagem.setText("Você perdeu!");
        textoDinheiro.setText("Prémio total: €" + dinheiro);

        // Configura a ação dos botões
        configurarBotao(botaoJogarNovamente, Perguntas.class);
        configurarBotao(botaoVoltarMenu, MenuPrincipal.class);
    }

    private void configurarBotao(Button botao, Class<?> classe) {
        botao.setOnClickListener(v -> {
            Intent intent = new Intent(Perdeu.this, classe);
            startActivity(intent);
            finish();
        });
    }
}