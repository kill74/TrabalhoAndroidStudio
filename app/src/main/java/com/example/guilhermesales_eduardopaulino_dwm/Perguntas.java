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
    // Declaração das variáveis para elementos visuais da interface do utilizador
    private TextView txtNivel, txtPremio, txtPergunta, txtTempo; // Textos para mostrar o nível, prémio, pergunta e tempo
    private MaterialButton[] btnRespostas = new MaterialButton[4]; // Botões para as respostas (4 no total)
    private MaterialButton btnAjuda5050, btnDesistir, btnTrocar; // Botões para ajudas e desistência

    // Declaração da base de dados e variáveis de jogo
    private PerguntasDB perguntasDB; // Objeto responsável por gerir a base de dados das perguntas
    private int nivelAtual = 1; // Nível atual do jogador, começa no nível 1
    private int premioAtual = 0; // Prémio acumulado pelo jogador
    private String respostaCerta; // Resposta correta para a pergunta atual
    private List<PerguntasDB.Question> perguntasAleatorias; // Lista de perguntas aleatórias carregadas
    private int indicePerguntaAtual = 0; // Índice da pergunta atual na lista
    private boolean ajudaUsada = false; // Estado para verificar se a ajuda 50/50 já foi usada
    private CountDownTimer temporizador; // Temporizador para limitar o tempo de resposta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perguntas);

        // Inicializa os elementos da interface e as ações dos botões
        inicializarComponentes();
        configurarAcoesBotoes();
    }

    private void inicializarComponentes() {
        // Liga as variáveis de texto aos elementos do layout
        txtNivel = findViewById(R.id.txtLevel);
        txtPremio = findViewById(R.id.txtEarnings);
        txtPergunta = findViewById(R.id.txtQuestion);
        txtTempo = findViewById(R.id.txtTimer);

        // Liga os botões das respostas ao layout (4 botões no total)
        btnRespostas[0] = findViewById(R.id.btnAnswer1);
        btnRespostas[1] = findViewById(R.id.btnAnswer2);
        btnRespostas[2] = findViewById(R.id.btnAnswer3);
        btnRespostas[3] = findViewById(R.id.btnAnswer4);

        // Liga os botões das ajudas ao layout
        btnAjuda5050 = findViewById(R.id.btnHelp50);
        btnDesistir = findViewById(R.id.btnQuit);
        btnTrocar = findViewById(R.id.btnHelpSwitch);

        // Cria um objeto para gerir a base de dados
        perguntasDB = new PerguntasDB(this);

        // Verifica se foi passado o nome do jogador pela intenção e mostra uma mensagem de boas-vindas
        String nomeJogador = getIntent().getStringExtra("user_name");
        if (nomeJogador != null) {
            Toast.makeText(this, "Bem-vindo, " + nomeJogador + "!", Toast.LENGTH_SHORT).show();
        }

        // Obtém uma lista de perguntas aleatórias da base de dados
        perguntasAleatorias = perguntasDB.getRandomQuestions();

        // Carrega a primeira pergunta no jogo
        carregarPergunta();
    }

    private void configurarAcoesBotoes() {
        // Define a ação ao clicar em qualquer botão de resposta
        for (MaterialButton botao : btnRespostas) {
            botao.setOnClickListener(v -> verificarResposta(botao.getText().toString()));
        }

        // Define a ação do botão de ajuda 50/50
        btnAjuda5050.setOnClickListener(v -> aplicarAjuda5050());

        // Define a ação do botão de desistência
        btnDesistir.setOnClickListener(v -> desistirJogo());

        // Define a ação do botão para trocar de pergunta
        btnTrocar.setOnClickListener(v -> trocarPergunta());
    }

    // Poderiamos fazer isto com um try e um catch
    // Carrega a pergunta atual na interface
    private void carregarPergunta() {
        if (indicePerguntaAtual < perguntasAleatorias.size()) {
            // Obtém a pergunta atual da lista
            PerguntasDB.Question perguntaAtual = perguntasAleatorias.get(indicePerguntaAtual);

            // Torna todos os botões de resposta visíveis
            for (MaterialButton botao : btnRespostas) {
                botao.setVisibility(View.VISIBLE);
            }

            // Atualiza os textos da interface com as informações da pergunta atual
            txtNivel.setText("Nível: " + nivelAtual);
            txtPremio.setText("Prémio: €" + premioAtual);
            txtPergunta.setText(perguntaAtual.getQuestion());

            // Atribui as respostas aos botões
            btnRespostas[0].setText(perguntaAtual.getAnswer1());
            btnRespostas[1].setText(perguntaAtual.getAnswer2());
            btnRespostas[2].setText(perguntaAtual.getAnswer3());
            btnRespostas[3].setText(perguntaAtual.getAnswer4());

            // Armazena a resposta correta para validação
            respostaCerta = perguntaAtual.getCorrect();

            // Inicia o temporizador para a pergunta atual (20 segundos)
            iniciarTemporizador(20000);
        } else {
            // Caso todas as perguntas tenham sido respondidas, o jogo termina
            finalizarJogo("Parabéns! Ganhou €" + premioAtual);
        }
    }

    private void iniciarTemporizador(long tempo) {
        // Cancela o temporizador anterior, caso exista
        if (temporizador != null) temporizador.cancel();

        // Cria um novo temporizador que atualiza o texto do tempo a cada segundo
        temporizador = new CountDownTimer(tempo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTempo.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                // Quando o tempo termina, o jogo finaliza com uma mensagem
                finalizarJogo("Tempo esgotado! Jogo terminado.");
            }
        }.start();
    }

    private void verificarResposta(String respostaSelecionada) {
        // Cancela o temporizador ao verificar a resposta
        temporizador.cancel();

        // Verifica se a resposta selecionada está correta
        boolean acertou = respostaSelecionada.equals(respostaCerta);
        if (acertou) {
            // Se a resposta estiver correta, incrementa o prémio e o nível
            premioAtual += 500;
            nivelAtual++;
            indicePerguntaAtual++;
            Toast.makeText(this, "Resposta correta! +€500", Toast.LENGTH_SHORT).show();
        } else {
            // Se a resposta estiver errada, decrementa o nível
            nivelAtual--;
        }

        // Verifica se o jogo terminou (nível máximo ou jogador perdeu)
        if (nivelAtual == 15 || nivelAtual == 0) {
            Intent intent = new Intent(this, acertou ? Ganhou.class : Perdeu.class);
            intent.putExtra("Nível", nivelAtual);
            intent.putExtra("dinheiro", premioAtual);
            startActivity(intent);
            finish();
        } else {
            // Caso contrário, carrega a próxima pergunta
            carregarPergunta();
        }
    }

    private void aplicarAjuda5050() {
        // Verifica se a ajuda já foi usada
        if (ajudaUsada) return;

        // Remove duas respostas incorretas da interface
        int removidos = 0;
        for (MaterialButton botao : btnRespostas) {
            if (!botao.getText().toString().equals(respostaCerta) && removidos < 2) {
                botao.setVisibility(View.INVISIBLE);
                removidos++;
            }
        }

        // Marca a ajuda como usada e desativa o botão
        ajudaUsada = true;
        btnAjuda5050.setEnabled(false);
    }

    private void trocarPergunta() {
        // Troca para a próxima pergunta na lista, se disponível
        if (indicePerguntaAtual < perguntasAleatorias.size() - 1) {
            indicePerguntaAtual++;
            carregarPergunta();
            btnTrocar.setEnabled(false); // Desativa o botão após o uso
        } else {
            Toast.makeText(this, "Não há mais perguntas disponíveis.", Toast.LENGTH_SHORT).show();
        }
    }

    private void desistirJogo() {
        // Cancela o temporizador se o jogador decidir desistir
        if (temporizador != null) temporizador.cancel();

        if (nivelAtual == 5 || nivelAtual == 10) {
            // Redireciona para a tela de vitória se o nível for garantido (5 ou 10)
            Intent intent = new Intent(this, Ganhou.class);
            intent.putExtra("Nível", nivelAtual);
            intent.putExtra("dinheiro", premioAtual);
            startActivity(intent);
        } else {
            // Caso contrário, volta para o menu principal
            Toast.makeText(this, "Você desistiu antes de alcançar um prémio garantido.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
        }

        finish(); // Fecha a atividade atual
    }

    private void finalizarJogo(String mensagem) {
        // Mostra uma mensagem final e encerra a atividade
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        finish();
    }
}
