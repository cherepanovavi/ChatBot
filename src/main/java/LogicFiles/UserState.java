package main.java.LogicFiles;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserState {
    private SimpleIntegerProperty score;
    private SimpleStringProperty name;
    private int questionNumber;
    private SimpleIntegerProperty questionAttempts;
    private Long id;

    public UserState(String username) {
        this.score = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty(username);
        this.questionNumber = -1;
        this.questionAttempts = new SimpleIntegerProperty(ChatBot.attemptsCount);
    }

    public UserState(String username, Long id){
        this.id = id;
        this.score = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty(username);
        this.questionNumber = -1;
        this.questionAttempts = new SimpleIntegerProperty(ChatBot.attemptsCount);
    }

    public Long getId(){return id;}

    public void restart(){
        score = new SimpleIntegerProperty(0);
        questionNumber = -1;
        questionAttempts = new SimpleIntegerProperty(ChatBot.attemptsCount);
    }

    public void addScores(int newScores) {
        setScore(getScore() + newScores);
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public void setQuestionAttempts(int questionAttempts) {
        this.questionAttempts.set(questionAttempts);
    }

    public int getScore() {
        return score.get();
    }

    public String getName() {
        return name.get();
    }

    public int getQuestionAttempts() {
        return questionAttempts.get();
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleIntegerProperty questionAttemptsProperty() {
        return questionAttempts;
    }

    public void spendAnAttempt() {
        setQuestionAttempts(getQuestionAttempts() - 1);
    }

    public void moveToNextQuestion() {

        questionNumber += 1;
        setQuestionAttempts(ChatBot.attemptsCount);
    }
}

