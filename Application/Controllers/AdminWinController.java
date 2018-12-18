package Source.Application.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class AdminWinController {

    @FXML
    private TextArea editionArea;

    @FXML
    private Label changesLabel;

    @FXML
    private void initialize()
    {
        editionArea.setText(readJSONFile());
    }

    private String readJSONFile()
    {
        try(BufferedReader br = new BufferedReader(new FileReader("src/Source/LogicFiles/question_base")))
        {
            StringBuilder stringBuilder = new StringBuilder();
            String line = br.readLine();

            while(line != null){
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
                line = br.readLine();
            }
            return stringBuilder.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private void reWriteJSONFile()
    {
        try (FileWriter writer = new FileWriter("src/Source/LogicFiles/question_base"))
        {
            String text = editionArea.getText();
            writer.write(text);
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void pushToGit()
    {
        var command = "cmd /c start cmd.exe /K \"cd source && git add LogicFiles/question_base && git commit -m \"База вопросов изменена администратором\" && git push origin telegram\"";
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
        changesLabel.setText("Изменения успешно сохранены");
    }

    public void saveChangesAndPush(ActionEvent actionEvent) {
        reWriteJSONFile();
        pushToGit();
        changesLabel.setText("Изменения сохранены и запушены");
    }
}
