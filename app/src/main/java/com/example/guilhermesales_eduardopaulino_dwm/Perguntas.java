package com.example.guilhermesales_eduardopaulino_dwm;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class Perguntas extends AppCompatActivity {

    private TextView txtLevel, txtEarnings, txtQuestion;
    private MaterialButton btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4, btnHelpSwitch;
    private PerguntasDB perguntasDB; // Base de dados para as perguntas
    private int currentLevel = 1; // Nível atual do jogo
    private int currentEarnings = 0; // Prémio acumulado
    private String correctAnswer; // Resposta correta atual
    private int questionIndex = 1; // Índice da pergunta atual (1-6)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perguntas);

        // Inicializar elementos da interface
        txtLevel = findViewById(R.id.txtLevel);
        txtEarnings = findViewById(R.id.txtEarnings);
        txtQuestion = findViewById(R.id.txtQuestion);

        btnAnswer1 = findViewById(R.id.btnAnswer1);
        btnAnswer2 = findViewById(R.id.btnAnswer2);
        btnAnswer3 = findViewById(R.id.btnAnswer3);
        btnAnswer4 = findViewById(R.id.btnAnswer4);
        btnHelpSwitch = findViewById(R.id.btnHelpSwitch);

        perguntasDB = new PerguntasDB(this); // Inicializa a base de dados
        loadQuestion(questionIndex); // Carrega a primeira pergunta

        // Configurar cliques nos botões de resposta
        btnAnswer1.setOnClickListener(v -> checkAnswer(btnAnswer1.getText().toString()));
        btnAnswer2.setOnClickListener(v -> checkAnswer(btnAnswer2.getText().toString()));
        btnAnswer3.setOnClickListener(v -> checkAnswer(btnAnswer3.getText().toString()));
        btnAnswer4.setOnClickListener(v -> checkAnswer(btnAnswer4.getText().toString()));

        // Configurar botão de ajuda para trocar de pergunta
        btnHelpSwitch.setOnClickListener(v -> switchQuestion());
    }

    // Carrega a pergunta e respostas da base de dados
    private void loadQuestion(int questionId) {
        Cursor cursor = perguntasDB.getQuestion(questionId);

        if (cursor.moveToFirst()) {
            txtLevel.setText("Nível: " + currentLevel);
            txtEarnings.setText("Prêmio: €" + currentEarnings);

            txtQuestion.setText(cursor.getString(1));
            btnAnswer1.setText(cursor.getString(2));
            btnAnswer2.setText(cursor.getString(3));
            btnAnswer3.setText(cursor.getString(4));
            btnAnswer4.setText(cursor.getString(5));

            correctAnswer = cursor.getString(6); // Define a resposta correta
        } else {
            Toast.makeText(this, "Erro ao carregar a pergunta.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }
    // Verifica se a resposta está correta
    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            currentEarnings += 1000; // Cada resposta correta vale €1000
            Toast.makeText(this, "Resposta correta! Ganhou €1000.", Toast.LENGTH_SHORT).show();

            if (currentLevel < 5) { // Avança para o próximo nível se ainda houver perguntas
                currentLevel++;
                questionIndex++;
                loadQuestion(questionIndex);
            } else { // Jogo completo
                Toast.makeText(this, "Parabéns! Prêmio total: €" + currentEarnings, Toast.LENGTH_LONG).show();
                finish(); // Termina o jogo
            }
        } else {
            Toast.makeText(this, "Resposta errada! Jogo terminado.", Toast.LENGTH_LONG).show();
            finish(); // Termina o jogo
        }
    }

    // Troca a pergunta atual por outra
    private void switchQuestion() {
        if (questionIndex == 6) questionIndex = 1; // Volta à primeira pergunta se estiver na última
        else questionIndex++;
        Toast.makeText(this, "Pergunta trocada!", Toast.LENGTH_SHORT).show();
        loadQuestion(questionIndex); // Carrega uma nova pergunta
        btnHelpSwitch.setEnabled(false); // Desativa o botão de ajuda
    }
}
