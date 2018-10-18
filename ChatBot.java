package Source;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;


public class ChatBot {
    UserState userState;

	String skipMessage = "Пропускаем этот вопрос."; 

    Question[] questions = {new Question("На озере расцвела одна лилия. " +
            "Каждый день число цветков удваивалось, и на двадцатый день все "
            + "\nозеро покрылось цветами. На какой день покрылась цветами "
            + "\nполовина озера?", Arrays.asList("19"), 1, "Если озеро полностью покрылось лилиями на 20-ый день," +
            " то наполовину оно покроется на 19 день, ведь каждый день количество лилий увеличивается вдвое",
            "\nОбратите внимание на то, во сколько раз увеличивается количество цветов  за день"),
            new Question("Сколько тонн земли находится в яме размера 2x2x2 метра",
                    Arrays.asList("0"), 1, "Ответ конечно же 0, откуда в яме земля, её же выкопали",
                    "\nВ ней будет столько же тонн земли, сколько и в яме 3х3х3"),
            new Question("Введите следующий символ последовательности С О Н Д Я Ф М А",
                    Arrays.asList("м"), 1, "Ответ буква \"м\", потому что буквы обозначают месяцы:" +
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
                            "зелёной"), 2, "Правильный ответ: в зелёной. Здесь работает простой метод исключения." +
                    "\nВ синей быть не может,в этом случае нарушается условие единственности правдивой надписи, так как на зеленой написано, что варенье в синей. " +
                    "\nВ то же время варенье не может быть и в красной, так как все надписи были бы лживы",
                    "\nПочитайте внимательно и постарайтесь исключить неправильные варианты, в конце концов, их всего три, а у вас есть три попытки, думаю, у вас все получится)")
    };

    private String ask(UserState userState)
    {
        int n = userState.getQuestionNumber();
        return questions[n].question;
    }
    
    String analyzeAnswer(UserState userState, String answer)
    {
    	answer = answer.toLowerCase();
    	String checkKeysRes = findKeys(userState, answer);
    	if (checkKeysRes != null)
    		//is it right check isNull?
    		return checkKeysRes;
    	return checkAnswer(userState, answer);
    }

    private String findKeys(UserState userState, String answer)
    {
        //if (answer.contains("готов"))
          //  return questions[userState.getQuestionNumber()].question;
    	if (answer.contains("помощь") || answer.contains("справка") || answer.contains("?")
    			|| answer.contains("-h"))
    		return getHelp();
    	if (answer.equals(" ") || answer.contains("пропустить") || answer.contains("следующий"))
    		return skipQuestion(userState);
    	if (answer.equals("подсказка") || answer.equals("hint"))
    	    return questions[userState.getQuestionNumber()].hint;
    	return null;
    }

    private String checkAnswer(UserState userState, String input)
    {
        String output;
        int scores;
        int questionNumber = userState.getQuestionNumber();
        if (questions[questionNumber].answers.contains(input))
        {
            output = "Правильный ответ!";
            scores = questions[questionNumber].cost;
            userState.addScores(scores);
            userState.moveToNextQuestion();
        }
        else if (questions[questionNumber].attempts == 2)
        {
            output = String.format("Неправильный ответ, у тебя осталось %d попытки", questions[questionNumber].attempts);
            questions[questionNumber].attempts -= 1;
        }
        else if (questions[questionNumber].attempts == 1)
        {
            output = "Неправильный ответ, у тебя осталось последняя попытка";
            questions[questionNumber].attempts -= 1;
        }
        else
        {
            output = String.format("У тебя закончились попытки. \n%s \nПереходим к следующему вопросу",
                    questions[questionNumber].explanation);
            userState.moveToNextQuestion();
        }
        return output;
    }
    //надо ли выносить изменение очков в отдельный метод?

    private String skipQuestion(UserState userState)
    {
    	userState.moveToNextQuestion();
    	return skipMessage;
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
                "\n Готов начать? Нажми клавишу Enter."+
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
        return String.format("Поздравляю, %s, ты набрал %d очк%s", userState.name, finalScore, getEnding(finalScore));
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
            System.out.println(analyzeAnswer(userState, answer));
        }
        System.out.println(getSessionResult(userState));
        input.close();
    }

    @FXML
    private TextArea chatArea;
    @FXML
    private TextArea answerArea;
    @FXML
    private Button nameEnter;
    public void realisationForGUI(UserState userState)
    {
        String input = answerArea.getText();
        if (!input.equals(""))
        {
            chatArea.appendText("\n[ВЫ]: " + input);
            chatArea.appendText("\n[БОТ]:" + analyzeAnswer(userState, input));
        }
        answerArea.deleteText(0, input.length());
        if (userState.getQuestionNumber() == questions.length)
            chatArea.appendText("\n[БОТ]:" + getSessionResult(userState));
    }
    public void showInputTextDialog()
    {
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle("Приветствие");
        dialog.setHeaderText("Как тебя зовут?");
        dialog.setContentText("Имя:");
        Optional<String> result = dialog.showAndWait();

        String userName = result.toString();
        userState = new UserState(userName);
        chatArea.appendText("\n[БОТ]:" + getWelcomeMessage(userName));
        nameEnter.setDisable(true);
    }
    public void onAnswer(ActionEvent actionEvent)
    {
        realisationForGUI(userState);
        chatArea.appendText("\n[БОТ]: " + ask(userState));
    }
}
