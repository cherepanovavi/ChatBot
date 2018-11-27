package Source.Application;

import Source.LogicFiles.ChatBot;
import Source.LogicFiles.UserState;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

public class Controller {
    private ChatBot chatBot = new ChatBot();
    private UserState userState;
    private UsersBaseChanger usersBaseChanger = new UsersBaseChanger();
    private ObservableList<UserState> usersList = usersBaseChanger.selectFromDataBase();

//    private void feelTestData() {
//        usersList.add(new UserState("Артем"));
//        usersList.add(new UserState("Nikita"));
//        usersList.add(new UserState("Gregory"));
//    }

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
        nameEnter.setDisable(true);
        nameEnter.setVisible(false);
        answerButton.setPrefWidth(answerButton.getWidth() + nameEnter.getWidth());
    }

    public void onAnswer(ActionEvent actionEvent) {
        realisationForGUI(userState);
    }
}
