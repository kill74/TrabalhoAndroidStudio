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

        temporizador = new CountDownTimer(tempo, 1000) { // ira de 1 em 1 ate ao final
            @Override
            public void onTick(long millisUntilFinished) {
                txtTempo.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                finalizarJogo("Tempo esgotado! Jogo terminado."); // se o temporizador chegar ao 0 ira mostrar esta mensagem
            }
        }.start();
    }

    private void verificarResposta(String respostaSelecionada) {
        temporizador.cancel(); // Para o temporizador, independente da resposta

        // Verifica se a resposta que o jogador selecionou é a resposta certa
        boolean acertou = respostaSelecionada.equals(respostaCerta);
        if (acertou) {
            // Ação caso a resposta esteja correta
            premioAtual += 500;
            nivelAtual++; // Avança para o próximo nivel
            indicePerguntaAtual++;
            Toast.makeText(this, "Resposta correta! +€500", Toast.LENGTH_SHORT).show();
        } else {
            // Ação caso a resposta esteja incorreta
            nivelAtual--; //
        }

        // Verifica se o jogo acabou ou se deve continuar
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

    // Função da ajuda de 50/50
    private void aplicarAjuda5050() {
        if (ajudaUsada) return; // se a ajuda ja foi usado o jogador nao ira conseguir usar mais a ajuda

        int removidos = 0;
        for (MaterialButton botao : btnRespostas) {
            if (!botao.getText().toString().equals(respostaCerta) && removidos < 2) { //ira remover 2 perguntas e ira mostrar a resposta certa e outra que estara errada
                botao.setVisibility(View.INVISIBLE); // ira esconder as 2 perguntas que ira estar incorretas
                removidos++;
            }
        }

        ajudaUsada = true; // se o botao ja foi usado
        btnAjuda5050.setEnabled(false); // ira desabilitalo e o jogador nao ira poder usar mais
    }

    // Função para trocar a pergunta
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

        // Verificar se é a 5ª ou 10ª pergunta e atualizar o prêmio se necessário
        if (indicePerguntaAtual == 4 || indicePerguntaAtual == 9) {
            // Verificar se a última resposta estava correta (simplificado para legibilidade)
            if (respostaCerta.equals(btnRespostas[0].getText().toString())) {
                premioAtual += 500;
            }

            Intent intent = new Intent(this, Ganhou.class);
            intent.putExtra("Nível", nivelAtual);
            intent.putExtra("dinheiro", premioAtual);
            startActivity(intent);
        } else {
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