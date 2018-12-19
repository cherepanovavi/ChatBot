package Source.Application.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminAccessController {
    private final String adminName = "Deniskin";
    private final String passWord = "IamAdmin";

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label wrongDataLabel;

    public void showAdminWindow(ActionEvent actionEvent) {
        String inputtedPassword = passwordField.getText();
        String inputtedName = userNameField.getText();

        if (!inputtedName.equals(adminName) || !inputtedPassword.equals(passWord)) {
            wrongDataLabel.setText("Неверное имя пользователя или пароль");
            userNameField.deleteText(0, inputtedName.length());
            passwordField.deleteText(0, inputtedPassword.length());
        }
        else
        {
            ((Node)actionEvent.getSource()).getScene().getWindow().hide();
            try
            {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../fxmlFiles/adminWin.fxml"));
                stage.setTitle("Изменение базы вопросов");
                stage.setMinWidth(600);
                stage.setMinHeight(800);
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

    }
}
