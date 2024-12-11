package com.example.guilhermesales_eduardopaulino_dwm;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class Perguntas extends AppCompatActivity {

    private TextView txtLevel, txtEarnings, txtQuestion, txtTimer;
    private MaterialButton btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnHelpSwitch;
    private PerguntasDB perguntasDB;
    private int currentLevel = 1;
    private int currentEarnings = 0;
    private String correctAnswer;
    private List<PerguntasDB.Question> randomQuestions;
    private int currentQuestionIndex = 0;
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

        perguntasDB = new PerguntasDB(this);

        // Obtém 15 perguntas aleatórias
        randomQuestions = perguntasDB.getRandomQuestions();

        // Carrega a primeira pergunta
        carregarPergunta();

        // Configurar cliques nos botões de resposta
        btnAnswer1.setOnClickListener(v -> verificarResposta(btnAnswer1.getText().toString()));
        btnAnswer2.setOnClickListener(v -> verificarResposta(btnAnswer2.getText().toString()));
        btnAnswer3.setOnClickListener(v -> verificarResposta(btnAnswer3.getText().toString()));
        btnAnswer4.setOnClickListener(v -> verificarResposta(btnAnswer4.getText().toString()));

        // Configurar botão de ajuda para trocar de pergunta
        btnHelpSwitch.setOnClickListener(v -> trocarPergunta());
    }

    // Carrega a próxima pergunta da lista de perguntas aleatórias
    private void carregarPergunta() {
        // Verifica se ainda há perguntas disponíveis
        if (currentQuestionIndex < randomQuestions.size()) {
            PerguntasDB.Question currentQuestion = randomQuestions.get(currentQuestionIndex);

            // Atualiza as informações do nível e prémio
            txtLevel.setText("Nível: " + currentLevel);
            txtEarnings.setText("Prémio: €" + currentEarnings);

            // Carrega as perguntas
            txtQuestion.setText(currentQuestion.getQuestion());
            btnAnswer1.setText(currentQuestion.getAnswer1());
            btnAnswer2.setText(currentQuestion.getAnswer2());
            btnAnswer3.setText(currentQuestion.getAnswer3());
            btnAnswer4.setText(currentQuestion.getAnswer4());

            correctAnswer = currentQuestion.getCorrect();

            // Inicia o timer para 30 segundos
            iniciarTimer(30000);
        } else {
            // Todas as perguntas foram respondidas
            Toast.makeText(this, "Parabéns! Prémio total: €" + currentEarnings, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Inicia o timer para contar o tempo restante
    private void iniciarTimer(long tempoEmMillis) {
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(tempoEmMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int segundosRestantes = (int) (millisUntilFinished / 1000);
                txtTimer.setText(String.format("%02d:%02d", segundosRestantes / 60, segundosRestantes % 60));
            }

            @Override
            public void onFinish() {
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

            if (currentLevel < 5) { // Avança para o próximo nível
                currentLevel++;
                currentQuestionIndex++;
                carregarPergunta();
            } else { // Jogo completo
                Toast.makeText(this, "Parabéns! Prémio total: €" + currentEarnings, Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Resposta errada! Jogo terminado.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Troca a pergunta atual por outra
    private void trocarPergunta() {
        // Pula para a próxima pergunta
        currentQuestionIndex++;
        carregarPergunta();
        btnHelpSwitch.setEnabled(false); // Desativa o botão de ajuda
    }
}
