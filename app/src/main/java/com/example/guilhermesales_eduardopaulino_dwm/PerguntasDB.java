package com.example.guilhermesales_eduardopaulino_dwm;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PerguntasDB {

    private final List<Question> questions;

    public PerguntasDB(Context context) {
        questions = new ArrayList<>();
        carregarPerguntas();
    }

    private void carregarPerguntas() {
        questions.add(new Question("Qual é a capital de Portugal?", "Lisboa", "Porto", "Braga", "Faro", "Lisboa"));
        questions.add(new Question("Quantos continentes existem?", "5", "6", "7", "8", "7"));
        questions.add(new Question("Quem pintou a Mona Lisa?", "Van Gogh", "Leonardo da Vinci", "Picasso", "Michelangelo", "Leonardo da Vinci"));
        questions.add(new Question("Qual é o maior oceano do mundo?", "Atlântico", "Pacífico", "Índico", "Ártico", "Pacífico"));
        questions.add(new Question("Qual é o metal mais precioso?", "Prata", "Platina", "Ouro", "Paládio", "Ouro"));
        questions.add(new Question("Qual é o maior planeta do sistema solar?", "Terra", "Júpiter", "Saturno", "Urano", "Júpiter"));
        questions.add(new Question("Qual é o país mais populoso do mundo?", "Índia", "Estados Unidos", "Brasil", "China", "China"));
        questions.add(new Question("Quantos graus tem um triângulo?", "180", "360", "90", "120", "180"));
        questions.add(new Question("Em que ano terminou a Segunda Guerra Mundial?", "1940", "1942", "1945", "1950", "1945"));
        questions.add(new Question("Qual é o símbolo químico da água?", "HO", "H2O", "O2", "OH", "H2O"));
        questions.add(new Question("Quantas cores tem o arco-íris?", "6", "7", "8", "9", "7"));
        questions.add(new Question("Qual é o animal mais rápido do mundo?", "Guepardo", "Falcão-peregrino", "Leopardo", "Cavalo", "Falcão-peregrino"));
        questions.add(new Question("Quem foi o primeiro homem a pisar na Lua?", "Neil Armstrong", "Buzz Aldrin", "Yuri Gagarin", "Michael Collins", "Neil Armstrong"));
        questions.add(new Question("Qual é o nome do maior deserto do mundo?", "Sahara", "Gobi", "Atacama", "Antártida", "Antártida"));
        questions.add(new Question("Quantos minutos tem uma hora?", "60", "120", "30", "90", "60"));
        questions.add(new Question("Qual é a montanha mais alta do mundo?", "Everest", "K2", "Kilimanjaro", "Makalu", "Everest"));
        questions.add(new Question("Quem escreveu os Lusiadas ?", "Camões", "Pessoa", "Saramago", "Eça de Queirós", "Camões"));
        questions.add(new Question("Qual é o rio mais longo do mundo?", "Nilo", "Amazonas", "Yangtzé", "Mississípi", "Amazonas"));
        questions.add(new Question("Em que país se encontra a Torre Eiffel?", "Itália", "Espanha", "França", "Inglaterra", "França"));
        questions.add(new Question("Qual é a língua mais falada no mundo?", "Inglês", "Espanhol", "Chinês", "Hindi", "Inglês"));
        questions.add(new Question("Qual é o país conhecido como a Terra do Sol Nascente?", "China", "Japão", "Coreia", "Tailândia", "Japão"));
        questions.add(new Question("Qual é o maior mamífero do mundo?", "Elefante", "Baleia-azul", "Hipopótamo", "Rinoceronte", "Baleia-azul"));
        questions.add(new Question("Qual é o órgão mais pesado do corpo humano?", "Cérebro", "Fígado", "Coração", "Pulmão", "Fígado"));
        questions.add(new Question("Quantos lados tem um hexágono?", "5", "6", "7", "8", "6"));
        questions.add(new Question("Qual é o nome do presidente dos Estados Unidos em 2021?", "Donald Trump", "Joe Biden", "Barack Obama", "George Bush", "Joe Biden"));
        questions.add(new Question("Qual é o nome científico do ser humano?", "Homo habilis", "Homo sapiens", "Homo erectus", "Homo neanderthalensis", "Homo sapiens"));
        questions.add(new Question("Qual é o menor país do mundo?", "Mónaco", "Vaticano", "Malta", "Andorra", "Vaticano"));
        questions.add(new Question("Qual é o instrumento utilizado para medir a temperatura?", "Termómetro", "Barómetro", "Higrómetro", "Anemómetro", "Termómetro"));
        questions.add(new Question("Quantos dias tem um ano bissexto?", "365", "366", "364", "368", "366"));
        questions.add(new Question("Qual é a fórmula química do dióxido de carbono?", "CO", "CO2", "O2", "C2O", "CO2"));
    }

    public List<Question> getRandomQuestions() {
        Collections.shuffle(questions);
        return questions.subList(0, Math.min(questions.size(), 30));
    }

    public static class Question {
        private final String question;
        private final String answer1;
        private final String answer2;
        private final String answer3;
        private final String answer4;
        private final String correct;

        public Question(String question, String answer1, String answer2, String answer3, String answer4, String correct) {
            this.question = question;
            this.answer1 = answer1;
            this.answer2 = answer2;
            this.answer3 = answer3;
            this.answer4 = answer4;
            this.correct = correct;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer1() {
            return answer1;
        }

        public String getAnswer2() {
            return answer2;
        }

        public String getAnswer3() {
            return answer3;
        }

        public String getAnswer4() {
            return answer4;
        }

        public String getCorrect() {
            return correct;
        }
    }
}
