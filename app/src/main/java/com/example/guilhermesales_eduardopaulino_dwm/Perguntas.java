package com.example.guilhermesales_eduardopaulino_dwm;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Perguntas extends AppCompatActivity {

    private TextView txtLevel, txtEarnings, txtQuestion, txtTimer;
    private MaterialButton btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnHelpSwitch;
    private PerguntasDB perguntasDB; // Base de dados para as perguntas
    private int currentLevel = 1; // Nível atual do jogo
    private int currentEarnings = 0; // Prémio acumulado
    private String correctAnswer; // Resposta correta atual
    private int questionIndex = 1; // Índice da pergunta atual (1-6)
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perguntas);

        // Inicializar elementos da interface
        txtLevel = findViewById(R.id.txtLevel);
        txtEarnings = findViewById(R.id.txtEarnings);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtTimer = findViewById(R.id.txtTimer);

        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        btnHelpSwitch = findViewById(R.id.btnHelpSwitch);

        perguntasDB = new PerguntasDB(this); // Inicializa a base de dados
        carregarPergunta(questionIndex); // Carrega a primeira pergunta

        // Configurar cliques nos botões de resposta
        btnAnswer1.setOnClickListener(v -> verificarResposta(btnAnswer1.getText().toString()));
        btnAnswer2.setOnClickListener(v -> verificarResposta(btnAnswer2.getText().toString()));
        btnAnswer3.setOnClickListener(v -> verificarResposta(btnAnswer3.getText().toString()));
        btnAnswer4.setOnClickListener(v -> verificarResposta(btnAnswer4.getText().toString()));

        // Configurar botão de ajuda para trocar de pergunta
        btnHelpSwitch.setOnClickListener(v -> trocarPergunta());
    }

    // Carrega a pergunta e respostas da base de dados
// Carrega a pergunta e respostas da base de dados
    private void carregarPergunta(int perguntaId) {
        Cursor cursor = perguntasDB.getQuestion(perguntaId);

        if (cursor != null && cursor.moveToFirst()) {
            // Atualiza as informações do nível e prêmio
            txtLevel.setText("Nível: " + currentLevel);
            txtEarnings.setText("Prémio: €" + currentEarnings);

            // Obtenha o índice das colunas
            int indexQuestion = cursor.getColumnIndex("question");
            int indexAnswer1 = cursor.getColumnIndex("answer1");
            int indexAnswer2 = cursor.getColumnIndex("answer2");
            int indexAnswer3 = cursor.getColumnIndex("answer3");
            int indexAnswer4 = cursor.getColumnIndex("answer4");
            int indexCorrect = cursor.getColumnIndex("correct");

            // Tive de fazer um if pois estava a dar um erro (Value must be ≥ 0 but getColumnIndex can be -1 ) e se o indice da coluna for
            // -1, significa que a coluna nao existe, e o else la em baixo ira terminar o jogo
            if (indexQuestion != -1 && indexAnswer1 != -1 && indexAnswer2 != -1 && indexAnswer3 != -1 && indexAnswer4 != -1 && indexCorrect != -1) {
                // Carrega as perguntas
                txtQuestion.setText(cursor.getString(indexQuestion));
                btnAnswer1.setText(cursor.getString(indexAnswer1));
                btnAnswer2.setText(cursor.getString(indexAnswer2));
                btnAnswer3.setText(cursor.getString(indexAnswer3));
                btnAnswer4.setText(cursor.getString(indexAnswer4));

                correctAnswer = cursor.getString(indexCorrect);
            } else {
                Toast.makeText(this, "Erro ao carregar os dados da pergunta. Colunas inválidas.", Toast.LENGTH_SHORT).show();
                finish(); // Termina o jogo se uma coluna não for encontrada
            }
        } else {
            Toast.makeText(this, "Erro ao carregar a pergunta.", Toast.LENGTH_SHORT).show();
            finish(); // Termina o jogo se a pergunta nao existir
        }

        if (cursor != null) cursor.close(); // Fecha o cursor para liberar memória

        // Inicia o timer para 30 segundos (30000ms)
        iniciarTimer(30000);
    }


    // Inicia o timer para contar o tempo restante (para ser mais dificil hehehe)
    private void iniciarTimer(long tempoEmMillis) {
        timer = new CountDownTimer(tempoEmMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Atualiza o timer a cada segundo
                int segundosRestantes = (int) (millisUntilFinished / 1000);
                txtTimer.setText(String.format("%02d:%02d", segundosRestantes / 60, segundosRestantes % 60));
            }

            @Override
            public void onFinish() {
                // Quando o tempo acabar o jogo ira acabar
                txtTimer.setText("00:00");
                Toast.makeText(Perguntas.this, "Tempo esgotado! Jogo terminado.", Toast.LENGTH_LONG).show();
                finish();
            }
        };
        timer.start();
    }

    // Verifica se a resposta está correta
    private void verificarResposta(String respostaSelecionada) {
        // Para a contagem do timer quando a resposta é dada
        if (timer != null) timer.cancel();

        if (respostaSelecionada.equals(correctAnswer)) {
            currentEarnings += 1000; // Cada resposta correta vale €1000
            Toast.makeText(this, "Resposta correta! Ganhou €1000.", Toast.LENGTH_SHORT).show();

            if (currentLevel < 5) { // Avança para o próximo nível se ainda houver perguntas
                currentLevel++;
                questionIndex++;
                carregarPergunta(questionIndex);
            } else { // Jogo completo
                Toast.makeText(this, "Parabéns! Prémio total: €" + currentEarnings, Toast.LENGTH_LONG).show();
                finish(); // Termina o jogo
            }
        } else {
            Toast.makeText(this, "Resposta errada! Jogo terminado.", Toast.LENGTH_LONG).show();
            finish(); // Termina o jogo
        }
    }

    // Troca a pergunta atual por outra
    private void trocarPergunta() {
        if (questionIndex == 6) questionIndex = 1; // Volta à primeira pergunta se estiver na última
        else questionIndex++;
        Toast.makeText(this, "Pergunta trocada!", Toast.LENGTH_SHORT).show();
        carregarPergunta(questionIndex); // Carrega uma nova pergunta
        btnHelpSwitch.setEnabled(false); // Desativa o botão de ajuda
    }
}
