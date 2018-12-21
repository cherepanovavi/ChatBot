package Application.Controllers;

import LogicFiles.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class QuestionEditorController {

    @FXML
    private TextArea questionArea;

    @FXML
    private TextField answersArea;

    @FXML
    private TextField costArea;

    @FXML
    private TextField explanationArea;

    @FXML
    private TextField hintArea;

    private Question question;

    public void setQuestion(Question question)
    {
        if (question.getQuestion() == null) {
            questionArea.setText("");
            answersArea.setText("");
            costArea.setText("");
            explanationArea.setText("");
            hintArea.setText("");
        }
        else {
            this.question = question;
            questionArea.setText(question.getQuestion());
            answersArea.setText(question.getAnswers().toString());
            costArea.setText(String.valueOf(question.getCost()));
            explanationArea.setText(question.getExplanation());
            hintArea.setText(question.getHint());
        }
    }

    public Question getQuestion(){ return question; }


    public void actionClose(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.hide();
    }

    public void actionSave(ActionEvent actionEvent) {
        ObservableList<String> answers = FXCollections.observableArrayList();
        question.setQuestion(questionArea.getText());
        var strings = answersArea.getText().substring(1, answersArea.getLength() - 1).split(",");
        answers.addAll(strings);
        question.setAnswers(answers);
        question.setCost(Integer.valueOf(costArea.getText()));
        question.setExplanation(explanationArea.getText());
        question.setHint(hintArea.getText());
        actionClose(actionEvent);
    }
}
