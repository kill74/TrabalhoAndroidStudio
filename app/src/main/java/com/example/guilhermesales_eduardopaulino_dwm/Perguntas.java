package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class Perguntas extends AppCompatActivity {
    private TextView txtNivel, txtPremio, txtPergunta, txtTempo;
    private MaterialButton[] btnRespostas = new MaterialButton[4];
    private MaterialButton btnAjuda5050, btnDesistir, btnTrocar;
    private PerguntasDB perguntasDB;

    private int nivelAtual = 1;
    private int premioAtual = 0;
    private String respostaCerta;
    private List<PerguntasDB.Question> perguntasAleatorias;
    private int indicePerguntaAtual = 0;
    private boolean ajudaUsada = false;
    private CountDownTimer temporizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perguntas);

        inicializarComponentes();
        configurarAcoesBotoes();
    }

    private void inicializarComponentes() {
        txtNivel = findViewById(R.id.txtLevel);
        txtPremio = findViewById(R.id.txtEarnings);
        txtPergunta = findViewById(R.id.txtQuestion);
        txtTempo = findViewById(R.id.txtTimer);

        btnRespostas[0] = findViewById(R.id.btnAnswer1);
        btnRespostas[1] = findViewById(R.id.btnAnswer2);
        btnRespostas[2] = findViewById(R.id.btnAnswer3);
        btnRespostas[3] = findViewById(R.id.btnAnswer4);

        btnAjuda5050 = findViewById(R.id.btnHelp50);
        btnDesistir = findViewById(R.id.btnQuit);
        btnTrocar = findViewById(R.id.btnHelpSwitch);

        perguntasDB = new PerguntasDB(this);

        String nomeJogador = getIntent().getStringExtra("user_name");
        if (nomeJogador != null) {
            Toast.makeText(this, "Bem-vindo, " + nomeJogador + "!", Toast.LENGTH_SHORT).show();
        }

        perguntasAleatorias = perguntasDB.getRandomQuestions();
        carregarPergunta();
    }

    private void configurarAcoesBotoes() {
        for (MaterialButton botao : btnRespostas) {
            botao.setOnClickListener(v -> verificarResposta(botao.getText().toString()));
        }

        btnAjuda5050.setOnClickListener(v -> aplicarAjuda5050());
        btnDesistir.setOnClickListener(v -> desistirJogo());
        btnTrocar.setOnClickListener(v -> trocarPergunta());
    }

    private void carregarPergunta() {
        if (indicePerguntaAtual < perguntasAleatorias.size()) {
            PerguntasDB.Question perguntaAtual = perguntasAleatorias.get(indicePerguntaAtual);

            for (MaterialButton botao : btnRespostas) {
                botao.setVisibility(View.VISIBLE);
            }

            txtNivel.setText("Nível: " + nivelAtual);
            txtPremio.setText("Prémio: €" + premioAtual);
            txtPergunta.setText(perguntaAtual.getQuestion());

            btnRespostas[0].setText(perguntaAtual.getAnswer1());
            btnRespostas[1].setText(perguntaAtual.getAnswer2());
            btnRespostas[2].setText(perguntaAtual.getAnswer3());
            btnRespostas[3].setText(perguntaAtual.getAnswer4());

            respostaCerta = perguntaAtual.getCorrect();

            iniciarTemporizador(20000);
        } else {
            finalizarJogo("Parabéns! Ganhou €" + premioAtual);
        }
    }

    private void iniciarTemporizador(long tempo) {
        if (temporizador != null) temporizador.cancel();

        temporizador = new CountDownTimer(tempo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTempo.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                finalizarJogo("Tempo esgotado! Jogo terminado.");
            }
        }.start();
    }

    private void verificarResposta(String respostaSelecionada) {
        temporizador.cancel();

        boolean acertou = respostaSelecionada.equals(respostaCerta);
        if (acertou) {
            premioAtual += 500;
            nivelAtual++;
            indicePerguntaAtual++;
            Toast.makeText(this, "Resposta correta! +€500", Toast.LENGTH_SHORT).show();
        } else {
            nivelAtual--;
        }

        if (nivelAtual == 15 || nivelAtual == 0) {
            Intent intent = new Intent(this, acertou ? Ganhou.class : Perdeu.class);
            intent.putExtra("Nível", nivelAtual);
            intent.putExtra("dinheiro", premioAtual);
            startActivity(intent);
            finish();
        } else {
            carregarPergunta();
        }
    }

    private void aplicarAjuda5050() {
        if (ajudaUsada) return;

        int removidos = 0;
        for (MaterialButton botao : btnRespostas) {
            if (!botao.getText().toString().equals(respostaCerta) && removidos < 2) {
                botao.setVisibility(View.INVISIBLE);
                removidos++;
            }
        }

        ajudaUsada = true;
        btnAjuda5050.setEnabled(false);
    }

    private void trocarPergunta() {
        if (indicePerguntaAtual < perguntasAleatorias.size() - 1) {
            indicePerguntaAtual++;
            carregarPergunta();
            btnTrocar.setEnabled(false);
        } else {
            Toast.makeText(this, "Não há mais perguntas disponíveis.", Toast.LENGTH_SHORT).show();
        }
    }

    private void desistirJogo() {
        if (temporizador != null) temporizador.cancel();

        if (nivelAtual == 5 || nivelAtual == 10) {
            // Se estiver no nível 5 ou 10, redireciona para a página Ganhou
            Intent intent = new Intent(this, Ganhou.class);
            intent.putExtra("Nível", nivelAtual);
            intent.putExtra("dinheiro", premioAtual);
            startActivity(intent);
        } else {
            // Caso contrário, volta ao menu principal
            Toast.makeText(this, "Você desistiu antes de alcançar um prêmio garantido.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
        }

        finish();
    }

    private void finalizarJogo(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        finish();
    }
}
