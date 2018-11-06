package Source;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;


public class ChatBot {
    private UserState userState;
    private ObservableList<UserState> usersList = FXCollections.observableArrayList();
    static int attemptsCount = 2;

    void feelTestData()
    {
        usersList.add(new UserState("Артем"));
        usersList.add(new UserState("Nikita"));
        usersList.add(new UserState("Gregory"));
    }

	String getSkipMessage() {return "Пропускаем этот вопрос.";}

    Question[] questions = {new Question("На озере расцвела одна лилия. " +
            "Каждый день число цветков удваивалось, и на двадцатый день все "
            + "\nозеро покрылось цветами. На какой день покрылась цветами "
            + "\nполовина озера?", Arrays.asList("19", "девятнадцать"), 1, "Если озеро полностью покрылось лилиями на 20-ый день," +
            " то наполовину оно покроется на 19 день, ведь каждый день количество лилий увеличивается вдвое",
            "\nОбратите внимание на то, во сколько раз увеличивается количество цветов  за день"),
            new Question("Сколько тонн земли находится в яме размера 2x2x2 метра",
                    Arrays.asList("0", "ноль"), 1, "Ответ конечно же 0, откуда в яме земля, её же выкопали",
                    "\nВ ней будет столько же тонн земли, сколько и в яме 3х3х3"),
            new Question("Введите следующий символ последовательности С О Н Д Я Ф М А",
                    Arrays.asList("м", "май"), 1, "Ответ буква \"м\", потому что буквы обозначают месяцы:" +
                    " Сентябрь, Октябрь и так далее до Апреля, а после Апреля идет Май",
                    "\nНа самом деле букв в последовательности должно быть 12"),
            new Question("Обогнав в спринте третьего бегуна на каком месте вы окажетесь",
                    Arrays.asList("3", "третий", "третьем", "на третьем"), 1, "Вы будете на третьем месте," +
                    " все же легко, просто представьте себя участником такого забега",
                    "\nВы будете на золоте)"),
            new Question("Малыш спрятал от Карлсона банку с вареньем в "
                    + "\nодну из трех разноцветных коробок. На коробках"
                    + "\nМалыш сделал надписи: на красной – «Здесь варенья нет»; "
                    + "\nна синей – «Варенье - здесь»; на зеленой – "
                    + "\n«Варенье в синей коробке». Только одна из надписей п"
                    + "\nравдива. В какой коробке Малыш спрятал варенье?",
                    Arrays.asList("в зеленой", "в зелёной", "зеленой",
                            "зелёной", "зеленая", "зелёная"), 2, "Правильный ответ: в зелёной. Здесь работает простой метод исключения." +
                    "\nВ синей быть не может,в этом случае нарушается условие единственности правдивой надписи, так как на зеленой написано, что варенье в синей. " +
                    "\nВ то же время варенье не может быть и в красной, так как все надписи были бы лживы",
                    "\nПочитайте внимательно и постарайтесь исключить неправильные варианты, в конце концов, их всего три, а у вас есть три попытки, думаю, у вас все получится)")
    };

    private String ask(UserState userState)
    {
        int n = userState.getQuestionNumber();
        return questions[n].getQuestion();
    }
    
    ArrayList<String> analyzeAnswer(UserState userState, String answer)
    {
        var result = new ArrayList<String>();
        var qN1 = userState.getQuestionNumber();
    	answer.toLowerCase();
    	String checkKeysRes = findKeys(userState, answer);
    	if (checkKeysRes != null)
    		result.add(checkKeysRes);
    	else if (qN1 == -1) {
            userState.moveToNextQuestion();
        }
        else
            result.add(checkAnswer(userState, answer));
        var qN2 = userState.getQuestionNumber();
    	if ((qN2 > qN1) && (qN2 < questions.length))
    	    result.add(ask(userState));
    	return result;
    }

    private String findKeys(UserState userState, String answer)
    {
    	if (answer.contains("помощь") || answer.contains("справка") || answer.contains("?")
    			|| answer.contains("-h"))
    		return getHelp();
    	if (answer.equals(" ") || answer.contains("пропустить") || answer.contains("следующий"))
    		return skipQuestion(userState);
    	if (answer.equals("подсказка") || answer.equals("hint"))
    	    return questions[userState.getQuestionNumber()].getHint();
    	return null;
    }

    private String checkAnswer(UserState userState, String input)
    {
        String output;
        int scores;
        int questionNumber = userState.getQuestionNumber();
        if (questions[questionNumber].isRightAnswer(input))
        {
            output = "Правильный ответ!";
            scores = questions[questionNumber].cost;
            userState.addScores(scores);
            userState.moveToNextQuestion();
        }
        else if (userState.getQuestionAttempts() > 0)
        {
            output = String.format("Неправильный ответ, у тебя осталось %d попытки", userState.getQuestionAttempts());
            userState.spendAnAttempt();
        }
        else
        {
            output = String.format("У тебя закончились попытки. \n%s \nПереходим к следующему вопросу",
                    questions[questionNumber].getExplanation());
            userState.moveToNextQuestion();
        }
        return output;
    }
    //надо ли выносить изменение очков в отдельный метод?

    private String skipQuestion(UserState userState)
    {
    	userState.moveToNextQuestion();
    	return getSkipMessage();
    }

    String getHelp()
    {
    	return "Напиши свой ответ в следующей строке или введи \"пропустить\\следующий\", чтобы пропустить вопрос. " +
                "\nТы можешь попросить подсказку по вопросу, для этого напиши \"подсказка\\hint\"" +
                "\nПока никаких фич у меня больше нет, простите";
    }

    private String getWelcomeMessage(String userName)
    {
    	return String.format("Привет, %s! Предлагаю тебе ответить на несколько каверзных вопросов" +
                "\n Готов начать? Отправь любое сообщение"+
    			"\n Хочешь узнать больше информации отправь мне сообщение, содержащее слово \"справка\" "
                , userName);
    }

    String getEnding(int score)
    {
    	int modScore = score % 10;
    	if (modScore == 1)
    		return "о";
    	if (modScore > 1 && modScore < 5)
    		return "а";
    	return "ов";
    }

    private String getSessionResult(UserState userState)
    {
    	int finalScore = userState.getScore();
        return String.format("Поздравляю, %s, ты набрал %d очк%s", userState.getName(), finalScore, getEnding(finalScore));
    }

    public void consoleRealisation()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Как тебя зовут?");
        String userName = input.nextLine();
        UserState userState = new UserState(userName);
        System.out.println(getWelcomeMessage(userName));  
        String output = findKeys(userState, input.nextLine().toLowerCase());
        if (output != null)
        	System.out.println(output);
        int length = questions.length;
        while (userState.getQuestionNumber() < length)
        {
            System.out.println(ask(userState));
            String answer = input.nextLine().toLowerCase();
            var botAnswer = analyzeAnswer(userState, answer);
            for (var bAnswer:botAnswer)
            {
                if(bAnswer != null)
                    System.out.println(bAnswer);
            }
        }
        System.out.println(getSessionResult(userState));
        input.close();
    }

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
    private void initialize()
    {
        userColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        attemptsColumn.setCellValueFactory(new PropertyValueFactory<>("questionAttempts"));
        feelTestData();
        usersList.addListener((ListChangeListener) (c) -> updateTable());
        //usersTable.setItems(usersList);

    }
    public void updateTable()
    {
        usersTable.setItems(usersList);
    }

    public void realisationForGUI(UserState userState)
    {
        String input = answerArea.getText();
        if (!input.equals("") && userState.getName() != null)
        {
            chatArea.appendText("\n[ВЫ]: " + input);
            var botAnswer = analyzeAnswer(userState, input);
            for (var answer:botAnswer)
            {
                if(answer != null)
                    chatArea.appendText("\n[БОТ]:" + answer);
            }
        }
        answerArea.deleteText(0, input.length());
        if (userState.getQuestionNumber() == questions.length)
            chatArea.appendText("\n[БОТ]:" + getSessionResult(userState));
    }
    public void showInputTextDialog()
    {
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
        chatArea.appendText("\n[БОТ]:" + getWelcomeMessage(userName));
        nameEnter.setDisable(true);
        nameEnter.setVisible(false);
        answerButton.setPrefWidth(answerButton.getWidth() + nameEnter.getWidth());
    }
    public void onAnswer(ActionEvent actionEvent)
    {
        realisationForGUI(userState);
    }
}
