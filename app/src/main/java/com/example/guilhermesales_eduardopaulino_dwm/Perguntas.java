package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Perguntas extends AppCompatActivity {

    // Declaração de elementos da interface e variáveis principais
    private TextView txtNivel, txtPremio, txtPergunta, txtTempo;
    private MaterialButton btnResposta1, btnResposta2, btnResposta3, btnResposta4, btnAjuda5050, btnDesistir, btnTrocar;
    private PerguntasDB perguntasDB; // Objeto para gerir as perguntas
    private int nivelAtual = 1; // Indica o nível atual do jogador
    private int premioAtual = 0; // Total acumulado de prémios
    private String respostaCerta; // Guarda a resposta correta da pergunta atual
    private List<PerguntasDB.Question> perguntasAleatorias; // Lista de perguntas aleatórias
    private int indicePerguntaAtual = 0; // Índice da pergunta atual
    private int acertosConsecutivos = 0; // Contador de acertos consecutivos
    private CountDownTimer temporizador; // Temporizador para cada pergunta

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perguntas);

        // Inicializar elementos da interface
        txtNivel = findViewById(R.id.txtLevel);
        txtPremio = findViewById(R.id.txtEarnings);
        txtPergunta = findViewById(R.id.txtQuestion);
        txtTempo = findViewById(R.id.txtTimer);

        btnResposta1 = findViewById(R.id.btnAnswer1);
        btnResposta2 = findViewById(R.id.btnAnswer2);
        btnResposta3 = findViewById(R.id.btnAnswer3);
        btnResposta4 = findViewById(R.id.btnAnswer4);
        btnAjuda5050 = findViewById(R.id.btnHelp50); 
        btnDesistir = findViewById(R.id.btnQuit);
        btnTrocar = findViewById(R.id.btnHelpSwitch);

        // Inicializa a base de dados de perguntas
        perguntasDB = new PerguntasDB(this);

        // Recupera o nome do jogador enviado pelo MenuPrincipal
        String nomeJogador = getIntent().getStringExtra("user_name");
        if (nomeJogador != null) {
            Toast.makeText(this, "Bem-vindo, " + nomeJogador + "!", Toast.LENGTH_SHORT).show();
        }

        // Obter uma lista de perguntas aleatórias da base de dados
        perguntasAleatorias = perguntasDB.getRandomQuestions();

        // Carregar a primeira pergunta
        carregarPergunta();

        // Configurar ações para os botões de resposta
        btnResposta1.setOnClickListener(v -> verificarResposta(btnResposta1.getText().toString()));
        btnResposta2.setOnClickListener(v -> verificarResposta(btnResposta2.getText().toString()));
        btnResposta3.setOnClickListener(v -> verificarResposta(btnResposta3.getText().toString()));
        btnResposta4.setOnClickListener(v -> verificarResposta(btnResposta4.getText().toString()));

        // Configurar ação do botão de ajuda "50/50"
        btnAjuda5050.setOnClickListener(v -> aplicarAjuda5050());

        // Configurar ação do botão de desistir
        btnDesistir.setOnClickListener(v -> desistirJogo());

        // Configurar ação do botão de trocar de pergunta
        btnTrocar.setOnClickListener(v -> trocarPergunta());
    }

    /**
     * Carrega a pergunta atual baseada no índice e atualiza os elementos da interface.
     */
    private void carregarPergunta() {
        if (indicePerguntaAtual < perguntasAleatorias.size()) {
            PerguntasDB.Question perguntaAtual = perguntasAleatorias.get(indicePerguntaAtual);

            // Atualiza os textos dos elementos da interface
            txtNivel.setText("Nível: " + nivelAtual);
            txtPremio.setText("Prémio: €" + premioAtual);
            txtPergunta.setText(perguntaAtual.getQuestion());
            btnResposta1.setText(perguntaAtual.getAnswer1());
            btnResposta2.setText(perguntaAtual.getAnswer2());
            btnResposta3.setText(perguntaAtual.getAnswer3());
            btnResposta4.setText(perguntaAtual.getAnswer4());

            // Armazena a resposta correta
            respostaCerta = perguntaAtual.getCorrect();

            // Inicia o temporizador de 20 segundos
            iniciarTemporizador(2000);
        } else {
            // Caso todas as perguntas sejam respondidas, o jogo termina
            finalizarJogo("Parabéns! Ganhou €" + premioAtual);
        }
    }

    /**
     * Inicia o temporizador para contar o tempo de resposta.
     */
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
        };
        temporizador.start();
    }

    /**
     * Verifica se a resposta selecionada pelo jogador está correta.
     */
    private void verificarResposta(String respostaSelecionada) {
        if (temporizador != null) temporizador.cancel();

        if (respostaSelecionada.equals(respostaCerta)) {
            acertosConsecutivos++; // Incrementa o número de acertos consecutivos
            premioAtual += 500;  // Por cada resposta correta ira aumentar 500 em 500
            Toast.makeText(this, "Resposta correta! +€1000", Toast.LENGTH_SHORT).show();
            nivelAtual++;  // Avança para o próximo nível
            indicePerguntaAtual++;  // Avança para a próxima pergunta

            // Verificar se o jogador acertou 15 perguntas consecutivas
            if (acertosConsecutivos == 15) {
                Intent intent = new Intent(Perguntas.this, Ganhou.class);
                intent.putExtra("pontos", nivelAtual);
                intent.putExtra("dinheiro", premioAtual);
                startActivity(intent);
                finish();
            } else {
                carregarPergunta();  // Carregar a próxima pergunta
            }

        } else {
            // Se errar, vai para a página de "Perdeu"
            Intent intent = new Intent(Perguntas.this, Perdeu.class);
            intent.putExtra("pontos", nivelAtual - 1);  // Envia os pontos conquistados
            intent.putExtra("dinheiro", premioAtual);  // Envia o valor acumulado
            startActivity(intent);
            finish();
        }
    }

    private boolean ajudausada = false;

    /**
     * Aplica a ajuda "50/50", desativando duas respostas erradas.
     */
    private void aplicarAjuda5050() {
        // Verificar se a ajuda já foi usada
        if(ajudausada){
            return; // ira sair da funcao se a ajuda ja foi usada
        }


        List<MaterialButton> botoes = new ArrayList<>();
        botoes.add(btnResposta1);
        botoes.add(btnResposta2);
        botoes.add(btnResposta3);
        botoes.add(btnResposta4);

        int removidos = 0; // Contador de respostas desativadas
        for (MaterialButton botao : botoes) {
            if (!botao.getText().toString().equals(respostaCerta) && removidos < 2) {
                botao.setEnabled(false);
                removidos++;
            }
        }
        btnAjuda5050.setEnabled(false); // Desativa o botão após uso
        ajudausada = true; // o que isto vai fazer é assumir que a ajuda ja foi usada e o jogador nao ira conseguir usar mais
    }

    /**
     * Troca a pergunta atual por outra.
     */
    private void trocarPergunta() {
        if (indicePerguntaAtual < perguntasAleatorias.size() - 1) {
            indicePerguntaAtual++;
            carregarPergunta();
        } else {
            Toast.makeText(this, "Não há mais perguntas disponíveis.", Toast.LENGTH_SHORT).show();
        }
        btnTrocar.setEnabled(false); // Desativa o botão após uso
    }

    /**
     * Desiste do jogo e volta ao menu principal.
     */
    private void desistirJogo() {
        startActivity(new Intent(this, MenuPrincipal.class));
        finish();
    }

    private void finalizarJogo(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
        finish();
    }
}
