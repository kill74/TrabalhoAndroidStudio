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

    private int nivelAtual = 1; // ira começar como default no nivel 1
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
        // Inicializar elementos da interface
        txtNivel = findViewById(R.id.txtLevel);
        txtPremio = findViewById(R.id.txtEarnings);
        txtPergunta = findViewById(R.id.txtQuestion);
        txtTempo = findViewById(R.id.txtTimer);

        // Inicializar botões de resposta
        btnRespostas[0] = findViewById(R.id.btnAnswer1);
        btnRespostas[1] = findViewById(R.id.btnAnswer2);
        btnRespostas[2] = findViewById(R.id.btnAnswer3);
        btnRespostas[3] = findViewById(R.id.btnAnswer4);

        btnAjuda5050 = findViewById(R.id.btnHelp50);
        btnDesistir = findViewById(R.id.btnQuit);
        btnTrocar = findViewById(R.id.btnHelpSwitch);

        // Inicializa a base de dados de perguntas
        perguntasDB = new PerguntasDB(this);

        // Recupera o nome do jogador
        String nomeJogador = getIntent().getStringExtra("user_name");
        if (nomeJogador != null) {
            Toast.makeText(this, "Bem-vindo, " + nomeJogador + "!", Toast.LENGTH_SHORT).show(); // ira aparecer um pop up a desejar boas vindas ao jogador
        }

        // Obter uma lista de perguntas aleatórias
        perguntasAleatorias = perguntasDB.getRandomQuestions();

        // Carregar a primeira pergunta
        carregarPergunta();
    }

    private void configurarAcoesBotoes() {
        // Configurar ações para os botões de resposta
        for (MaterialButton botao : btnRespostas) {
            botao.setOnClickListener(v -> verificarResposta(botao.getText().toString()));
        }

        // Configurar ação do botão de ajuda "50/50"
        btnAjuda5050.setOnClickListener(v -> aplicarAjuda5050());

        // Configurar ação do botão de desistir
        btnDesistir.setOnClickListener(v -> desistirJogo());

        // Configurar ação do botão de trocar de pergunta
        btnTrocar.setOnClickListener(v -> trocarPergunta());
    }

    private void carregarPergunta() {
        if (indicePerguntaAtual < perguntasAleatorias.size()) {
            PerguntasDB.Question perguntaAtual = perguntasAleatorias.get(indicePerguntaAtual);

            // Restaura a visibilidade de todos os botões
            for (MaterialButton botao : btnRespostas) {
                botao.setVisibility(View.VISIBLE);
            }

            // Atualiza os textos dos elementos da interface
            txtNivel.setText("Nível: " + nivelAtual);
            txtPremio.setText("Prémio: €" + premioAtual);
            txtPergunta.setText(perguntaAtual.getQuestion());

            // Atribui textos aos botões
            btnRespostas[0].setText(perguntaAtual.getAnswer1());
            btnRespostas[1].setText(perguntaAtual.getAnswer2());
            btnRespostas[2].setText(perguntaAtual.getAnswer3());
            btnRespostas[3].setText(perguntaAtual.getAnswer4());

            // Armazena a resposta correta
            respostaCerta = perguntaAtual.getCorrect();

            // Inicia o temporizador de 20 segundos
            iniciarTemporizador(20000);
        } else {
            // Caso todas as perguntas sejam respondidas, o jogo termina
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
        if (temporizador != null) temporizador.cancel();

        if (respostaSelecionada.equals(respostaCerta)) {
            premioAtual += 500; // se a resposta estiver certa o jogador ira ganhar 500
            Toast.makeText(this, "Resposta correta! +€500", Toast.LENGTH_SHORT).show(); // e ira aparecer um pop up
            nivelAtual++; // ira aumentara mais um nivel
            indicePerguntaAtual++;

            if (nivelAtual == 15) { // se ele estiver no nivel 15
                Intent intent = new Intent(this, Ganhou.class); // ira ser enviado para a pagina final (Ganhou)
                intent.putExtra("Nível", nivelAtual); // que ira mostrar o nível em que ele acabou
                intent.putExtra("dinheiro", premioAtual); // bem como o dinheiro que ele ganhou
                startActivity(intent);
                finish();
            } else {
                carregarPergunta();
            }
        } else { // se ele errar alguma pergunta ira ser mandado para a pagina final (Perdeu)
            Intent intent = new Intent(this, Perdeu.class);
            intent.putExtra("Nível", nivelAtual - 1); // que ira mostrar o nivel em que ele perdeu
            intent.putExtra("dinheirqqqqqqqqawqqq2qQq12222qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq                   1o", premioAtual); // bem como o dinheiro que ele perdeu
            startActivity(intent);
            finish();
        }
    }

    // Função
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
        startActivity(new Intent(this, MenuPrincipal.class));
        finish();
    }

    private void finalizarJogo(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        finish();
    }
}