package main.java.Application.Controllers;

import main.java.LogicFiles.Parser;
import main.java.LogicFiles.Question;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.ArrayList;


public class AdminWinController {

    private ObservableList<Question> questions = Parser.parseQuestions(Parser.getFileFromGit());
    private Stage adminWin;

    @FXML
    private TableView questionsTable;

    @FXML
    private TableColumn<Question, String> questionsColumn;

    @FXML
    private TableColumn<Question, ?> answersColumn;

    @FXML
    private TableColumn<Question, Integer> costColumn;

    @FXML
    private TableColumn<Question, String>  explanationColumn;

    @FXML
    private TableColumn<Question, String> hintColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button changeButton;

    private Parent fxmlEdit;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private QuestionEditorController questionEditorController;
    private Stage questionEditor;

    public void setAdminStage(Stage adminStage) { this.adminWin = adminStage; }

    @FXML
    private void initialize()
    {
        questionsTable.setItems(questions);
        questionsColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        answersColumn.setCellValueFactory(new PropertyValueFactory<>("answers"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        explanationColumn.setCellValueFactory(new PropertyValueFactory<>("explanation"));
        hintColumn.setCellValueFactory(new PropertyValueFactory<>("hint"));

        try
        {
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("fxmlFiles/questionEditor.fxml"));
            fxmlEdit = fxmlLoader.load();
            questionEditorController = fxmlLoader.getController();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        questions.addListener((ListChangeListener) (c) -> updateTable());

        questionsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2)
                {
                    questionEditorController.setQuestion((Question)questionsTable.getSelectionModel().getSelectedItem());
                    makeEditorWindow();
                }
            }
        });
    }

    private void updateTable() {
        questionsTable.setItems(questions);
    }

    private void reWriteJSONFile()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/LogicFiles/question_base")))
        {
            String text = Parser.createJSON(questions);
            writer.write(text);
            writer.append("\r\n");
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void pushToGit()
    {
        var command = "cmd /c start cmd.exe /K \"cd src/main/java && git add LogicFiles/question_base && git commit -m \"База вопросов изменена администратором\" && git push origin telegram && exit\"";
        try
        {
            Process proc = Runtime.getRuntime().exec(command);
            proc.waitFor();
            proc.destroy();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    public void saveChanges(ActionEvent actionEvent) {
        reWriteJSONFile();
    }

    public void saveChangesAndPush(ActionEvent actionEvent) {
        reWriteJSONFile();
        pushToGit();
    }

    private void makeEditorWindow()
    {
        if (questionEditor == null) {
            questionEditor = new Stage();
            questionEditor.setTitle("Окно изменения/добавления вопросов");
            questionEditor.setMinWidth(800);
            questionEditor.setMinHeight(800);
            questionEditor.setResizable(true);
            questionEditor.setScene(new Scene(fxmlEdit));
            questionEditor.initModality(Modality.APPLICATION_MODAL);
            questionEditor.initOwner(adminWin);
        }

        questionEditor.showAndWait();
    }

    public void showEditorWin(ActionEvent actionEvent) {

        var buttonName = (Button)actionEvent.getSource();

        Question question = (Question)questionsTable.getSelectionModel().getSelectedItem();

        switch(buttonName.getId())
        {
            case "changeButton":
            {
                questionEditorController.setQuestion(question);
                makeEditorWindow();
                break;
            }
            case "addButton":
            {
                questionEditorController.setQuestion(new Question("", FXCollections.observableArrayList(), 0, " ", ""));
                makeEditorWindow();
                questions.add(questionEditorController.getQuestion());
                break;
            }
            case "deleteButton":
            {
                questions.remove(question);
                break;
            }
        }
    }
}
