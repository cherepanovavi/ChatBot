package main.java.LogicFiles;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.util.List;

public class Question {
    private SimpleStringProperty question;
    private SimpleListProperty<String> answers;
    private SimpleIntegerProperty cost;
    private SimpleStringProperty explanation;
    private SimpleStringProperty hint;


    public Question(String question, List<String> answers, Integer cost, String explanation, String hint) {
        this.question = new SimpleStringProperty(question);
        this.answers = new SimpleListProperty<>(FXCollections.observableArrayList(answers));
        this.cost = new SimpleIntegerProperty(cost);
        this.explanation = new SimpleStringProperty(explanation);
        this.hint = new SimpleStringProperty(hint);
    }

    public int getCost(){
        return cost.get();
    }

    public String getHint() { return hint.get(); }

    public String getQuestion() {
        return question.get();
    }

    public String getExplanation() {
        if (explanation.get().charAt(0) == '?') {
            return Parser.getWikiSearchResult(explanation.get().substring(1));
        }
        return explanation.get();
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public void setAnswers(ObservableList<String> answers) {
        this.answers.set(answers);
    }

    public void setCost(int cost) {
        this.cost.set(cost);
    }

    public void setExplanation(String explanation) {
        this.explanation.set(explanation);
    }

    public void setHint(String hint) {
        this.hint.set(hint);
    }

    public List<String> getAnswers(){return answers.get();}

    public SimpleStringProperty questionProperty(){return question;}

    public SimpleStringProperty explanationProperty(){return explanation;}

    public SimpleListProperty answersProperty(){return answers;}

    public SimpleIntegerProperty costProperty(){return cost;}

    public SimpleStringProperty hintProperty(){return hint;}

    public boolean isRightAnswer(String answer) {
        return answers.contains(answer);
    }
}
