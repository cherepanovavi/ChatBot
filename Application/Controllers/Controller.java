package Source.Application.Controllers;

import Source.Application.UsersBaseChanger;
import Source.LogicFiles.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Controller implements ISender{
    private ChatBot chatBot = new ChatBot( new InitializingManager(this, 120000));
    private UserState userState;
    private UsersBaseChanger usersBaseChanger = new UsersBaseChanger();
    private ObservableList<UserState> usersList = usersBaseChanger.selectFromDataBase();

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField answerArea;

    @FXML
    private Button nameEnter;

    @FXML
    private Button answerButton;

    @FXML
    private TableView usersTable;

    @FXML
    private TableColumn<?, ?> userColumn;

    @FXML
    private TableColumn<?, ?> scoreColumn;

    @FXML
    private TableColumn<?, ?> attemptsColumn;

    @FXML
    private Button adminButton;

    @FXML
    private void initialize() {
        userColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        attemptsColumn.setCellValueFactory(new PropertyValueFactory<>("questionAttempts"));
//        feelTestData();
        usersList.addListener((ListChangeListener) (c) -> updateTable());
        //usersTable.setItems(usersList);

    }

    private void updateTable() {
        usersTable.setItems(usersList);
    }

    private void realisationForGUI(UserState userState) {
        String input = answerArea.getText();
        if (!input.equals("") && userState.getName() != null) {
            chatArea.appendText("\n[ВЫ]: " + input);
            var botAnswer = chatBot.analyzeAnswer(userState, input);
            for (var answer : botAnswer) {
                if (answer != null)
                    chatArea.appendText("\n[БОТ]:" + answer);
            }
        }
        answerArea.deleteText(0, input.length());
        if (userState.getQuestionNumber() == chatBot.questions.length) {
            usersBaseChanger.insertIntoDataBase(userState);
            chatArea.appendText("\n[БОТ]:" + chatBot.getSessionResult(userState));
            userState.restart();
        }
    }

    public void showInputTextDialog() {
        TextInputDialog dialog = new TextInputDialog("User");

        dialog.setTitle("Приветствие");
        dialog.setHeaderText("Как тебя зовут?");
        dialog.setContentText("Имя:");
        Optional<String> result = dialog.showAndWait();

        String userName;
        if (result.isPresent() && !result.get().equals(""))
            userName = result.get();
        else
            userName = dialog.getDefaultValue();

        userState = new UserState(userName);
        usersList.add(userState);
        chatArea.appendText("\n[БОТ]:" + chatBot.getWelcomeMessage(userName));
        adminButton.setDisable(true);
        nameEnter.setDisable(true);
        nameEnter.setVisible(false);
        answerButton.setPrefWidth(answerButton.getWidth() + nameEnter.getWidth());
    }

    public void onAnswer(ActionEvent actionEvent) {
        realisationForGUI(userState);
    }

    public void showLoggingInWindow(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../fxmlFiles/adminLogIn.fxml"));
            stage.setTitle("Вход в качестве администратора");
            stage.setMinWidth(400);
            stage.setMinHeight(200);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(UserState userState, String message) {
        chatArea.appendText("\n[БОТ]:" + message);
    }
}
